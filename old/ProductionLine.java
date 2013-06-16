package com.freefoam.productionreports;

import java.util.ArrayList;

public class ProductionLine {
	private ArrayList<String> checkpoints;

	private String title;

	public ProductionLine(String title) {
		super();
		this.title = title;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}
	
}
