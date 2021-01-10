package losty.netatmo.oauthtoken;

import losty.netatmo.exceptions.NetatmoNotLoggedInException;
import org.junit.Test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class OAuthTokenHandlerTest {

    @Test(expected = NetatmoNotLoggedInException.class)
    public void notLoggedIn() throws Throwable {
        OAuthTokenStore oauthTokenStore = new TransientOAuthTokenStore();
        OAuthTokenHandler tokenHandler = new OAuthTokenHandler("url", "scope", "client_id", "client_secret", oauthTokenStore);

        Method verifyLoggedIn = OAuthTokenHandler.class.getDeclaredMethod("verifyLoggedIn");
        verifyLoggedIn.setAccessible(true);
        try {
            verifyLoggedIn.invoke(tokenHandler);
            fail();
        } catch (InvocationTargetException e) {
            throw e.getTargetException();
        }
    }

    @Test
    public void validAccessToken() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        OAuthTokenStore oauthTokenStore = new TransientOAuthTokenStore();
        oauthTokenStore.setTokens("refreshToken", "accessToken", System.currentTimeMillis() + 20000);
        OAuthTokenHandler tokenHandler = new OAuthTokenHandler("url", "scope", "client_id", "client_secret", oauthTokenStore);

        Method verifyAccessToken = OAuthTokenHandler.class.getDeclaredMethod("verifyAccessToken");
        verifyAccessToken.setAccessible(true);
        verifyAccessToken.invoke(tokenHandler);
    }

    @Test
    public void expiredAccessToken() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        OAuthTokenStore oauthTokenStore = new TransientOAuthTokenStore();
        oauthTokenStore.setTokens("refreshToken", "accessToken", System.currentTimeMillis() - 20000);
        OAuthTokenHandler tokenHandler = new OAuthTokenHandler("url", "scope", "client_id", "client_secret", oauthTokenStore);

        Method isAccessTokenExpired = OAuthTokenHandler.class.getDeclaredMethod("isAccessTokenExpired");
        isAccessTokenExpired.setAccessible(true);
        assertTrue((boolean) isAccessTokenExpired.invoke(tokenHandler));
    }
}