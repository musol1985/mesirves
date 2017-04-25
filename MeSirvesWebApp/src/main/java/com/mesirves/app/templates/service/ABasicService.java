package com.mesirves.app.templates.service;

import org.springframework.beans.factory.annotation.Autowired;

import com.mesirves.app.templates.dao.IBasicIdDAO;

public class ABasicService<T extends IBasicIdDAO>{
	@Autowired
	protected T dao;
	

	public T getDAO(){
		return dao;
	}
}
