package com.freefoam.productionreports;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Note {
	private long recordId;
	private long checkpointId;
	private String content;
	private String timestamp;

	public Note() {
		this.timestamp = new SimpleDateFormat("HH:mm").format(new Date());	}
	
	public Note(long checkpointId, String content) {
		this();
		this.checkpointId = checkpointId;
		this.content = content;
	}
	
	// not calling super, this record will be created from a DB cursor
	public Note(long recordId, long checkpointId, String content, String created) {
		this(checkpointId,content );
		this.timestamp = created;
	}

	public String getContent() {
		return content;
	}

	public void setColour(String content) {
		this.content = content;
	}

	public long getRecordId() {
		return recordId;
	}

	public void setRecordId(long recordId) {
		this.recordId = recordId;
	}

	public long getCheckpointId() {
		return checkpointId;
	}

	public String getTimestamp() {
		return timestamp;
	}
	
	public String toString(){
		return "" + content + " - " + timestamp;
	}	
}
