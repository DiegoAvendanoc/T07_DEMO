package pe.edu.vallegrande.vg_ms_resource.repository;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import pe.edu.vallegrande.vg_ms_resource.model.Product;

public interface ProductRepository extends ReactiveMongoRepository<Product, String> {
}
