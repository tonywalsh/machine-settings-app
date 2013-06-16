package com.freefoam.productionreports;

// Visitor Interface
public interface Recorder {
	// BasicRecord is Abstract Class
	public void record(BasicRecord record);
	public void record(IntegerRecord record);
	public void record(StringRecord record);
	public void record(FloatRecord record);
	public void record(BooleanRecord record);
}
