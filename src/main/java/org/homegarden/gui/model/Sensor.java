package org.homegarden.gui.model;

import javax.json.bind.annotation.JsonbProperty;
import java.io.Serializable;
import java.util.Date;

public class Sensor implements Serializable {
    @JsonbProperty("id")
    private int id;
    private Date timestamp;
    @JsonbProperty("sensorValue")
    private int sensorValue;


    public Sensor() {
        this.timestamp = new Date();
    }
    public Sensor(int id) {
        this.id = id;
        this.timestamp = new Date();
    }
    public Sensor(int id, int sensorValue) {
        this.id = id;
        this.timestamp = new Date();
        this.sensorValue = sensorValue;
    }


    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getTimestamp() {
        return this.timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public int getSensorValue() {
        return this.sensorValue;
    }

    public void setSensorValue(int sensorValue) {
        this.sensorValue = sensorValue;
    }


}