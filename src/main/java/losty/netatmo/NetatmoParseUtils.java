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

import losty.netatmo.model.Module;
import losty.netatmo.model.*;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.*;

public class NetatmoParseUtils {

    private NetatmoParseUtils() {
        throw new IllegalStateException("Utility class");
    }

    public static List<Station> parseStationsData(final JSONObject response) throws JSONException {
        final List<Station> result = new ArrayList<>();

        if (response == null) {
            return result;
        }

        final JSONArray JSONstations = response.getJSONObject("body").getJSONArray("devices");

        for (int i = 0; i < JSONstations.length(); i++) {
            final JSONObject station = JSONstations.getJSONObject(i);
            final String stationId = station.getString("_id");
            final String stationName = station.getString("station_name");
            final String stationModuleName = station.optString("module_name", stationId);

            final Station newStation = new Station(stationName, stationId);
            final Module newStationModule = new Module(stationModuleName, stationId, Module.TYPE_INDOOR);

            newStation.addModule(newStationModule);


            JSONArray JSONmodules = station.getJSONArray("modules");

            for (int j = 0; j < JSONmodules.length(); j++) {
                final JSONObject module = JSONmodules.getJSONObject(j);
                final String moduleId = module.getString("_id");
                final String moduleName = module.optString("module_name", moduleId);
                final String moduleType = module.getString("type");

                final Module newModule = new Module(moduleName, moduleId, moduleType);
                newStation.addModule(newModule);
            }

            result.add(newStation);
        }

        return result;
    }

    public static List<Measures> parseMeasures(final JSONObject response, final String[] types) throws JSONException {
        final List<Measures> result = new ArrayList<>();

        if (response == null) {
            return result;
        }

        final JSONArray body = response.getJSONArray("body");
        for (int i = 0; i < body.length(); i++) {
            final JSONObject data = body.getJSONObject(i);

            final Long beginTime = data.getLong("beg_time");
            final Long stepTime = data.optLong("step_time", 0);
            final JSONArray values = data.getJSONArray("value");

            for (int j = 0; j < values.length(); j++) {
                final JSONArray myValues = values.getJSONArray(j);
                long myBeginTime = (beginTime + j * stepTime) * 1000;
                final Measures measures = new Measures();
                measures.setBeginTime(myBeginTime);

                for (int k = 0; k < types.length; k++) {
                    if (myValues.isNull(k)) {
                        continue;
                    }

                    switch (types[k]) {
                        case Params.TYPE_TEMPERATURE:
                            measures.setTemperature(myValues.getDouble(k));
                            break;
                        case Params.TYPE_CO2:
                            measures.setCO2(myValues.getDouble(k));
                            break;
                        case Params.TYPE_HUMIDITY:
                            measures.setHumidity(myValues.getDouble(k));
                            break;
                        case Params.TYPE_PRESSURE:
                            measures.setPressure(myValues.getDouble(k));
                            break;
                        case Params.TYPE_NOISE:
                            measures.setNoise(myValues.getDouble(k));
                            break;
                        case Params.TYPE_MIN_TEMP:
                            measures.setMinTemp(myValues.getDouble(k));
                            break;
                        case Params.TYPE_MAX_TEMP:
                            measures.setMaxTemp(myValues.getDouble(k));
                            break;
                        case Params.TYPE_RAIN:
                            measures.setRain(myValues.getDouble(k));
                            break;
                        case Params.TYPE_RAIN_SUM:
                            measures.setSum_rain(myValues.getDouble(k));
                            break;
                        case Params.TYPE_WIND_ANGLE:
                            measures.setWindAngle(myValues.getDouble(k));
                            break;
                        case Params.TYPE_WIND_STRENGTH:
                            measures.setWindStrength(myValues.getDouble(k));
                            break;
                        case Params.TYPE_GUST_ANGLE:
                            measures.setGustAngle(myValues.getDouble(k));
                            break;
                        case Params.TYPE_GUST_STRENGTH:
                            measures.setGustStrength(myValues.getDouble(k));
                            break;
                        default:
                            //
                    }
                }
                result.add(measures);
            }
        }

        return result;
    }

