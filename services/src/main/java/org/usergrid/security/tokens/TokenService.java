package org.usergrid.security.tokens;

import java.util.Map;

import org.usergrid.security.AuthPrincipalInfo;

public interface TokenService {

	public String createToken(TokenCategory tokenType, String type,
			Map<String, Object> state) throws Exception;

	public String createToken(AuthPrincipalInfo principal) throws Exception;

	public String createToken(AuthPrincipalInfo principal,
			Map<String, Object> state) throws Exception;

	public String createToken(TokenCategory tokenCategory, String type,
			AuthPrincipalInfo principal, Map<String, Object> state)
			throws Exception;

	public TokenInfo getTokenInfo(String token) throws Exception;

	public String refreshToken(String token) throws Exception;

	public long getMaxTokenAge(String token);

}
