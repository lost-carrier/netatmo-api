package losty.netatmo.store;

/**
 * Holds the OAuth access and refresh token and the expiration time.
 */
public class TransientTokenStore implements TokenStore {

    private String refreshToken;
    private String accessToken;
    private long expiresAt;

    @Override
    public void setTokens(String refreshToken, String accessToken, long expiresAt) {
        this.refreshToken = refreshToken;
        this.accessToken = accessToken;
        this.expiresAt = expiresAt;
    }

    @Override
    public String getRefreshToken() {
        return refreshToken;
    }

    @Override
    public String getAccessToken() {
        return accessToken;
    }

    @Override
    public long getExpiresAt() {
        return expiresAt;
    }
}
