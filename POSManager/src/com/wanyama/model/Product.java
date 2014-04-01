package com.wanyama.model;

/*
 * Class representing products on sale
 * @author William Opondo
 *
 */
public class Product {
	
	int id;			//
	int code;		// product code (can be replaced with name)
	
	// Constructors
	public Product (){
	}
	
	public Product(int id, int number) {
		this.id =id;
		code = number;
	}
	
	// Setters
	public void setID (int id) {
		this.id = id;
	}
	
	public void setCode(int number) {
		code = number;
	}
	
	// Getters
	public int getId() {
		return id;
	}
	
	public int getCode() {
		return code;
	}

}
