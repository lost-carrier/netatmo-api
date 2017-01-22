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

	public static final double NO_DATA = Double.NaN;

	long beginTime;

	double temperature;
	double minTemp;
	double maxTemp;
	double humidity;
	double pressure;
	double noise;
	double CO2;
	double rain;
	double sum_rain_24;
	double sum_rain_1;
	double windAngle;
	double windStrength;
	double gustAngle;
	double gustStrength;

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

	public void setBeginTime(long beginTime) {
		this.beginTime = beginTime;
	}

	public double getTemperature() {
		return temperature;
	}

	public void setTemperature(double temperature) {
		this.temperature = temperature;
	}

	public double getCO2() {
		return CO2;
	}

	public void setCO2(double CO2) {
		this.CO2 = CO2;
	}

	public double getHumidity() {
		return humidity;
	}

	public void setHumidity(double humidity) {
		this.humidity = humidity;
	}

	public double getPressure() {
		return pressure;
	}

	public void setPressure(double pressure) {
		this.pressure = pressure;
	}

	public double getNoise() {
		return noise;
	}

	public void setNoise(double noise) {
		this.noise = noise;
	}

	public double getMinTemp() {
		return minTemp;
	}

	public void setMinTemp(double minTemp) {
		this.minTemp = minTemp;
	}

	public double getMaxTemp() {
		return maxTemp;
	}

	public void setMaxTemp(double maxTemp) {
		this.maxTemp = maxTemp;
	}

	public double getSum_rain_24() {
		return sum_rain_24;
	}

	public void setSum_rain_24(double sum_rain_24) {
		this.sum_rain_24 = sum_rain_24;
	}

	public void setSum_rain_1(double sum_rain_1) {
		this.sum_rain_1 = sum_rain_1;
	}

	public double getSum_rain_1() {
		return sum_rain_1;
	}

	public void setRain(double rain) {
		this.rain = rain;
	}

	public double getRain() {
		return rain;
	}

	public void setWindAngle(double windAngle) {
		this.windAngle = windAngle;
	}

	public double getWindAngle() {
		return windAngle;
	}

	public void setWindStrength(double windStrength) {
		this.windStrength = windStrength;
	}

	public double getWindStrength() {
		return windStrength;
	}

	public void setGustAngle(double gustAngle) {
		this.gustAngle = gustAngle;
	}

	public double getGustAngle() {
		return gustAngle;
	}

	public void setGustStrength(double gustStrength) {
		this.gustStrength = gustStrength;
	}

	public double getGustStrength() {
		return gustStrength;
	}

}