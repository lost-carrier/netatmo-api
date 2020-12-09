package losty.netatmo.oauthtoken;

/**
 * Holds the OAuth access token. May persist the token additionally.
 */
public interface OAuthTokenStore {

    /**
     * Sets the tokens and expiration parameter from the OAuth request.
     * 
     * @param refreshToken the refreshToken
     * @param accessToken the accessToken
     * @param expiresAt the calculated expiration time
     */
    void setTokens(String refreshToken, String accessToken, long expiresAt);
    
    /**
     * @return the refresh token
     */
    String getRefreshToken();
    
    /**
     * @return the access token
     */
    String getAccessToken();
    
    /**
     * @return the expiration time
     */
    long getExpiresAt();
}
