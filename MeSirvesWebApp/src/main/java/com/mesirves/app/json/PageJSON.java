package com.mesirves.app.json;

import java.util.List;

public class PageJSON<T>{
	public long total;
	public long paginas;
	public List<T> data;
	

	public PageJSON(long total, List<T> data) {
		this.total = total;
		this.data = data;
	}
	public PageJSON(long total, long paginas, List<T> data) {
		this.total = total;
		this.data = data;
		this.paginas=paginas;
	}
	public long getTotal() {
		return total;
	}
	public void setTotal(long total) {
		this.total = total;
	}
	public List<T> getData() {
		return data;
	}
	public void setData(List<T> data) {
		this.data = data;
	}
	public long getPaginas() {
		return paginas;
	}
	public void setPaginas(long paginas) {
		this.paginas = paginas;
	}

	
}
