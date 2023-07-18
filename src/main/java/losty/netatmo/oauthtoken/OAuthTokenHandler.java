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


/**
 * This class handles all stuff around the OAuth authentication including signing the requests.
 */
public class OAuthTokenHandler {

    private final String tokenUrl;
    private final String scope;
    private final String clientId;
    private final String clientSecret;
    private final OAuthTokenStore oauthTokenStore;
    private final OAuthClient oAuthClient = new OAuthClient(new URLConnectionClient());

    /**
     *
     * @param tokenUrl The URL where to get the token. Should be always https://api.netatmo.net/oauth2/token, but one can never be sure...
     * @param scope The requested scope for the token.
     * @param clientId This apps clientId.
     * @param clientSecret This apps clientSecret.
     * @param oauthTokenStore Some method to store the retrieved tokens so that they can be reused.
     */
    public OAuthTokenHandler(String tokenUrl, String scope, String clientId, String clientSecret, OAuthTokenStore oauthTokenStore) {
        this.tokenUrl = tokenUrl;
        this.scope = scope;
        this.clientId = clientId;
        this.clientSecret = clientSecret;
        this.oauthTokenStore = oauthTokenStore;
    }

    /**
     * Executes a certain GET request to a OAuth protected URL.
     *
     * @param request The URL to GET.
     * @return The body of the response
     * @throws NetatmoOAuthException if something goes wrong - check Exception message for details.
     */
    public String executeRequest(String request) throws NetatmoOAuthException {

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

    private void verifyAccessToken() {
        if(isAccessTokenExpired()) {
            refreshToken();
        }
    }

    private boolean isAccessTokenExpired() {
        return oauthTokenStore.getExpiresAt() < System.currentTimeMillis();
    }

    private void refreshToken() throws NetatmoNotLoggedInException, NetatmoOAuthException {

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
}
