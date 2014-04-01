package com.wanyama.model;

/*
 *Class representation of sale booths (points of sale) 
 * @author FUJITSU
 *
 */
public class Stall {
	
	int id;
	int stall_number;
	
	// constructor
	public Stall(int id, int number) {
		this.id = id;
		stall_number = number;
	}
	
	// Setters
	public void setId(int id){
		this.id = id;
	}
	public void setNumber(int number){
		stall_number = number;
	}
	
	// Getters
	public int getId(){
		return id;
	}
	public int getNumber(){
		return stall_number;
	}
}
