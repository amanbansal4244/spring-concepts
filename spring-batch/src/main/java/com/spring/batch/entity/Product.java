package com.spring.batch.entity;

import lombok.Data;

@Data
public class Product {
	private String productId;
	private String title;
	private String description;

	private String price;

	private String discount;

	private String discountedPrice;

	public Product(String productId, String title, String description, String price, String discount) {
		this.productId = productId;
		this.title = title;
		this.description = description;
		this.price = price;
		this.discount = discount;
	}

	public Product() {
	}

	
}
