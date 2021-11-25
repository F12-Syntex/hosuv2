package com.hosu.settings;

public class Margin {

	public double marginA;
	public double marginB;

	public Margin(double marginA, double marginB) {
		super();
		this.marginA = marginA;
		this.marginB = marginB;
	}
	
	public double getMarginA() {
		return marginA;
	}
	public void setMarginA(double marginA) {
		this.marginA = marginA;
	}
	public double getMarginB() {
		return marginB;
	}
	public void setMarginB(double marginB) {
		this.marginB = marginB;
	}
	
	public double sum() {
		return this.marginA + this.marginB;
	}
	
	public double getRatioA() {
		return this.marginA / this.sum();
	}
	
	public double getRatioB() {
		return this.marginA / this.sum();
	}
	
}
