package com.mesirves.app.components.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mesirves.app.dao.TokenDAO;

@Service
public class TokenService {
	@Autowired
	private TokenDAO dao;

	public TokenDAO getDAO() {
		return dao;
	}
}
