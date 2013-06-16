package com.freefoam.productionreports;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class Report {
	private long recordId;
	private String line;
	private String product;
	private String colour;
	private String creationDate;
	private Boolean isOpen;
	private String creator;
	private ArrayList<Checkpoint> checkpoints;
	private ArrayList<String> checkpointTimestamps;

	public Report() {
		this.creationDate = new SimpleDateFormat("dd/MM/yyyy").format(new Date());
		this.isOpen = true;
		this.creator = "unknown";
		this.product = "not set";
		this.checkpoints = new ArrayList<Checkpoint>();
		this.checkpointTimestamps = new ArrayList<String>();
	}
	
	public Report(String line, String product, String creator) {
		this();
		this.line = line;
		this.product = product;
		this.creator = creator;
	}
	
	// not calling super, this record will be created from a DB cursor
	public Report(long recordId, String line, String product, String colour,
			String creationDate, Boolean isOpen, String creator) {
		this(line,product,colour);
		this.recordId = recordId;
		this.creationDate = creationDate;
		this.isOpen = isOpen;
		this.creator = creator;
	}

	public String getProduct() {
		return product;
	}

	public void setProduct(String product) {
		this.product = product;
	}

	public long getRecordId() {
		return recordId;
	}

	public void setRecordId(long recordId) {
		this.recordId = recordId;
	}
	
	public String getLine() {
		return line;
	}

	public void setLine(String line) {
		this.line = line;
	}

	public String getColour() {
		return colour;
	}

	public void setColour(String colour) {
		this.colour = colour;
	}

	public String getCreationDate() {
		return creationDate;
	}

	public Boolean getIsOpen() {
		return isOpen;
	}

	public String getCreator() {
		return creator;
	}

	public String created() {
		return creationDate;
	}
	
	public Boolean isOpen() {
		return isOpen;
	}

	public void close() {
		this.isOpen = false;
	}
	
	public void setCheckpoints(ArrayList<Checkpoint> checkpoints) {
		this.checkpoints = checkpoints;
	}
	
	public ArrayList<Checkpoint> getCheckpoints() {
		return this.checkpoints;
	}
	
	public void setCheckpointTimestamps(ArrayList<String> cts) {
		this.checkpointTimestamps = cts;
	}

	public ArrayList<String> getCheckpointTimestamps() {
		return this.checkpointTimestamps;
	}

	public void addCheckpointTimestamp(String ts) {
		this.checkpointTimestamps.add(ts);
	}

	public void addCheckpoint(Checkpoint cp) {
		this.checkpoints.add(cp);
	}
	
	public String toString(){
		return "" + line + " - " + product + " - " + colour;
	}	
}
