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

public class Module {

    public static final String TYPE_OUTDOOR = "NAModule1";
    public static final String TYPE_WIND_GAUGE = "NAModule2";
    public static final String TYPE_RAIN_GAUGE = "NAModule3";
    public static final String TYPE_INDOOR = "NAModule4";
    public static final String TYPE_NA_THERM_1 = "NATherm1"; // thermostat
    public static final String TYPE_VALVE = "NRV"; // valve
    public static final String TYPE_NA_PLUG = "NAPlug"; // relay
    public static final String TYPE_NA_CAMERA = "NACamera"; // welcome camera
    public static final String TYPE_NOC = "NOC"; // presence camera

    private String name;
    private String id;
    private String type;
    private boolean reachable;
    private boolean boiler_status;

    public Module() {
    }

    public Module(final String name, final String id, final String type) {
        this.name = name;
        this.id = id;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(final String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(final String type) {
        this.type = type;
    }

    public boolean isReachable() {
        return reachable;
    }

    public void setReachable(boolean reachable) {
        this.reachable = reachable;
    }

    public boolean isBoilerStatus() {
        return boiler_status;
    }

    public void setBoilerStatus(boolean boiler_status) {
        this.boiler_status = boiler_status;
    }
}