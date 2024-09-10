package pe.edu.vallegrande.vg_ms_resource.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "products")
@Data
public class Product {
	@Id
	private String id;
	private String name;
	private String description;
	private double price;
	private int quantity;
}
