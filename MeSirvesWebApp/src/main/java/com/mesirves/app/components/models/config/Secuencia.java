package com.mesirves.app.components.models.config;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class Secuencia {
	@Id
	private IdCentroPK id;

	private long seq;

	public IdCentroPK getId() {
		return id;
	}

	public void setId(IdCentroPK id) {
		this.id = id;
	}

	public long getSeq() {
		return seq;
	}

	public void setSeq(long seq) {
		this.seq = seq;
	}
	
	
	
}