    public static List<Map.Entry<Station, Measures>> parsePublicData(final JSONObject response) throws JSONException {
        final List<Map.Entry<Station, Measures>> result = new ArrayList<>();

        if (response == null) {
            return result;
        }

        final JSONArray body = response.getJSONArray("body");
        for (int i = 0; i < body.length(); i++) {

            final JSONObject data = body.getJSONObject(i);
            final Station station = parseStation(data);
            final List<Module> modules = parseModules(data.getJSONObject("module_types"));
            final Map<String, Station> stationsById = determineStations(station, modules);

            final JSONObject measuresAllModules = data.getJSONObject("measures");
            for (String measuresModuleId : measuresAllModules.keySet()) {

                final JSONObject measuresThisModule = measuresAllModules.getJSONObject(measuresModuleId);
                final Station stationForModule = stationsById.get(measuresModuleId);
                Measures measures = null;
                if (stationForModule == null) {
                    throw new RuntimeException(format("Cannot find moduleId=%s", measuresModuleId));
                } else if (stationForModule.getModules().isEmpty()) {
                    measures = parseMeasuresMethod1(measuresThisModule);
                } else switch (stationForModule.getModules().get(0).getType()) {
                    case Module.TYPE_OUTDOOR:
                        measures = parseMeasuresMethod1(measuresThisModule);
                        break;
                    case Module.TYPE_WIND_GAUGE:
                        measures = parseMeasuresMethod2(measuresThisModule);
                        break;
                    case Module.TYPE_RAIN_GAUGE:
                        measures = parseMeasuresMethod2(measuresThisModule);
                        break;
                    case Module.TYPE_INDOOR:
                        measures = parseMeasuresMethod1(measuresThisModule);
                        break;
                }
                result.add(new AbstractMap.SimpleImmutableEntry<>(stationForModule, measures));
            }

        }

        return result;
    }

    public static List<Home> parseHomesdata(final JSONObject response) throws JSONException {
        final List<Home> result = new ArrayList<>();

        if (response == null) {
            return result;
        }

        final JSONArray jsonHomes = response.getJSONObject("body").getJSONArray("homes");

        for (int i = 0; i < jsonHomes.length(); i++) {
            final JSONObject homeJsonObject = jsonHomes.getJSONObject(i);
            final String id = homeJsonObject.getString("id");
            final String name = homeJsonObject.getString("name");

            final Home home = new Home(id, name);
            result.add(home);
        }

        return result;
    }

    public static Home parseHomestatus(final JSONObject response) throws JSONException {
        Home home = new Home();

        if (response == null) {
            return home;
        }

        final JSONObject jsonHome = response.getJSONObject("body").getJSONObject("home");
        final String home_id = jsonHome.getString("id");
        home.setId(home_id);

        final JSONArray jsonModules = jsonHome.getJSONArray("modules");
        for (int i = 0; i < jsonModules.length(); i++) {
            final JSONObject jsonModule = jsonModules.getJSONObject(i);
            final String module_id = jsonModule.getString("id");
            final String module_type = jsonModule.getString("type");

            Module module = new Module();
            module.setId(module_id);
            module.setType(module_type);

            if (module_type.equals(Module.TYPE_NA_THERM_1)) {
                boolean boiler_status = jsonModule.getBoolean("boiler_status");
                boolean reachable = jsonModule.getBoolean("reachable");
                module.setBoilerStatus(boiler_status);
                module.setReachable(reachable);
            }

            home.addModule(module);
        }

        final JSONArray jsonRooms = jsonHome.getJSONArray("rooms");
        for (int i = 0; i < jsonRooms.length(); i++) {
            final JSONObject jsonRoom = jsonRooms.getJSONObject(i);
            final String room_id = jsonRoom.getString("id");
            final boolean reachable = jsonRoom.getBoolean("reachable");
            final float therm_measured_temperature = jsonRoom.getFloat("therm_measured_temperature");
            final float therm_setpoint_temperature = jsonRoom.getFloat("therm_setpoint_temperature");
            final String therm_setpoint_mode = jsonRoom.getString("therm_setpoint_mode");

            Long therm_setpoint_start_time = null;
            if (jsonRoom.opt("therm_setpoint_start_time") != null) {
                therm_setpoint_start_time = jsonRoom.getLong("therm_setpoint_start_time");
            }

            Long therm_setpoint_end_time = null;
            if (jsonRoom.opt("therm_setpoint_end_time") != null) {
                therm_setpoint_end_time = jsonRoom.getLong("therm_setpoint_end_time");
            }

            Room room = new Room();
            room.setId(room_id);
            room.setReachable(reachable);
            room.setTherm_measured_temperature(therm_measured_temperature);
            room.setTherm_setpoint_temperature(therm_setpoint_temperature);
            room.setTherm_setpoint_mode(therm_setpoint_mode);
            if (therm_setpoint_start_time != null){
                room.setTherm_setpoint_start_time(therm_setpoint_start_time);}
            if (therm_setpoint_end_time != null){
                room.setTherm_setpoint_end_time(therm_setpoint_end_time);}

            home.addRoom(room);
        }

        return home;
    }

