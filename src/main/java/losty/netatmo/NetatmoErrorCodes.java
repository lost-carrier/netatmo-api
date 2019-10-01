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

/**
 * When a request fails, the server will return
 * a JSONObject response with one of the number listed here.
 * NB: no error code is returned when doing a '/oauth/*' request.
 */
public class NetatmoErrorCodes {

    public NetatmoErrorCodes() {
        throw new IllegalStateException("Utility class");
    }

    public static final int ACCESS_TOKEN_MISSING = 1;
    public static final int INVALID_ACCESS_TOKEN = 2;
    public static final int ACCESS_TOKEN_EXPIRED = 3;
    public static final int INCONSISTENCY_ERROR = 4;
    public static final int APPLICATION_DEACTIVATED = 5;
    public static final int INVALID_EMAIL = 6;
    public static final int NOTHING_TO_MODIFY = 7;
    public static final int EMAIL_ALREADY_EXISTS = 8;
    public static final int DEVICE_NOT_FOUND = 9;
    public static final int MISSING_ARGS = 10;
    public static final int INTERNAL_ERROR = 11;
    public static final int DEVICE_OR_SECRET_NO_MATCH = 12;
    public static final int OPERATION_FORBIDDEN = 13;
    public static final int APPLICATION_NAME_ALREADY_EXISTS = 14;
    public static final int NO_PLACES_IN_DEVICE = 15;
    public static final int MGT_KEY_MISSING = 16;
    public static final int BAD_MGT_KEY = 17;
    public static final int DEVICE_ID_ALREADY_EXISTS = 18;
    public static final int IP_NOT_FOUND = 19;
    public static final int TOO_MANY_USER_WITH_IP = 20;
    public static final int INVALID_ARG = 21;
    public static final int APPLICATION_NOT_FOUND = 22;
    public static final int USER_NOT_FOUND = 23;
    public static final int INVALID_TIMEZONE = 24;
    public static final int INVALID_DATE = 25;
    public static final int MAX_USAGE_REACHED = 26;
    public static final int MEASURE_ALREADY_EXISTS = 27;
    public static final int ALREADY_DEVICE_OWNER = 28;
    public static final int INVALID_IP = 29;
    public static final int INVALID_REFRESH_TOKEN = 30;
    public static final int NOT_FOUND = 31;
    public static final int BAD_PASSWORD = 32;
    public static final int FORCE_ASSOCIATE = 33;

    public static final int USER_NEED_LOGIN = 9997;
    public static final int EXCEPTION_NOCONNECT = 9998;
    public static final int UNKNOWN = 9999;
}