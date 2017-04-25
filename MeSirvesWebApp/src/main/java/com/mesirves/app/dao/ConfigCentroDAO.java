package com.mesirves.app.dao;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.mesirves.app.components.models.config.ConfigCentro;
import com.mesirves.app.components.models.config.IdCentroPK;

@Repository
public interface ConfigCentroDAO extends MongoRepository<ConfigCentro, IdCentroPK>{

}
