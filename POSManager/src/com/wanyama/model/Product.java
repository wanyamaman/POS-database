package com.wanyama.model;

/*
 * Class representing products on sale
 * @author William Opondo
 *
 */
public class Product {
	
	int id;			//
	int code;		// product code (can be replaced with name)
	int price;		// price of product
	
	// Constructors
	public Product (){
	}
	
	public Product(int id, int code, int price) {
		this.id =id;
		this.code = code;
		this.price = price;
	}
	
	// Setters
	public void setID (int id) {
		this.id = id;
	}
	
	public void setCode(int number) {
		code = number;
	}
	
	public void setPrice(int amount) {
		price = amount;
	}
	
	// Getters
	public int getId() {
		return id;
	}
	
	public int getCode() {
		return code;
	}
	
	public int getPrice() {
		return price;
	}

}
