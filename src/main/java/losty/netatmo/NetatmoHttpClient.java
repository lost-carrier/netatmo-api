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
import org.json.JSONObject;

import losty.netatmo.model.Measures;
import losty.netatmo.model.Module;
import losty.netatmo.model.Station;

public class NetatmoHttpClient {

	// API URLs that will be used for requests, see:
	// https://dev.netatmo.com/doc.
	protected final static String URL_BASE = "https://api.netatmo.net";
	protected final static String URL_REQUEST_TOKEN = URL_BASE + "/oauth2/token";
	protected final static String URL_GET_DEVICES_LIST = URL_BASE + "/api/devicelist";
	protected final static String URL_GET_MEASURES = URL_BASE + "/api/getmeasure";

	private final OAuthClient oAuthClient = new OAuthClient(new URLConnectionClient());

	private String clientId;
	private String clientSecret;

	public NetatmoHttpClient(String clientId, String clientSecret) {
		this.clientId = clientId;
		this.clientSecret = clientSecret;
	}

	/**
	 * This is the first request you have to do before being able to use the
	 * API. It allows you to retrieve an access token in one step, using your
	 * application's credentials and the user's credentials.
	 * 
	 * @param email
	 * @param password
	 * @throws OAuthSystemException
	 * @throws OAuthProblemException
	 */
	public OAuthJSONAccessTokenResponse login(String email, String password)
			throws OAuthSystemException, OAuthProblemException {

		OAuthClientRequest request = OAuthClientRequest.tokenLocation(URL_REQUEST_TOKEN)
				.setGrantType(GrantType.PASSWORD).setClientId(clientId).setClientSecret(clientSecret).setUsername(email)
				.setPassword(password).setScope("read_station").buildBodyMessage();

		return oAuthClient.accessToken(request);

	}

	/**
	 * Returns the list of devices owned by the user, and their modules. A
	 * device is identified by its _id (which is its mac address) and each
	 * device may have one, several or no modules, also identified by an _id.
	 * See
	 * <a href="https://dev.netatmo.com/doc/methods/devicelist">dev.netatmo.com/
	 * doc/methods/devicelist</a> for more information.
	 * 
	 * @param token The token obtained by the login function.
	 * @throws OAuthProblemException
	 * @throws OAuthSystemException
	 */
	public List<Station> getDevicesList(OAuthJSONAccessTokenResponse token)
			throws OAuthSystemException, OAuthProblemException {

		OAuthClientRequest bearerClientRequest = new OAuthBearerClientRequest(URL_GET_DEVICES_LIST)
				.setAccessToken(token.getAccessToken()).buildQueryMessage();

		OAuthResourceResponse resourceResponse = oAuthClient.resource(bearerClientRequest, OAuth.HttpMethod.GET,
				OAuthResourceResponse.class);

		return NetatmoUtils.parseDevicesList(new JSONObject(resourceResponse.getBody()));

	}

	/**
	 * Returns the list of measures for a device or module owned by the user.
	 * See
	 * <a href="https://dev.netatmo.com/doc/methods/devicelist">dev.netatmo.com/
	 * doc/methods/devicelist</a> for more information.
	 * 
	 * Some parameters are optional, they can be set to "null".
	 * 
	 * @param token The token obtained by the login function.
	 * @param station The station to query
	 * @param module The module of the station (optional)
	 * @param types A list of the types to query
	 * @param scales The scale to query
	 * @param dateBegin Start date of the interval to query (optional)
	 * @param dateEnd End date of the interval to query (optional)
	 * @param limit The amount of Measures to be returned at maximum (be careful - max. is 1024!)
	 * @param realTime Some fancy real_time stuff from Netatmo
	 * @return
	 * @throws OAuthSystemException
	 * @throws OAuthProblemException
	 */
	public List<Measures> getMeasures(OAuthJSONAccessTokenResponse token, Station station, Module module,
			List<String> types, String scale, Date dateBegin, Date dateEnd, Integer limit, Boolean realTime)
					throws OAuthSystemException, OAuthProblemException {

		String[] typesArr = types.toArray(new String[0]);

		List<String> params = new ArrayList<>();
		params.add("device_id=" + station.getId());
		params.add("scale=" + scale);
		params.add("type=" + implode(",", typesArr));
		if (module != null) {
			params.add("module_id=" + module.getId());
		}
		if (dateBegin != null) {
			params.add(String.format("date_begin=%d", dateBegin.getTime() / 1000));
		}
		if (dateEnd != null) {
			params.add(String.format("date_end=%d", dateEnd.getTime() / 1000));
		}
		if (limit != null) {
			params.add(String.format("limit=%d", limit));
		}
		if (realTime != null) {
			params.add(String.format("real_time=%b", realTime));
		}
		String query = implode("&", params.toArray(new String[0]));
		String request = URL_GET_MEASURES + "?" + query;
		OAuthClientRequest bearerClientRequest = new OAuthBearerClientRequest(request)
				.setAccessToken(token.getAccessToken()).buildQueryMessage();

		OAuthResourceResponse resourceResponse = oAuthClient.resource(bearerClientRequest, OAuth.HttpMethod.GET,
				OAuthResourceResponse.class);

		return NetatmoUtils.parseMeasures(new JSONObject(resourceResponse.getBody()), typesArr);

	}

	private static String implode(String separator, String... data) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < data.length - 1; i++) {
			sb.append(data[i]);
			sb.append(separator);
		}
		sb.append(data[data.length - 1].trim());
		return sb.toString();
	}

}
