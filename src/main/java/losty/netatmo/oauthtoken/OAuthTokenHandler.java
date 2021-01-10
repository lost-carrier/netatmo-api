package losty.netatmo.oauthtoken;

import losty.netatmo.exceptions.NetatmoNotLoggedInException;
import losty.netatmo.exceptions.NetatmoOAuthException;
import org.apache.oltu.oauth2.client.OAuthClient;
import org.apache.oltu.oauth2.client.URLConnectionClient;
import org.apache.oltu.oauth2.client.request.OAuthBearerClientRequest;
import org.apache.oltu.oauth2.client.request.OAuthClientRequest;
import org.apache.oltu.oauth2.client.response.OAuthJSONAccessTokenResponse;
import org.apache.oltu.oauth2.client.response.OAuthResourceResponse;
import org.apache.oltu.oauth2.common.OAuth;
import org.apache.oltu.oauth2.common.exception.OAuthProblemException;
import org.apache.oltu.oauth2.common.exception.OAuthSystemException;
import org.apache.oltu.oauth2.common.message.types.GrantType;

public class OAuthTokenHandler {

    private final String tokenUrl;
    private final String scope;
    private final String clientId;
    private final String clientSecret;
    private final OAuthTokenStore oauthTokenStore;
    private final OAuthClient oAuthClient = new OAuthClient(new URLConnectionClient());

    /**
     * To be described...
     *
     * @param tokenUrl
     * @param scope
     * @param clientId
     * @param clientSecret
     * @param oauthTokenStore
     */
    public OAuthTokenHandler(String tokenUrl, String scope, String clientId, String clientSecret, OAuthTokenStore oauthTokenStore) {
        this.tokenUrl = tokenUrl;
        this.scope = scope;
        this.clientId = clientId;
        this.clientSecret = clientSecret;
        this.oauthTokenStore = oauthTokenStore;
    }

    /**
     * This is the first request you have to do before being able to use the
     * API. It allows you to retrieve an access token in one step, using your
     * application's credentials and the user's credentials.
     *
     * @param email E-Mail
     * @param password Password
     * @throws NetatmoOAuthException When something goes wrong with OAuth.
     */
    public void login(final String email, final String password) throws NetatmoOAuthException {

        try {
            OAuthClientRequest request = OAuthClientRequest.tokenLocation(tokenUrl)
                    .setGrantType(GrantType.PASSWORD)
                    .setClientId(clientId)
                    .setClientSecret(clientSecret)
                    .setUsername(email)
                    .setPassword(password)
                    .setScope(scope)
                    .buildBodyMessage();

            OAuthJSONAccessTokenResponse token = oAuthClient.accessToken(request);
            long expiresAt = System.currentTimeMillis() + token.getExpiresIn() * 1000;
            oauthTokenStore.setTokens(token.getRefreshToken(), token.getAccessToken(), expiresAt);
        } catch (OAuthSystemException | OAuthProblemException e) {
            throw new NetatmoOAuthException(e);
        }
    }

    /**
     * To be described...
     *
     * @param request
     * @return The body of the response
     * @throws NetatmoOAuthException
     */
    public String executeRequest(String request) throws NetatmoOAuthException {

        verifyLoggedIn();
        verifyAccessToken();

        try {
            final OAuthClientRequest bearerClientRequest = createBearerClientRequest(request);
            final OAuthResourceResponse resourceResponse = oAuthClient.resource(bearerClientRequest, OAuth.HttpMethod.GET, OAuthResourceResponse.class);
            return resourceResponse.getBody();
        } catch (OAuthSystemException | OAuthProblemException e) {
            throw new NetatmoOAuthException(e);
        }
    }

    private OAuthClientRequest createBearerClientRequest(String request) throws OAuthSystemException {
        return new OAuthBearerClientRequest(request)
                .setAccessToken(oauthTokenStore.getAccessToken())
                .buildQueryMessage();
    }

    private void verifyLoggedIn() throws NetatmoNotLoggedInException {
        if (oauthTokenStore.getAccessToken() == null) {
            throw new NetatmoNotLoggedInException("Please use login() first!");
        }
    }

    private void verifyAccessToken() {
        if(isAccessTokenExpired()) {
            refreshToken();
        }
    }

    private void refreshToken() throws NetatmoNotLoggedInException, NetatmoOAuthException {

        verifyLoggedIn();

        try {
            OAuthClientRequest request = OAuthClientRequest.tokenLocation(tokenUrl)
                    .setGrantType(GrantType.REFRESH_TOKEN)
                    .setClientId(clientId)
                    .setClientSecret(clientSecret)
                    .setRefreshToken(oauthTokenStore.getRefreshToken())
                    .setScope(scope)
                    .buildBodyMessage();

            OAuthJSONAccessTokenResponse token = oAuthClient.accessToken(request);
            long expiresAt = System.currentTimeMillis() + token.getExpiresIn() * 1000;
            oauthTokenStore.setTokens(token.getRefreshToken(), token.getAccessToken(), expiresAt);
        } catch (OAuthSystemException | OAuthProblemException e) {
            throw new NetatmoOAuthException(e);
        }
    }

    private boolean isAccessTokenExpired() {
        return oauthTokenStore.getExpiresAt() < System.currentTimeMillis();
    }
}
