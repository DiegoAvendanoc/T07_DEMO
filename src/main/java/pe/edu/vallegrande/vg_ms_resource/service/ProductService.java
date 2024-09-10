package pe.edu.vallegrande.vg_ms_resource.service;

import io.github.resilience4j.ratelimiter.RateLimiter;
import io.github.resilience4j.ratelimiter.RateLimiterConfig;
import io.github.resilience4j.ratelimiter.RateLimiterRegistry;
import jakarta.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pe.edu.vallegrande.vg_ms_resource.model.Product;
import pe.edu.vallegrande.vg_ms_resource.repository.ProductRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;

import java.time.Duration;

@Service
public class ProductService {

	@Autowired
	private ProductRepository productRepository;

	@Autowired
	private RateLimiterRegistry rateLimiterRegistry;

	private RateLimiter rateLimiter;

	@PostConstruct
	public void init() {
		this.rateLimiter = rateLimiterRegistry.rateLimiter("productServiceRateLimiter");
	}

	public Flux<Product> getAllProducts() {
		return Flux.defer(
				() -> Mono.fromCallable(() -> rateLimiter.acquirePermission()).thenMany(productRepository.findAll()))
				.retryWhen(Retry.fixedDelay(3, Duration.ofSeconds(1)));
	}

	public Mono<Product> getProductById(String id) {
		return Mono.defer(
				() -> Mono.fromCallable(() -> rateLimiter.acquirePermission()).then(productRepository.findById(id)))
				.retryWhen(Retry.fixedDelay(3, Duration.ofSeconds(1)));
	}

	public Mono<Product> createProduct(Product product) {
		return Mono.defer(
				() -> Mono.fromCallable(() -> rateLimiter.acquirePermission()).then(productRepository.save(product)))
				.retryWhen(Retry.fixedDelay(3, Duration.ofSeconds(1)));
	}

	public Mono<Product> updateProduct(String id, Product product) {
		return productRepository.findById(id).flatMap(existingProduct -> {
			if (product.getName() != null) {
				existingProduct.setName(product.getName());
			}
			if (product.getDescription() != null) {
				existingProduct.setDescription(product.getDescription());
			}
			if (product.getPrice() != 0) {
				existingProduct.setPrice(product.getPrice());
			}
			if (product.getQuantity() != 0) {
				existingProduct.setQuantity(product.getQuantity());
			}
			existingProduct.setId(id);
			return Mono.fromCallable(() -> rateLimiter.acquirePermission())
					.then(productRepository.save(existingProduct))
					.retryWhen(Retry.fixedDelay(3, Duration.ofSeconds(1)));
		}).switchIfEmpty(Mono.error(new RuntimeException("Product not found")));
	}

	public Mono<Void> deleteProduct(String id) {
		return Mono.defer(
				() -> Mono.fromCallable(() -> rateLimiter.acquirePermission()).then(productRepository.deleteById(id)))
				.retryWhen(Retry.fixedDelay(3, Duration.ofSeconds(1)));
	}
}
