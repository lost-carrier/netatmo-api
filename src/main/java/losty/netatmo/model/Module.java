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
    private Integer firmware;
    private Long lastSetup;
    private Long lastMessage;
    private Long lastSeen;
    private Integer rfStatus;
    private Integer batteryVp;
    private Integer batteryPercent;
    private Boolean reachable;
    private Boolean boiler_status;

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

    public Integer getFirmware() {
        return firmware;
    }

    public void setFirmware(Integer firmware) {
        this.firmware = firmware;
    }

    public Long getLastSetup() {
        return lastSetup;
    }

    public void setLastSetup(Long lastSetup) {
        this.lastSetup = lastSetup;
    }

    public Long getLastMessage() {
        return lastMessage;
    }

    public void setLastMessage(Long lastMessage) {
        this.lastMessage = lastMessage;
    }

    public Long getLastSeen() {
        return lastSeen;
    }

    public void setLastSeen(Long lastSeen) {
        this.lastSeen = lastSeen;
    }

    public Integer getRfStatus() {
        return rfStatus;
    }

    public void setRfStatus(Integer rfStatus) {
        this.rfStatus = rfStatus;
    }

    public Integer getBatteryVp() {
        return batteryVp;
    }

    public void setBatteryVp(Integer batteryVp) {
        this.batteryVp = batteryVp;
    }

    public Integer getBatteryPercent() {
        return batteryPercent;
    }

    public void setBatteryPercent(Integer batteryPercent) {
        this.batteryPercent = batteryPercent;
    }

    public Boolean isReachable() {
        return reachable;
    }

    public void setReachable(Boolean reachable) {
        this.reachable = reachable;
    }

    public Boolean isBoilerStatus() {
        return boiler_status;
    }

    public void setBoilerStatus(Boolean boiler_status) {
        this.boiler_status = boiler_status;
    }
}