package losty.netatmo.oauthtoken;

import losty.netatmo.exceptions.NetatmoNotLoggedInException;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.apache.oltu.oauth2.client.OAuthClient;
import org.apache.oltu.oauth2.client.request.OAuthClientRequest;
import org.apache.oltu.oauth2.client.response.OAuthJSONAccessTokenResponse;
import org.apache.oltu.oauth2.client.response.OAuthResourceResponse;
import org.apache.oltu.oauth2.common.exception.OAuthProblemException;
import org.apache.oltu.oauth2.common.exception.OAuthSystemException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

public class OAuthTokenHandlerTest {

    private OAuthTokenHandler oAuthTokenHandler;
    private OAuthTokenStore oAuthTokenStore;
    private OAuthClient oAuthClient;


    @BeforeEach
    public void init() throws IllegalAccessException {
        oAuthTokenStore = new TransientOAuthTokenStore();
        oAuthTokenHandler = new OAuthTokenHandler("url", "scope", "client_id", "client_secret", oAuthTokenStore);
        oAuthClient = mock(OAuthClient.class);
        FieldUtils.writeField(oAuthTokenHandler, "oAuthClient", oAuthClient, true);
    }

    @Test
    public void testHeileWelt() throws OAuthProblemException, OAuthSystemException {
        OAuthResourceResponse resourceResponse = mock(OAuthResourceResponse.class);
        when(resourceResponse.getBody()).thenReturn("I am the body of the response!");
        when(oAuthClient.resource(any(OAuthClientRequest.class), eq("GET"), eq(OAuthResourceResponse.class))).thenReturn(resourceResponse);
        oAuthTokenStore.setTokens("refreshToken", "accessToken", System.currentTimeMillis() + 10000);

        String response = oAuthTokenHandler.executeRequest("someUrl");

        assertEquals("I am the body of the response!", response);
        verify(oAuthClient, never()).accessToken(any(OAuthClientRequest.class));
    }

    @Test
    public void testTokenRefresh() throws OAuthProblemException, OAuthSystemException {
        OAuthJSONAccessTokenResponse tokenResponse = mock(OAuthJSONAccessTokenResponse.class);
        when(tokenResponse.getRefreshToken()).thenReturn("refreshToken");
        when(tokenResponse.getAccessToken()).thenReturn("accessToken");
        when(tokenResponse.getExpiresIn()).thenReturn(42L);
        when(oAuthClient.accessToken(any(OAuthClientRequest.class))).thenReturn(tokenResponse);

        OAuthResourceResponse resourceResponse = mock(OAuthResourceResponse.class);
        when(resourceResponse.getBody()).thenReturn("I am the body of the response!");
        when(oAuthClient.resource(any(OAuthClientRequest.class), eq("GET"), eq(OAuthResourceResponse.class))).thenReturn(resourceResponse);

        oAuthTokenStore.setTokens("refreshToken", "accessToken", System.currentTimeMillis() - 10000);

        String response = oAuthTokenHandler.executeRequest("someUrl");

        assertEquals("I am the body of the response!", response);
        verify(oAuthClient, times(1)).accessToken(any(OAuthClientRequest.class));
    }
}