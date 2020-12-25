package com.example.shopping.model.data;

import lombok.Data;

@Data
public class Cart {
	private int id;
	private String name;
	private String price;
	private int quantity;
	private String image;
	
	public Cart(int id,String name,String price,int quantity,String image) {
		this.setId(id);
		this.setName(name);
		this.setPrice(price);
		this.setQuantity(quantity);
		this.setImage(image);
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}
/*
 * <div class="overlay text-center">
         <h4 class="display-4">You are going to paypal</h4>
         <img src="images/ajax-loader.gif" alt="">
      </div>
 */
}
