package com.mesirves.app.dao;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.mesirves.app.components.models.core.Token;

@Repository
public interface TokenDAO extends MongoRepository<Token, String>{
	public Token findBySeries(String series);
}
