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

import losty.netatmo.exceptions.NetatmoNotLoggedInException;
import losty.netatmo.exceptions.NetatmoOAuthException;
import losty.netatmo.exceptions.NetatmoParseException;
import losty.netatmo.model.Home;
import losty.netatmo.model.Measures;
import losty.netatmo.model.Module;
import losty.netatmo.model.Station;
import losty.netatmo.oauthtoken.OAuthTokenHandler;
import losty.netatmo.oauthtoken.OAuthTokenStore;
import losty.netatmo.oauthtoken.TransientOAuthTokenStore;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class NetatmoHttpClient {

    // API URLs that will be used for requests, see:
    // https://dev.netatmo.com/doc.
    private static final String URL_BASE = "https://api.netatmo.net";
    private static final String URL_REQUEST_TOKEN = URL_BASE + "/oauth2/token";
    private static final String URL_GET_STATIONS_DATA = URL_BASE + "/api/getstationsdata";
    private static final String URL_GET_MEASURES = URL_BASE + "/api/getmeasure";
    private static final String URL_GET_PUBLIC_DATA = URL_BASE + "/api/getpublicdata";
    private static final String URL_GET_HOMESDATA = URL_BASE + "/api/homesdata";
    private static final String URL_GET_HOMESTATUS = URL_BASE + "/api/homestatus";

    private static final String SCOPE = "read_station read_thermostat";

    private final OAuthTokenHandler oAuthTokenHandler;

    public NetatmoHttpClient(final String clientId, final String clientSecret) {
        this(clientId, clientSecret, new TransientOAuthTokenStore());
    }

    public NetatmoHttpClient(final String clientId, final String clientSecret, final OAuthTokenStore oauthTokenStore) {
        this.oAuthTokenHandler = new OAuthTokenHandler(URL_REQUEST_TOKEN, SCOPE, clientId, clientSecret, oauthTokenStore);
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
        oAuthTokenHandler.login(email, password);
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
     * @param station The station to query (optional)
     * @param getFavorites Whether to fetch favorites, too (optional)
     * @return The found Stations.
     * @throws NetatmoNotLoggedInException If not logged in.
     * @throws NetatmoOAuthException When something goes wrong with OAuth.
     * @throws NetatmoParseException If parsing goes wrong.
     */
    public List<Station> getStationsData(final Station station, final Boolean getFavorites)
            throws NetatmoNotLoggedInException, NetatmoOAuthException, NetatmoParseException {

        final List<String> params = new ArrayList<>();

        if (station != null) {
            params.add("device_id=" + station.getId());
        }

        if (getFavorites != null) {
            params.add("get_favorites=" + getFavorites);
        }

        final String query = implode("&", params.toArray(new String[0]));
        final String request = URL_GET_STATIONS_DATA + "?" + query;
        try {
            final String responseBody = oAuthTokenHandler.executeRequest(request);
            return NetatmoParseUtils.parseStationsData(new JSONObject(responseBody));
        } catch (JSONException e) {
            throw new NetatmoParseException(e);
        }
    }

    /**
     * Returns the list of measures for a device or module owned by the user.
     * See
     * <a href="https://dev.netatmo.com/en-US/resources/technical/reference/common/getmeasure">
     * dev.netatmo.com/en-US/resources/technical/reference/common/getmeasure</a> for more information.
     *
     * Some parameters are optional, they can be set to "null".
     *
     * @param station The station to query
     * @param module The module of the station (optional)
     * @param types A list of the types to query
     * @param scale The scale to query
     * @param dateBegin Start date of the interval to query (optional)
     * @param dateEnd End date of the interval to query (optional)
     * @param limit The amount of Measures to be returned at maximum (be careful - max. is 1024!)
     * @param realTime Some fancy real_time stuff from Netatmo
     * @return The requested Measures from Netatmo.
     * @throws NetatmoNotLoggedInException If not logged in.
     * @throws NetatmoOAuthException When something goes wrong with OAuth.
     * @throws NetatmoParseException If parsing goes wrong.
     */
    public List<Measures> getMeasures(final Station station, final Module module,
                                      final List<String> types, final String scale, final Date dateBegin, final Date dateEnd, final Integer limit, final Boolean realTime)
            throws NetatmoNotLoggedInException, NetatmoOAuthException, NetatmoParseException {

        Long dateBeginMillis = null;
        if (dateBegin != null) {
            dateBeginMillis = (dateBegin.getTime() / 1000);
        }

        Long dateEndMillis = null;
        if (dateEnd != null) {
            dateEndMillis = (dateEnd.getTime() / 1000);
        }

        return getMeasures(station, module, types, scale, dateBeginMillis, dateEndMillis, limit, realTime);
    }

    /**
     * Returns the list of measures for a device or module owned by the user.
     * See
     * <a href="https://dev.netatmo.com/en-US/resources/technical/reference/common/getmeasure">
     * dev.netatmo.com/en-US/resources/technical/reference/common/getmeasure</a> for more information.
     *
     * Some parameters are optional, they can be set to "null".
     *
     * @param station The station to query
     * @param module The module of the station (optional)
     * @param types A list of the types to query
     * @param scale The scale to query
     * @param dateBegin Start date of the interval to query (optional)
     * @param dateEnd End date of the interval to query (optional)
     * @param limit The amount of Measures to be returned at maximum (be careful - max. is 1024!)
     * @param realTime Some fancy real_time stuff from Netatmo
     * @return The requested Measures from Netatmo.
     * @throws NetatmoNotLoggedInException If not logged in.
     * @throws NetatmoOAuthException When something goes wrong with OAuth.
     * @throws NetatmoParseException If parsing goes wrong.
     */
    public List<Measures> getMeasures(final Station station, final Module module,
                                      final List<String> types, final String scale, final Long dateBegin, final Long dateEnd, final Integer limit, final Boolean realTime)
            throws NetatmoNotLoggedInException, NetatmoOAuthException, NetatmoParseException {

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

        try {
            final String responseBody = oAuthTokenHandler.executeRequest(request);
            return NetatmoParseUtils.parseMeasures(new JSONObject(responseBody), typesArr);
        } catch (JSONException e) {
            throw new NetatmoParseException(e);
        }
    }

    /**
     * Retrieves publicly shared weather data from Outdoor Modules within a predefined area.
     * See <a href=
     * "https://dev.netatmo.com/en-US/resources/technical/reference/weatherapi/getpublicdata">
     * dev.netatmo.com/en-US/resources/technical/reference/weatherapi/getpublicdata</a>
     * for more information.
     *
     * @param lat_ne Latitude of the north east corner of the requested area. -85.0 to +85.0 and (with lat_ne greater than lat_sw)
     * @param lon_ne Longitude of the north east corner of the requested area. -180.0 to +180.0 (with lon_ne greater than lon_sw)
     * @param lat_sw Latitude of the south west corner of the requested area. -85.0 to +85.0
     * @param lon_sw Longitude of the south west corner of the requested area. -180 to +180.0
     * @param types To filter stations based on relevant measurements you want (e.g. rain will only return stations with rain gauges). Default is no filter. You can find all measurements available on the Thermostat page.
     * @param filter True to exclude station with abnormal temperature measures. Default is false.
     * @return The requested weather data.
     * @throws NetatmoNotLoggedInException If not logged in.
     * @throws NetatmoOAuthException When something goes wrong with OAuth.
     * @throws NetatmoParseException If parsing goes wrong.
     */
    public List<Map.Entry<Station, Measures>> getPublicData(double lat_ne, double lon_ne, double lat_sw, double lon_sw,
                                                            final List<String> types, final Boolean filter)
            throws NetatmoNotLoggedInException, NetatmoOAuthException, NetatmoParseException {

        final List<String> params = new ArrayList<>();
        params.add("lat_ne=" + lat_ne);
        params.add("lon_ne=" + lon_ne);
        params.add("lat_sw=" + lat_sw);
        params.add("lon_sw=" + lon_sw);
        if (types != null) {
            params.add("required_data=" + implode(",", types.toArray(new String[0])));
        }
        if (filter != null) {
            params.add("filter=" + filter);
        }
        final String query = implode("&", params.toArray(new String[0]));
        final String request = URL_GET_PUBLIC_DATA + "?" + query;

        try {
            final String responseBody = oAuthTokenHandler.executeRequest(request);
            return NetatmoParseUtils.parsePublicData(new JSONObject(responseBody));
        } catch (JSONException e) {
            throw new NetatmoParseException(e);
        }
    }

    private static String implode(final String separator, final String... data) {

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < data.length - 1; i++) {
            sb.append(data[i]);
            sb.append(separator);
        }

        if (data.length > 0) {
            sb.append(data[data.length - 1].trim());
        }

        return sb.toString();
    }

    /**
     * Retrieve user's homes and their topology.
     * See <a href=
     * "https://dev.netatmo.com/en-US/resources/technical/reference/energy/homesdata">
     * dev.netatmo.com/en-US/resources/technical/reference/energy/homesdata</a>
     * for more information.
     *
     * @return The requested home data.
     * @throws NetatmoNotLoggedInException If not logged in.
     * @throws NetatmoOAuthException When something goes wrong with OAuth.
     * @throws NetatmoParseException If parsing goes wrong.
     */
    public List<Home> getHomesdata() throws NetatmoNotLoggedInException, NetatmoOAuthException, NetatmoParseException {

        final String request = URL_GET_HOMESDATA;

        try {
            final String responseBody = oAuthTokenHandler.executeRequest(request);
            return NetatmoParseUtils.parseHomesdata(new JSONObject(responseBody));
        } catch (JSONException e) {
            throw new NetatmoParseException(e);
        }
    }

    /**
     * Get the current status and data measured for all home devices.
     * See <a href=
     * "https://dev.netatmo.com/en-US/resources/technical/reference/energy/homestatus">
     * dev.netatmo.com/en-US/resources/technical/reference/energy/homestatus</a>
     * for more information.
     *
     * @param home The home to query
     * @return The requested home status.
     * @throws NetatmoNotLoggedInException If not logged in.
     * @throws NetatmoOAuthException When something goes wrong with OAuth.
     * @throws NetatmoParseException If parsing goes wrong.
     */
    public Home getHomestatus(final Home home) throws NetatmoNotLoggedInException, NetatmoOAuthException, NetatmoParseException {

        final List<String> params = new ArrayList<>();

        if (home != null) {
            params.add("home_id=" + home.getId());
        }

        final String query = implode("&", params.toArray(new String[0]));
        final String request = URL_GET_HOMESTATUS + "?" + query;
        try {
            final String responseBody = oAuthTokenHandler.executeRequest(request);
            final Home result = NetatmoParseUtils.parseHomestatus(new JSONObject(responseBody));
            result.setName(home != null ? home.getName() : null);
            return result;
        } catch (JSONException e) {
            throw new NetatmoParseException(e);
        }
    }
}
