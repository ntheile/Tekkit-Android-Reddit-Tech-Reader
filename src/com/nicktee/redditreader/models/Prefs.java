package com.nicktee.redditreader.models;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName="Prefs")
public class Prefs {
	
	public Prefs(){
		super();
	}

	@DatabaseField(id = true)
	String subReddits;

	@DatabaseField(canBeNull = false)
	Boolean selected;
	
	public Boolean getSelected() {
		return selected;
	}

	public void setSelected(Boolean selected) {
		this.selected = selected;
	}

	public String getSubReddits() {
		return subReddits;
	}

	public void setSubReddits(String subReddits) {
		this.subReddits = subReddits;
	}
	
}
