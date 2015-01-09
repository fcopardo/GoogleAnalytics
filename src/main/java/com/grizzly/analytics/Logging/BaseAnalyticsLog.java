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

import android.os.Build;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.grizzly.analytics.DefinitionsAnalytics;
import com.grizzly.apf.Definitions.DefinitionsStrategies;
import com.grizzly.apf.Ormlite.Model.BaseModel;
import com.j256.ormlite.field.DatabaseField;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Base class for analytics logs.
 * Created on 03-04-14.
 */
public abstract class BaseAnalyticsLog extends BaseModel<String> {

    /*
    Constants to define database column names.
     */
    @JsonIgnore
    public static final String ID = "id";
    @JsonIgnore
    public static final String APP = "app";
    @JsonIgnore
    public static final String WINDOW = "window";
    @JsonIgnore
    public static final String DEVICE = "device";
    @JsonIgnore
    public static final String ANALYTICS_STRATEGY = "analytics_strategy";

    /**
     * Constructor method. Based in the analytics strategy, selects the most fitting storage strategy.
     * It also sets the ID value of the class to the current count of logged events.
     */
    public BaseAnalyticsLog() {

        super(String.class);

        SimpleDateFormat sd = new SimpleDateFormat("yyyy/MM/dd HH:mm:ssz");
        this.idField = sd.format(Calendar.getInstance(Locale.getDefault()).getTime())+"-"+Build.MANUFACTURER+" "+Build.DEVICE;
        resetStrategy();

    }

    /**
     * Class members.
     * idField: autogenerated ID with the date, time, timezone, manufacturer and device name. For database storage.
     * app: The app's name.
     * Window: The source view.
     * sendValues: a list of data to be sent in the report.
     * freeValues: a list of random data in string format to be sent.
     */
    @JsonIgnore
    @DatabaseField(columnName = ID, id = true, generatedId = true)
    private String idField;
    @JsonProperty("app")
    @DatabaseField(columnName = APP)
    private String app;
    @JsonProperty("window")
    @DatabaseField(columnName = WINDOW)
    private String window;
    @JsonProperty("device")
    @DatabaseField(columnName = DEVICE)
    private String device = Build.DEVICE;
    @JsonIgnore
    private List<Integer> analyticsStrategies = new ArrayList<>();

    @JsonIgnore
    private HashMap<String, Object> sendValues = new HashMap<String, Object>();
    @JsonIgnore
    private HashMap<String, String> freeValues = new HashMap<String, String>();

    @JsonIgnore
    public String getIdField() {
        return idField;
    }

    @JsonIgnore
    public void setIdField(String idField) {
        this.idField = idField;
    }

    public String getApp() {
        return app;
    }

    public void setApp(String app) {
        this.app = app;
    }

    public String getWindow() {
        return window;
    }

    public void setWindow(String window) {
        this.window = window;
    }

    public String getDevice() {
        return device;
    }

    public void setDevice(String device) {
        this.device = device;
    }

    public List<Integer> getAnalyticsStrategies() {
        return analyticsStrategies;
    }

    public void setAnalyticsStrategy(List<Integer> analyticsStrategies) {

        this.analyticsStrategies = analyticsStrategies;
        resetStrategy();
    }

    /**
     * Activates a valid analytics strategy.
     * @param analyticsStrategy
     */
    public void addAnalyticsStrategy(int analyticsStrategy) {

        if(DefinitionsAnalytics.isStrategy(analyticsStrategy)){
            this.analyticsStrategies.add(analyticsStrategy);
            resetStrategy();
        }
    }

    /**
     * Discards a valid analytics strategy.
     * @param analyticsStrategy
     */
    public void deleteAnalyticsStrategy(int analyticsStrategy) {

        if(DefinitionsAnalytics.isStrategy(analyticsStrategy) && this.analyticsStrategies.contains(analyticsStrategy)){
            this.analyticsStrategies.remove(analyticsStrategy);
            resetStrategy();
        }
    }

    /**
     * If procedes, activates the database storage strategy.
     */
    private void resetStrategy(){

        if(analyticsStrategies.contains(DefinitionsAnalytics.STRATEGY_ALL) || analyticsStrategies.contains(DefinitionsAnalytics.STRATEGY_LOCAL)){
            this.setStorageStrategy(DefinitionsStrategies.STRATEGY_DATABASE);
        }
        else{
            this.setStorageStrategy(DefinitionsStrategies.STRATEGY_DONT_STORE);
        }
    }

    @JsonIgnore
    @Override
    public String getId() {
        return getIdField();
    }

    @JsonIgnore
    @Override
    public void setId(String id) {
        setIdField(id);
    }

    public HashMap<String, String> getFreeValues() {
        return freeValues;
    }

    public void setFreeValues(HashMap<String, String> freeValues) {
        this.freeValues = freeValues;
    }

    /**
     * Allows the subclasses to access the list of reporting arguments.
     * @return a list of String type.
     */
    protected HashMap<String, Object> getSendValues(){
        return sendValues;
    }

    /**
     * Allows any classes to get the list of reporting arguments.
     * @return a list of String type.
     */
    public HashMap<String, Object> loadSendvalues(){
        initializeSendValues();
        return sendValues;
    }

    /**
     * Fills the list of reporting arguments.
     * example:
     *
     *      sendValues.put("App", getApp());
     *      sendValues.put("Window", getWindow());
     *      sendValues.put("Device", getDevice());
     */
    protected void initializeSendValues(){

        addSendValues();
    }

    /**
     * Abstract method. Subclasses must use it to add their members to the reporting argument list.
     * Ej:
     * getSendValues().put(key, myclassInstance.getMemberToBeReported())
     */
    protected abstract void addSendValues();

}
