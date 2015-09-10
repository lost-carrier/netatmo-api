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
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import losty.netatmo.model.Measures;
import losty.netatmo.model.Module;
import losty.netatmo.model.Params;
import losty.netatmo.model.Station;

public class NetatmoUtils {

	public static List<Station> parseDevicesList(JSONObject response) {
		List<Station> devices = new ArrayList<Station>();

		try {
			JSONArray JSONstations = response.getJSONObject("body").getJSONArray("devices");

			for (int i = 0; i < JSONstations.length(); i++) {
				JSONObject station = JSONstations.getJSONObject(i);
				String name = station.getString("station_name");
				String moduleName = station.getString("module_name");
				String id = station.getString("_id");

				Station newStation = new Station(name, id);
				Module newModule = new Module(moduleName, id, Module.TYPE_INDOOR);

				newStation.addModule(newModule);
				devices.add(newStation);
			}

			JSONArray JSONmodules = response.getJSONObject("body").getJSONArray("modules");

			for (int i = 0; i < JSONmodules.length(); i++) {
				JSONObject module = JSONmodules.getJSONObject(i);
				String mainDevice = module.getString("main_device");
				String name = module.getString("module_name");
				String id = module.getString("_id");
				String type = module.getString("type");

				Module newModule = new Module(name, id, type);
				for (Station station : devices) {
					if (mainDevice.equals(station.getId())) {
						station.addModule(newModule);
					}
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}

		return devices;
	}

	public static List<Measures> parseMeasures(JSONObject response, String[] types) {
		List<Measures> result = new ArrayList<>();
		try {
			JSONArray body = response.getJSONArray("body");
			for (int i = 0; i < body.length(); i++) {
				JSONObject data = body.getJSONObject(i);
				Long beginTime = data.getLong("beg_time");
				Long stepTime = data.getLong("step_time");
				JSONArray values = data.getJSONArray("value");
				for (int j = 0; j < values.length(); j++) {
					JSONArray myValues = values.getJSONArray(j);
					long myBeginTime = (beginTime + j * stepTime) * 1000;
					Measures measures = new Measures();
					measures.setBeginTime(myBeginTime);
					for (int k = 0; k < types.length; k++) {
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
						}
					}
					result.add(measures);
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}

		return result;
	}
	
}