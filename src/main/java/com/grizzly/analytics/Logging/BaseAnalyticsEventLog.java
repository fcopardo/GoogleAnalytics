/*
 * Copyright (c) 2014.  Fco Pardo Baeza
 *
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 */

package com.grizzly.analytics.Logging;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.grizzly.analytics.DefinitionsAnalytics;
import com.j256.ormlite.field.DatabaseField;

/**
 * Created on 03-04-14.
 */
public class BaseAnalyticsEventLog extends BaseAnalyticsLog {

    @JsonIgnore
    public static final String LABEL = "label";
    @JsonIgnore
    public static final String CATEGORY = "category";
    @JsonIgnore
    public static final String ACTION = "action";
    @JsonIgnore
    public static final String VALUE = "value";

    public BaseAnalyticsEventLog() {

        this.addAnalyticsStrategy(DefinitionsAnalytics.STRATEGY_GOOGLE);
    }

    @Override
    protected void addSendValues() {
        this.getSendValues().put(LABEL, getLabel());
        this.getSendValues().put(CATEGORY, getCategory());
        this.getSendValues().put(ACTION, getAction());
        this.getSendValues().put(VALUE, getValue());
    }

    @JsonProperty("label")
    @DatabaseField(columnName = LABEL)
    private String label;
    @JsonProperty("category")
    @DatabaseField(columnName = CATEGORY)
    private String category;
    @JsonProperty("action")
    @DatabaseField(columnName = ACTION)
    private String action;
    @JsonProperty("value")
    @DatabaseField(columnName = VALUE)
    private Long value = 0L;

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public Long getValue() {
        return value;
    }

    public void setValue(Long value) {
        this.value = value;
    }
}