    private static Map<String, Station> determineStations(Station station, List<Module> modules) {
        final Map<String, Station> ret = new HashMap<>();
        ret.put(station.getId(), station);
        for (Module module : modules) {
            final Station stationForModule = new Station(station);
            stationForModule.setModules(Collections.singletonList(module));
            ret.put(module.getId(), stationForModule);
        }
        return ret;
    }

    private static Measures parseMeasuresMethod1(JSONObject measuresThisModule) {
        final JSONArray types = measuresThisModule.getJSONArray("type");
        final JSONObject res = measuresThisModule.getJSONObject("res");
        final String timestampStr = res.keys().next();
        final JSONArray values = res.getJSONArray(timestampStr);
        final Measures ret = new Measures();
        long timestamp = Long.parseLong(timestampStr);
        ret.setBeginTime(timestamp);
        for (int i = 0; i < types.length(); i++) {
            final String type = types.getString(i);
            double value = values.getDouble(i);
            switch (type) {
                case "temperature":
                    ret.setTemperature(value);
                    break;
                case "humidity":
                    ret.setHumidity(value);
                    break;
                case "pressure":
                    ret.setPressure(value);
                    break;
            }
        }
        return ret;
    }

    private static Measures parseMeasuresMethod2(JSONObject measuresThisModule) {
        Measures ret = new Measures();
        for (String key : measuresThisModule.keySet()) {
            switch (key) {
                case "rain_60min":
                    ret.setSum_rain_1(measuresThisModule.getDouble(key));
                    break;
                case "rain_24h":
                    ret.setSum_rain_24(measuresThisModule.getDouble(key));
                    break;
                case "rain_live":
                    ret.setRain(measuresThisModule.getDouble(key));
                    break;
                case "wind_strength":
                    ret.setWindStrength(measuresThisModule.getDouble(key));
                    break;
                case "wind_angle":
                    ret.setWindAngle(measuresThisModule.getDouble(key));
                    break;
                case "gust_strength":
                    ret.setGustStrength(measuresThisModule.getDouble(key));
                    break;
                case "gust_angle":
                    ret.setGustAngle(measuresThisModule.getDouble(key));
                    break;
                case "rain_timeutc":
                case "wind_timeutc":
                    ret.setBeginTime(measuresThisModule.getLong(key));
                    break;
            }
        }
        return ret;
    }

    private static List<Module> parseModules(JSONObject modules) {
        List<Module> ret = new ArrayList<>();
        for (String moduleId : modules.keySet()) {
            String moduleType = modules.getString(moduleId);
            Module module = new Module(null, moduleId, moduleType);
            ret.add(module);
        }

        return ret;
    }

    private static Station parseStation(JSONObject data) {
        final String id = data.getString("_id");
        final JSONObject place = data.getJSONObject("place");
        final JSONArray location = place.getJSONArray("location");
//        final String name = String.format("%f,%f,%f", location.getDouble(1), location.getDouble(0), place.getDouble("altitude"));
        final String name = format("%f,%f", location.getDouble(1), location.getDouble(0));
        return new Station(name, id);
    }

    public static String format(String string, Object... params) {
        return String.format(Locale.US, string, params);
    }
}