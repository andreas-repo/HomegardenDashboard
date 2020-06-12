package org.homegarden.gui.logic;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.homegarden.gui.model.Sensor;

import javax.json.bind.Jsonb;
import javax.json.bind.JsonbBuilder;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;

public class Humidification {
    private final Sensor dryReference;
    private final Sensor idealReference;
    private final int id;
    private boolean running;

    private final String BASE_URI = "http://localhost:8080/sensor/";
    private final String BASE_URI_RELAY = "http://192.168.0.11:8080/relay/";

    public Humidification(Sensor dryReference, Sensor idealReference, boolean running) throws InterruptedException {
        this.dryReference = dryReference;
        this.idealReference = idealReference;
        this.id = this.dryReference.getId();
        this.running = running;
        //wateringLogic(this.running);
    }

    private int getActualValue() {
        int sensorValue = 0;
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpGet httpGet = new HttpGet(BASE_URI +this.id);
        try {
            CloseableHttpResponse response = httpClient.execute(httpGet);
            System.out.println(response.getStatusLine());
            HttpEntity entity = response.getEntity();
            InputStream inputStream = entity.getContent();
            BufferedInputStream input = new BufferedInputStream(inputStream);
            byte[] buffer = new byte[1024];
            while(input.read(buffer) != -1) {}
            String sensorJson = new String(buffer);
            Jsonb jsonb = JsonbBuilder.create();
            Sensor sensor = jsonb.fromJson(sensorJson, Sensor.class);
            sensorValue += sensor.getSensorValue();
            System.out.println("Actual Sensorvalue " +this.id + " " +sensorValue);
            EntityUtils.consume(entity);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return sensorValue;
    }

    private void openValve() {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpGet httpGet = new HttpGet(BASE_URI_RELAY + "/start/" + this.id);
        try {
            CloseableHttpResponse response = httpClient.execute(httpGet);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void closeValve() {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpGet httpGet = new HttpGet(BASE_URI_RELAY + "/stop/" + this.id);
        try {
            CloseableHttpResponse response = httpClient.execute(httpGet);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void wateringLogic(boolean running) throws InterruptedException {
        int dryNew = dryReference.getSensorValue() - idealReference.getSensorValue();
        boolean doWatering = false;
        while(running == true) {
            int sensorValue = getActualValue();
            sensorValue = sensorValue - idealReference.getSensorValue();
            double result =  ((double)sensorValue / (double)dryNew) * 100;
            int percentage = (int)result;
            if(percentage >= 70) {
                System.out.println("Percentage value before watering = " +percentage);
                openValve();
                doWatering = true;
                while(doWatering) {
                    int value = getActualValue();
                    value = value - idealReference.getSensorValue();
                    double result2 = ((double)value / (double)dryNew) * 100;
                    value = (int)result2;
                    System.out.println("While watering value = " + value);
                    if(value <= 10) {
                        doWatering = false;
                        closeValve();
                        break;
                    }
                    Thread.sleep(1000);
                }
                Thread.sleep(1000);
            } else {
                closeValve();
            }
            Thread.sleep(1000);
        }
    }
}
