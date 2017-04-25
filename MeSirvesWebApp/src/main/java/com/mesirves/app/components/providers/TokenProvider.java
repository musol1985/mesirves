package com.mesirves.app.components.providers;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.web.authentication.rememberme.PersistentRememberMeToken;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.springframework.stereotype.Component;

import com.mesirves.app.components.models.core.Token;
import com.mesirves.app.components.services.TokenService;

@Component
public class TokenProvider implements PersistentTokenRepository {
	
	@Autowired
	private TokenService tokens;

	@Override
	public void createNewToken(PersistentRememberMeToken token) {
		tokens.getDAO().save(Token.fromSpringToken(token));
	}

	@Override
	public void updateToken(String series, String tokenValue, Date lastUsed) {
		Token t=tokens.getDAO().findBySeries(series);
		if(t!=null){
			t.setTokenValue(tokenValue);
			t.setDate(lastUsed);
			tokens.getDAO().save(t);
		}
	}

	@Override
	public PersistentRememberMeToken getTokenForSeries(String seriesId) {
		Token t=tokens.getDAO().findBySeries(seriesId);
		if(t!=null)
			return t.toSpringToken();
		return null;
	}

	@Override
	public void removeUserTokens(String username) {
		Token t=tokens.getDAO().findOne(username);
		if(t!=null)
			tokens.getDAO().delete(t);
	}


}
