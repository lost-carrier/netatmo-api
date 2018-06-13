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

import java.text.ParseException;
import java.util.*;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import losty.netatmo.model.Measures;
import losty.netatmo.model.Module;
import losty.netatmo.model.Params;
import losty.netatmo.model.Station;

public class NetatmoUtils {

	public static List<Station> parseStationsData(final JSONObject response) throws JSONException {
		final List<Station> result = new ArrayList<Station>();

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
					case Params.TYPE_RAIN_SUM_1:
						measures.setSum_rain_1(myValues.getDouble(k));
						break;
					case Params.TYPE_RAIN_SUM_24:
						measures.setSum_rain_24(myValues.getDouble(k));
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
	
}