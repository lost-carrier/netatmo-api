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
package losty.netatmo.model;

public class Measures {

	private static final double NO_DATA = Double.NaN;

	private long beginTime;

	private double temperature;
	private double minTemp;
	private double maxTemp;
	private double humidity;
	private double pressure;
	private double noise;
	private double CO2;
	private double rain;
	private double sum_rain;
	private double sum_rain_24;
	private double sum_rain_1;
	private double windAngle;
	private double windStrength;
	private double gustAngle;
	private double gustStrength;

	public Measures() {
		beginTime = 0;
		temperature = NO_DATA;
		minTemp = NO_DATA;
		maxTemp = NO_DATA;
		humidity = NO_DATA;
		pressure = NO_DATA;
		noise = NO_DATA;
		CO2 = NO_DATA;
		rain = NO_DATA;
		sum_rain = NO_DATA;
		sum_rain_1 = NO_DATA;
		sum_rain_24 = NO_DATA;
		windAngle = NO_DATA;
		windStrength = NO_DATA;
		gustAngle = NO_DATA;
		gustStrength = NO_DATA;
	}

	public long getBeginTime() {
		return this.beginTime;
	}

	public void setBeginTime(final long beginTime) {
		this.beginTime = beginTime;
	}

	public double getTemperature() {
		return temperature;
	}

	public void setTemperature(final double temperature) {
		this.temperature = temperature;
	}

	public double getCO2() {
		return CO2;
	}

	public void setCO2(final double CO2) {
		this.CO2 = CO2;
	}

	public double getHumidity() {
		return humidity;
	}

	public void setHumidity(final double humidity) {
		this.humidity = humidity;
	}

	public double getPressure() {
		return pressure;
	}

	public void setPressure(final double pressure) {
		this.pressure = pressure;
	}

	public double getNoise() {
		return noise;
	}

	public void setNoise(final double noise) {
		this.noise = noise;
	}

	public double getMinTemp() {
		return minTemp;
	}

	public void setMinTemp(final double minTemp) {
		this.minTemp = minTemp;
	}

	public double getMaxTemp() {
		return maxTemp;
	}

	public void setMaxTemp(final double maxTemp) {
		this.maxTemp = maxTemp;
	}

	/**
	 * Will be filled by getPublicData() requests.
	 * @return sum_rain_24
	 */
	public double getSum_rain_24() {
		return sum_rain_24;
	}

	public void setSum_rain_24(final double sum_rain_24) {
		this.sum_rain_24 = sum_rain_24;
	}

	/**
	 * Will be filled by getPublicData() requests.
	 * @return sum_rain_1
	 */
	public double getSum_rain_1() {
		return sum_rain_1;
	}

	public void setSum_rain_1(final double sum_rain_1) {
		this.sum_rain_1 = sum_rain_1;
	}

	/**
	 * Will be filled by getMeasures() requests with aggregation (scale != max).
	 * @return sum_rain
	 */
	public double getSum_rain() {
		return sum_rain;
	}

	public void setSum_rain(final double sum_rain) {
		this.sum_rain = sum_rain;
	}

	/**
	 * Will be filled by getMeasures() requests without aggregation (scale == max). This gets also filled for requests
	 * with aggregation (scale != max), but not sure how the data should make sense...
	 * @return rain
	 */
	public double getRain() {
		return rain;
	}

	public void setRain(final double rain) {
		this.rain = rain;
	}

	public double getWindAngle() {
		return windAngle;
	}

	public void setWindAngle(final double windAngle) {
		this.windAngle = windAngle;
	}

	public double getWindStrength() {
		return windStrength;
	}

	public void setWindStrength(final double windStrength) {
		this.windStrength = windStrength;
	}

	public double getGustAngle() {
		return gustAngle;
	}

	public void setGustAngle(final double gustAngle) {
		this.gustAngle = gustAngle;
	}

	public double getGustStrength() {
		return gustStrength;
	}

	public void setGustStrength(final double gustStrength) {
		this.gustStrength = gustStrength;
	}
}