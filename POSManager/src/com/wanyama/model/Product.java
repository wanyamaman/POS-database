package com.wanyama.model;

/*
 * Class representing products on sale
 * @author William Opondo
 *
 */
public class Product {
	
	long id;			//
	int code;		// product code (can be replaced with name)
	int price;		// price of product
	
	// Constructors
	public Product(int code, int price) {
		this.code = code;
		this.price = price;
	}
	// should not be used (rather set id setter method when inserted into database)
	public Product(long id, int code, int price) {
		this.id =id;
		this.code = code;
		this.price = price;
	}
	
	// Setters
	public void setID (long id) {
		this.id = id;
	}
	
	public void setCode(int number) {
		code = number;
	}
	
	public void setPrice(int amount) {
		price = amount;
	}
	
	// Getters
	public long getId() {
		return id;
	}
	
	public int getCode() {
		return code;
	}
	
	public int getPrice() {
		return price;
	}
	
	public String toString(){
		return ("ID: "+ id+ "  Code: "+code+ " "+"Price: R"+price);
	}

}
