package test.mesirves.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

import com.mongodb.MongoClient;

@Configuration
@EnableMongoRepositories("com.mesirves.app.dao")
public class TestDBConfig {
 
	 @Bean
	 public MongoTemplate mongoTemplate() throws Exception {
		 return new MongoTemplate(new MongoClient("localhost"), "MeSirvesTest");
	 }
 
}

