package losty.netatmo.oauthtoken;

import losty.netatmo.exceptions.NetatmoNotLoggedInException;
import org.junit.Test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import static org.junit.Assert.assertTrue;

public class OAuthTokenHandlerTest {

    @Test(expected = NetatmoNotLoggedInException.class)
    public void notLoggedIn() {
        OAuthTokenStore oauthTokenStore = new TransientOAuthTokenStore();
        OAuthTokenHandler tokenHandler = new OAuthTokenHandler("url", "scope", "client_id", "client_secret", oauthTokenStore);
        tokenHandler.verifyLoggedIn();
    }

    @Test
    public void validAccessToken() {
        OAuthTokenStore oauthTokenStore = new TransientOAuthTokenStore();
        oauthTokenStore.setTokens("refreshToken", "accessToken", System.currentTimeMillis() + 20000);
        OAuthTokenHandler tokenHandler = new OAuthTokenHandler("url", "scope", "client_id", "client_secret", oauthTokenStore);
        tokenHandler.verifyAccessToken();
    }

    @Test
    public void expiredAccessToken() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        OAuthTokenStore oauthTokenStore = new TransientOAuthTokenStore();
        oauthTokenStore.setTokens("refreshToken", "accessToken", System.currentTimeMillis() - 20000);
        OAuthTokenHandler tokenHandler = new OAuthTokenHandler("url", "scope", "client_id", "client_secret", oauthTokenStore);

        Method privateStringMethod = OAuthTokenHandler.class.getDeclaredMethod("isAccessTokenExpired");
        privateStringMethod.setAccessible(true);
        assertTrue((boolean) privateStringMethod.invoke(tokenHandler));
    }
}