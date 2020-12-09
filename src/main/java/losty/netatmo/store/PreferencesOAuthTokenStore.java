package losty.netatmo.store;

import java.util.prefs.Preferences;

/**
 * {@link OAuthTokenStore} implementation which stores the OAuth token as Java Preferences.
 */
public class PreferencesOAuthTokenStore implements OAuthTokenStore {
    private static final String KEY_REFRESH_TOKEN = "KEY_REFRESH_TOKEN";
    private static final String KEY_ACCESS_TOKEN = "KEY_ACCESS_TOKEN";
    private static final String KEY_EXPIRES_AT = "KEY_EXPIRES_AT";
    
    private Preferences preferences;
    
    /**
     * @param preferencesPathName the path name of the preference node
     */
    public PreferencesOAuthTokenStore(String preferencesPathName) {
        preferences = Preferences.userRoot().node(preferencesPathName);
    }

    @Override
    public void setTokens(String refreshToken, String accessToken, long expiresAt) {
        preferences.put(KEY_REFRESH_TOKEN, refreshToken);
        preferences.put(KEY_ACCESS_TOKEN, accessToken);
        preferences.putLong(KEY_EXPIRES_AT, expiresAt);
    }

    @Override
    public String getRefreshToken() {
        return preferences.get(KEY_REFRESH_TOKEN, null);
    }

    @Override
    public String getAccessToken() {
        return preferences.get(KEY_ACCESS_TOKEN, null);
    }

    @Override
    public long getExpiresAt() {
        return preferences.getLong(KEY_EXPIRES_AT, 0);
    }
}
