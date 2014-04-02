package com.wanyama.model;

/*
 *Class representation of sale booths (points of sale) 
 * @author FUJITSU
 *
 */
public class Stall {
	
	int id;
	int stall_number;
	int balance;
	
	// constructor
	public Stall(int id, int number, int balance) {
		this.id = id;
		stall_number = number;
		this.balance = balance;
	}
	
	// Setters
	public void setId(int id){
		this.id = id;
	}
	public void setNumber(int number){
		stall_number = number;
	}
	public void setBalance(int amount){
		balance = amount;
	}
	
	// Getters
	public int getId(){
		return id;
	}
	public int getNumber(){
		return stall_number;
	}
	public int getBalance(){
		return balance;
	}
}
