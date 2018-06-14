Netatmo Java API (unofficial)
========

Small adaption of the original [Netatmo Android API][1] that can be used with plain Java (>=v7) instead of Android. Nothing special, but might help you getting started as some parsing and the OAuth2 stuff is already handled (many thanks to those folks at [Apache Oltu][2]!).

Documentation of their API is available at https://dev.netatmo.com/doc/.

Installation
--------
Now available at Maven Central, so just add the dependency to your ```pom.xml```.

    <dependencies>
        [...]
        <dependency>
            <groupId>org.losty.netatmo</groupId>
            <artifactId>netatmo-api</artifactId>
            <version>0.2.0</version>
        </dependency>
        [...]
    </dependencies>

Usage
--------

You'll get CLIENT_ID and CLIENT_SECRET at https://dev.netatmo.com/dev/createapp.

	NetatmoHttpClient client = new NetatmoHttpClient(CLIENT_ID, CLIENT_SECRET);
	OAuthJSONAccessTokenResponse token = client.login(E_MAIL, PASSWORD);
	
	List<String> types = Arrays.asList(Params.TYPE_TEMPERATURE, Params.TYPE_PRESSURE, Params.TYPE_HUMIDITY);
	Date dateBegin = DateTime.parse("2015-09-10T00Z").toDate();
	Date dateEnd = DateTime.parse("2015-09-11T00Z").toDate();
	Station station = client.getStationsData(token).get(0);
	
	List<Measures> measures = client.getMeasures(token, station, station.getModules().get(0), types, Params.SCALE_MAX, dateBegin, dateEnd, null, null);
	for (Measures measure : measures) {
		new DateTime(measure.getBeginTime()).withZone(DateTimeZone.UTC).toString();
		measure.getTemperature();
		measure.getPressure();
		measure.getHumidity();
		...

What else?
--------

Hmm... That's about it!

Forks, PRs and stuff is always welcome!

License
--------

    Copyright 2013 Netatmo

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

        http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.

[1]: https://github.com/Netatmo/Netatmo-API-Android
[2]: https://oltu.apache.org