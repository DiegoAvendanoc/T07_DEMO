package pe.edu.vallegrande.vg_ms_resource.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import pe.edu.vallegrande.vg_ms_resource.model.Product;
import pe.edu.vallegrande.vg_ms_resource.service.ProductService;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/products")
public class ProductController {

	@Autowired
	private ProductService productService;

	@GetMapping
	public Flux<Product> getAllProducts() {
		return productService.getAllProducts();
	}

	@GetMapping("/{id}")
	public Mono<Product> getProductById(@PathVariable String id) {
		return productService.getProductById(id);
	}

	@PostMapping
	public Mono<Product> createProduct(@RequestBody Product product) {
		return productService.createProduct(product);
	}

	@PutMapping("/{id}")
	public Mono<Product> updateProduct(@PathVariable String id, @RequestBody Product product) {
		return productService.updateProduct(id, product);
	}

	@DeleteMapping("/{id}")
	public Mono<Void> deleteProduct(@PathVariable String id) {
		return productService.deleteProduct(id);
	}
}
