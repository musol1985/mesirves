package com.mesirves.app.components.models.core;

import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.security.web.authentication.rememberme.PersistentRememberMeToken;

@Document
public class Token {
	@Id
	private String userName;
	@Indexed
	private String series;
	private String tokenValue;
	private Date date;
	
	
	
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getSeries() {
		return series;
	}
	public void setSeries(String series) {
		this.series = series;
	}
	public String getTokenValue() {
		return tokenValue;
	}
	public void setTokenValue(String tokenValue) {
		this.tokenValue = tokenValue;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	
	public PersistentRememberMeToken toSpringToken(){
		return new PersistentRememberMeToken(userName, series, tokenValue, date);
	}
	
	public static Token fromSpringToken(PersistentRememberMeToken t){
		Token token=new Token();
		token.setUserName(t.getUsername());
		token.setSeries(t.getSeries());
		token.setTokenValue(t.getTokenValue());
		token.setDate(t.getDate());
		return token;
	}
}
