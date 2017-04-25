package com.mesirves.app.json;

import java.util.List;

public class ListJSON<T> extends ResponseJSON {

	public ListJSON(int codigo) {
		super(codigo);
	}
	
	public ListJSON(int codigo, List<T> list) {
		super(codigo, list);
	}
}
