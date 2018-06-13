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

import java.util.ArrayList;
import java.util.List;

public class Station {
    private String name;
    private String id;
    private List<Module> modules;

    public Station(final String name, final String id) {
        this.name = name;
        this.id = id;
        this.modules = new ArrayList<>();
    }

    public Station(final Station station) {
        this.name = station.getName();
        this.id = station.getId();
        this.modules = new ArrayList<>(station.getModules());
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

    public List<Module> getModules() {
        return modules;
    }

    public void setModules(final List<Module> modules) {
        this.modules = modules;
    }

    public void addModule(final Module module) {
        this.modules.add(module);
    }
}