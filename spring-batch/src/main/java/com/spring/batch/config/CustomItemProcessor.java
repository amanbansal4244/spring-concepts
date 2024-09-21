package com.spring.batch.config;

import org.springframework.batch.item.ItemProcessor;

import com.spring.batch.entity.Product;

public class CustomItemProcessor implements ItemProcessor<Product, Product> {

	/**
	 * Here, we can process our data and apply some business logic
	 */
	@Override
	public Product process(Product itemAsProduct) throws Exception {
		try {
			// Put the percentage logic
			System.out.println(itemAsProduct.getDescription());
			int discountPer = Integer.parseInt(itemAsProduct.getDiscount().trim());
			double originalPrice = Double.parseDouble(itemAsProduct.getPrice().trim());
			double discount = (discountPer / 100) * originalPrice;
			double finalPrice = originalPrice - discount;
			itemAsProduct.setDiscountedPrice(String.valueOf(finalPrice));
		} catch (NumberFormatException ex) {
			ex.printStackTrace();
		}

		return itemAsProduct;
	}
}
