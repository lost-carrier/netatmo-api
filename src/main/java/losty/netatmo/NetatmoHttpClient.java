/*
 * Copyright 2013 Netatmo
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package losty.netatmo;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
import org.json.JSONException;
import org.json.JSONObject;

import losty.netatmo.model.Measures;
import losty.netatmo.model.Module;
import losty.netatmo.model.Station;

public class NetatmoHttpClient {

	// API URLs that will be used for requests, see:
	// https://dev.netatmo.com/doc.
	private final static String URL_BASE = "https://api.netatmo.net";
    private final static String URL_REQUEST_TOKEN = URL_BASE + "/oauth2/token";
    private final static String URL_GET_STATIONS_DATA = URL_BASE + "/api/getstationsdata";
    private final static String URL_GET_MEASURES = URL_BASE + "/api/getmeasure";

	private final OAuthClient oAuthClient = new OAuthClient(new URLConnectionClient());

	private String clientId;
	private String clientSecret;

	public NetatmoHttpClient(final String clientId, final String clientSecret) {
		this.clientId = clientId;
		this.clientSecret = clientSecret;
	}

	/**
	 * This is the first request you have to do before being able to use the
	 * API. It allows you to retrieve an access token in one step, using your
	 * application's credentials and the user's credentials.
	 * 
	 * @param email E-Mail
	 * @param password Password
	 * @return The Access Token.
	 * @throws OAuthSystemException When something goes wrong with OAuth.
	 * @throws OAuthProblemException When something goes wrong with OAuth.
	 */
	public OAuthJSONAccessTokenResponse login(final String email, final String password)
			throws OAuthSystemException, OAuthProblemException {

		OAuthClientRequest request = OAuthClientRequest.tokenLocation(URL_REQUEST_TOKEN)
				.setGrantType(GrantType.PASSWORD)
				.setClientId(clientId)
				.setClientSecret(clientSecret)
				.setUsername(email)
				.setPassword(password)
				.setScope("read_station")
				.buildBodyMessage();

		return oAuthClient.accessToken(request);

	}

	/**
	 * Retrieve an refreshed or renewed access token, using your
	 * refresh token and the user's credentials.
	 *
	 * @param token The token obtained by the login function.
	 * @param email E-Mail
	 * @param password Password
	 * @throws OAuthSystemException When something goes wrong with OAuth.
	 * @throws OAuthProblemException When something goes wrong with OAuth.
	 */
	public OAuthJSONAccessTokenResponse refreshToken(final OAuthJSONAccessTokenResponse token, final String email, final String password) throws OAuthSystemException, OAuthProblemException {
		OAuthClientRequest request = OAuthClientRequest.tokenLocation(URL_REQUEST_TOKEN)
				.setGrantType(GrantType.PASSWORD)
				.setClientId(clientId)
				.setClientSecret(clientSecret)
				.setRefreshToken(token.getRefreshToken())
				.setUsername(email)
				.setPassword(password)
				.setScope("read_station")
				.buildBodyMessage();

		return oAuthClient.accessToken(request);

	}

	/**
	 * Returns the list of stations owned by the user, and their modules. A
	 * device is identified by its _id (which is its mac address) and each
	 * device may have one, several or no modules, also identified by an _id.
	 * See <a href=
	 * "https://dev.netatmo.com/en-US/resources/technical/reference/weatherstation/getstationsdata">
	 * dev.netatmo.com/en-US/resources/technical/reference/weatherstation/getstationsdata</a>
	 * for more information.
	 * 
	 * @param token The token obtained by the login function.
	 * @return The found Stations.
	 * @throws OAuthSystemException When something goes wrong with OAuth.
	 * @throws OAuthProblemException When something goes wrong with OAuth.
	 * @throws JSONException If paring goes wrong.
	 */
	public List<Station> getStationsData(final OAuthJSONAccessTokenResponse token)
			throws OAuthSystemException, OAuthProblemException, JSONException {

		OAuthClientRequest bearerClientRequest = new OAuthBearerClientRequest(URL_GET_STATIONS_DATA)
				.setAccessToken(token.getAccessToken())
				.buildQueryMessage();

		OAuthResourceResponse resourceResponse = oAuthClient.resource(bearerClientRequest, OAuth.HttpMethod.GET,
				OAuthResourceResponse.class);

		return NetatmoUtils.parseStationsData(new JSONObject(resourceResponse.getBody()));

	}

	/**
	 * Returns the list of measures for a device or module owned by the user.
	 * See
	 * <a href="https://dev.netatmo.com/en-US/resources/technical/reference/common/getmeasure">
	 * dev.netatmo.com/en-US/resources/technical/reference/common/getmeasure</a> for more information.
	 * 
	 * Some parameters are optional, they can be set to "null".
	 * 
	 * @param token The token obtained by the login function.
	 * @param station The station to query
	 * @param module The module of the station (optional)
	 * @param types A list of the types to query
	 * @param scale The scale to query
	 * @param dateBegin Start date of the interval to query (optional)
	 * @param dateEnd End date of the interval to query (optional)
	 * @param limit The amount of Measures to be returned at maximum (be careful - max. is 1024!)
	 * @param realTime Some fancy real_time stuff from Netatmo
	 * @return The requested Measures from Netatmo.
	 * @throws OAuthSystemException When something goes wrong with OAuth.
	 * @throws OAuthProblemException When something goes wrong with OAuth.
	 */
	public List<Measures> getMeasures(final OAuthJSONAccessTokenResponse token, final Station station, final Module module,
			final List<String> types, final String scale, final Date dateBegin, final Date dateEnd, final Integer limit, final Boolean realTime)
					throws OAuthSystemException, OAuthProblemException {

		Long dateBeginMillis = null;
		if (dateBegin != null) {
			dateBeginMillis = (dateBegin.getTime() / 1000);
		}
		
		Long dateEndMillis = null;
		if (dateEnd != null) {
			dateEndMillis = (dateEnd.getTime() / 1000);
		}

		return getMeasures(token, station, module, types, scale, dateBeginMillis, dateEndMillis, limit, realTime);

	}

	/**
	 * Returns the list of measures for a device or module owned by the user.
	 * See
	 * <a href="https://dev.netatmo.com/en-US/resources/technical/reference/common/getmeasure">
	 * dev.netatmo.com/en-US/resources/technical/reference/common/getmeasure</a> for more information.
	 * 
	 * Some parameters are optional, they can be set to "null".
	 * 
	 * @param token The token obtained by the login function.
	 * @param station The station to query
	 * @param module The module of the station (optional)
	 * @param types A list of the types to query
	 * @param scale The scale to query
	 * @param dateBegin Start date of the interval to query (optional)
	 * @param dateEnd End date of the interval to query (optional)
	 * @param limit The amount of Measures to be returned at maximum (be careful - max. is 1024!)
	 * @param realTime Some fancy real_time stuff from Netatmo
	 * @return
	 * @throws OAuthSystemException
	 * @throws OAuthProblemException
	 * @throws JSONException
	 */
	public List<Measures> getMeasures(final OAuthJSONAccessTokenResponse token, final Station station, final Module module,
			final List<String> types, final String scale, final Long dateBegin, final Long dateEnd, final Integer limit, final Boolean realTime)
					throws OAuthSystemException, OAuthProblemException, JSONException {

		final String[] typesArr;
		if (types != null) {
			typesArr = types.toArray(new String[0]);
		} else {
			typesArr = new String[0];
		}

		final List<String> params = new ArrayList<>();
		params.add("device_id=" + station.getId());
		params.add("scale=" + scale);
		params.add("type=" + implode(",", typesArr));
		if (module != null) {
			params.add("module_id=" + module.getId());
		}
		if (dateBegin != null) {
			params.add(String.format("date_begin=%d", dateBegin));
		}
		if (dateEnd != null) {
			params.add(String.format("date_end=%d", dateEnd));
		}
		if (limit != null) {
			params.add(String.format("limit=%d", limit));
		}
		if (realTime != null) {
			params.add(String.format("real_time=%b", realTime));
		}
		final String query = implode("&", params.toArray(new String[0]));
		final String request = URL_GET_MEASURES + "?" + query;
		final OAuthClientRequest bearerClientRequest = new OAuthBearerClientRequest(request).setAccessToken(token.getAccessToken()).buildQueryMessage();
		final OAuthResourceResponse resourceResponse = oAuthClient.resource(bearerClientRequest, OAuth.HttpMethod.GET, OAuthResourceResponse.class);
		return NetatmoUtils.parseMeasures(new JSONObject(resourceResponse.getBody()), typesArr);
	}

	private static String implode(final String separator, final String... data) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < data.length - 1; i++) {
			sb.append(data[i]);
			sb.append(separator);
		}
		sb.append(data[data.length - 1].trim());
		return sb.toString();
	}
}
