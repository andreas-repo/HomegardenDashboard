package org.homegarden.gui;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.homegarden.gui.logic.MultiThreadManager;
import org.homegarden.gui.model.Sensor;

import javax.json.bind.Jsonb;
import javax.json.bind.JsonbBuilder;
import java.io.*;

public class GuiModel extends Application {
    private boolean isConfigDry1 = false;
    private Sensor referenceDryValueSensor1;
    private boolean isConfigIdeal1 = false;
    private Sensor referenceIdealValueSensor1;

    private boolean isConfigDry2 = false;
    private Sensor referenceDryValueSensor2;
    private boolean isConfigIdeal2 = false;
    private Sensor referenceIdealValueSensor2;

    private boolean isConfigDry3 = false;
    private Sensor referenceDryValueSensor3;
    private boolean isConfigIdeal3 = false;
    private Sensor referenceIdealValueSensor3;

    private boolean isConfigDry4 = false;
    private Sensor referenceDryValueSensor4;
    private boolean isConfigIdeal4 = false;
    private Sensor referenceIdealValueSensor4;

    private MultiThreadManager number1;
    private MultiThreadManager number2;
    private MultiThreadManager number3;
    private MultiThreadManager number4;

    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/fxml_gui-model.fxml"));

        TabPane tabPane = new TabPane();
        /**
         * Setup Tab
         */
        GridPane gridSetupTab = new GridPane();
        gridSetupTab.setAlignment(Pos.CENTER);
        gridSetupTab.setHgap(10);
        gridSetupTab.setVgap(10);
        gridSetupTab.setPadding(new Insets(25, 25, 25, 25));

        gridSetupTab.getStylesheets().add("userinterface.css");

        Text setupTabTitle = new Text("Automatic Irrigation Sensor Config");
        setupTabTitle.setId("setupTabTitle");
        HBox hbSetupTabTitle = new HBox(10);
        hbSetupTabTitle.setAlignment(Pos.CENTER);
        hbSetupTabTitle.getChildren().add(setupTabTitle);
        gridSetupTab.add(hbSetupTabTitle, 2, 0, 2, 1);

        Label setupTabTextArea = new Label("1.) Put Humidity Sensor into dry soil,\n select Sensors you want to calibrate\n and press \"Dry\" button.\n 2.) Put Humidity Sensor into soil with ideal humidity,\n select Sensors you want to calibrate\n and press \"Ideal Moisture\" button.");
        setupTabTextArea.setId("setupTabTextArea");
        setupTabTextArea.setMouseTransparent(true);
        setupTabTextArea.setWrapText(true);
        HBox hbSetupTabTextArea = new HBox(10);
        hbSetupTabTextArea.setAlignment(Pos.CENTER);
        hbSetupTabTextArea.getChildren().add(setupTabTextArea);
        gridSetupTab.add(hbSetupTabTextArea, 2, 1, 2, 1);

        CheckBox checkBoxSensor1 = new CheckBox("Sensor 1");
        checkBoxSensor1.setId("checkBoxSensor1");
        HBox hbCheckBoxSensor1 = new HBox(10);
        hbCheckBoxSensor1.setAlignment(Pos.CENTER);
        hbCheckBoxSensor1.getChildren().add(checkBoxSensor1);
        gridSetupTab.add(hbCheckBoxSensor1, 0, 3);

        CheckBox checkBoxSensor2 = new CheckBox("Sensor 2");
        checkBoxSensor2.setId("checkBoxSensor2");
        HBox hbCheckBoxSensor2 = new HBox(10);
        hbCheckBoxSensor2.setAlignment(Pos.CENTER);
        hbCheckBoxSensor2.getChildren().add(checkBoxSensor2);
        gridSetupTab.add(hbCheckBoxSensor2, 1, 3);

        CheckBox checkBoxSensor3 = new CheckBox("Sensor 3");
        checkBoxSensor3.setId("checkBoxSensor3");
        HBox hbCheckBoxSensor3 = new HBox(10);
        hbCheckBoxSensor3.setAlignment(Pos.CENTER);
        hbCheckBoxSensor3.getChildren().add(checkBoxSensor3);
        gridSetupTab.add(hbCheckBoxSensor3, 2, 3);

        CheckBox checkBoxSensor4 = new CheckBox("Sensor 4");
        checkBoxSensor4.setId("checkBoxSensor4");
        HBox hbCheckBoxSensor4 = new HBox(10);
        hbCheckBoxSensor4.setAlignment(Pos.CENTER);
        hbCheckBoxSensor4.getChildren().add(checkBoxSensor4);
        gridSetupTab.add(hbCheckBoxSensor4, 3, 3);

        Label setupTabDryLabel = new Label("Setup Dry Value:");
        setupTabDryLabel.setId("setupTabDryLabel");
        HBox hbSetupTabDryLabel = new HBox(10);
        hbSetupTabDryLabel.setAlignment(Pos.CENTER);
        hbSetupTabDryLabel.getChildren().add(setupTabDryLabel);
        gridSetupTab.add(hbSetupTabDryLabel, 1, 5);

        Button setupTabButtonDry = new Button("Dry");
        setupTabButtonDry.setId("setupTabButtonDry");
        HBox hbButtonDry = new HBox(10);
        hbButtonDry.setAlignment(Pos.CENTER);
        hbButtonDry.getChildren().add(setupTabButtonDry);
        gridSetupTab.add(hbButtonDry, 2, 5);

        Label setupTabIdealLabel = new Label("Setup Ideal Value:");
        setupTabIdealLabel.setId("setupTabIdealLabel");
        HBox hbSetupTabIdealLabel = new HBox(10);
        hbSetupTabIdealLabel.setAlignment(Pos.CENTER);
        hbSetupTabIdealLabel.getChildren().add(setupTabIdealLabel);
        gridSetupTab.add(hbSetupTabIdealLabel, 1, 6);

        Button setupTabButtonIdeal = new Button("Ideal Moisture");
        setupTabButtonIdeal.setId("setupTabButtonIdeal");
        HBox hbButtonIdeal = new HBox(10);
        hbButtonIdeal.setAlignment(Pos.CENTER);
        hbButtonIdeal.getChildren().add(setupTabButtonIdeal);
        gridSetupTab.add(hbButtonIdeal, 2, 6);

        setupTabButtonDry.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                if (checkBoxSensor1.isSelected()) {
                    CloseableHttpClient httpClient = HttpClients.createDefault();
                    HttpGet httpGet = new HttpGet("http://localhost:8080/sensor/1");
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
                        GuiModel.this.referenceDryValueSensor1 = jsonb.fromJson(sensorJson, Sensor.class);
                        System.out.println(GuiModel.this.referenceDryValueSensor1.getSensorValue());
                        EntityUtils.consume(entity);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    GuiModel.this.isConfigDry1 = true;
                }
                if (checkBoxSensor2.isSelected()) {
                    CloseableHttpClient httpClient = HttpClients.createDefault();
                    HttpGet httpGet = new HttpGet("http://localhost:8080/sensor/2");
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
                        GuiModel.this.referenceDryValueSensor2 = jsonb.fromJson(sensorJson, Sensor.class);
                        System.out.println(GuiModel.this.referenceDryValueSensor2.getSensorValue());
                        EntityUtils.consume(entity);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    GuiModel.this.isConfigDry2 = true;
                }
                if (checkBoxSensor3.isSelected()) {
                    CloseableHttpClient httpClient = HttpClients.createDefault();
                    HttpGet httpGet = new HttpGet("http://localhost:8080/sensor/3");
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
                        GuiModel.this.referenceDryValueSensor3 = jsonb.fromJson(sensorJson, Sensor.class);
                        System.out.println(GuiModel.this.referenceDryValueSensor3.getSensorValue());
                        EntityUtils.consume(entity);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    GuiModel.this.isConfigDry3 = true;
                }
                if (checkBoxSensor4.isSelected()) {
                    CloseableHttpClient httpClient = HttpClients.createDefault();
                    HttpGet httpGet = new HttpGet("http://localhost:8080/sensor/4");
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
                        GuiModel.this.referenceDryValueSensor4 = jsonb.fromJson(sensorJson, Sensor.class);
                        System.out.println(GuiModel.this.referenceDryValueSensor4.getSensorValue());
                        EntityUtils.consume(entity);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    GuiModel.this.isConfigDry4 = true;
                }
            }
        });


        setupTabButtonIdeal.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                if (checkBoxSensor1.isSelected()) {
                    CloseableHttpClient httpClient = HttpClients.createDefault();
                    HttpGet httpGet = new HttpGet("http://localhost:8080/sensor/1");
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
                        GuiModel.this.referenceIdealValueSensor1 = jsonb.fromJson(sensorJson, Sensor.class);
                        System.out.println(GuiModel.this.referenceIdealValueSensor1.getSensorValue());
                        EntityUtils.consume(entity);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    GuiModel.this.isConfigIdeal1 = true;
                }
                if (checkBoxSensor2.isSelected()) {
                    CloseableHttpClient httpClient = HttpClients.createDefault();
                    HttpGet httpGet = new HttpGet("http://localhost:8080/sensor/2");
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
                        GuiModel.this.referenceIdealValueSensor2 = jsonb.fromJson(sensorJson, Sensor.class);
                        System.out.println(GuiModel.this.referenceIdealValueSensor2.getSensorValue());
                        EntityUtils.consume(entity);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    GuiModel.this.isConfigIdeal2 = true;
                }
                if (checkBoxSensor3.isSelected()) {
                    CloseableHttpClient httpClient = HttpClients.createDefault();
                    HttpGet httpGet = new HttpGet("http://localhost:8080/sensor/3");
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
                        GuiModel.this.referenceIdealValueSensor3 = jsonb.fromJson(sensorJson, Sensor.class);
                        System.out.println(GuiModel.this.referenceIdealValueSensor3.getSensorValue());
                        EntityUtils.consume(entity);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    GuiModel.this.isConfigIdeal3 = true;
                }
                if (checkBoxSensor4.isSelected()) {
                    CloseableHttpClient httpClient = HttpClients.createDefault();
                    HttpGet httpGet = new HttpGet("http://localhost:8080/sensor/4");
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
                        GuiModel.this.referenceIdealValueSensor4 = jsonb.fromJson(sensorJson, Sensor.class);
                        System.out.println(GuiModel.this.referenceIdealValueSensor4.getSensorValue());
                        EntityUtils.consume(entity);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    GuiModel.this.isConfigIdeal4 = true;
                }
            }
        });

        /**
         * Run Tab
         */
        GridPane gridRunTap = new GridPane();
        gridRunTap.setAlignment(Pos.CENTER);
        gridRunTap.setHgap(10);
        gridRunTap.setVgap(10);
        gridRunTap.setPadding(new Insets(25, 25, 25, 25));

        gridRunTap.getStylesheets().add("userinterface.css");

        Label runTabAutoIrrigationLabel = new Label("If you chosse 'Auto Irrigation' the software uses the configured values to automatically water your plants.");
        runTabAutoIrrigationLabel.setId("runTabAutoIrrigationLabel");
        runTabAutoIrrigationLabel.setWrapText(true);
        HBox hbRunTabAutoIrrigationLabel = new HBox(10);
        hbRunTabAutoIrrigationLabel.setAlignment(Pos.CENTER);
        hbRunTabAutoIrrigationLabel.getChildren().add(runTabAutoIrrigationLabel);
        gridRunTap.add(hbRunTabAutoIrrigationLabel, 1, 1, 3, 1);

        Label runTabDropInfoLabel = new Label("If 'Drop Irrigation' is chosen, the plants get a small but steady flow of fresh water.");
        runTabDropInfoLabel.setId("runTabDropInfoLabel");
        runTabDropInfoLabel.setWrapText(true);
        HBox hbRunTabDropInfoLabel = new HBox(10);
        hbRunTabDropInfoLabel.setAlignment(Pos.CENTER);
        hbRunTabDropInfoLabel.getChildren().add(runTabDropInfoLabel);
        gridRunTap.add(hbRunTabDropInfoLabel, 1, 2, 3, 1);

        Label runTabManualInfoLabel = new Label("'Manual Start/Stop' allows you to manually water your plants.");
        runTabManualInfoLabel.setId("runTabManualInfoLabel");
        runTabManualInfoLabel.setWrapText(true);
        HBox hbRunTabManualInfoLabel = new HBox(10);
        hbRunTabManualInfoLabel.setAlignment(Pos.CENTER);
        hbRunTabManualInfoLabel.getChildren().add(runTabManualInfoLabel);
        gridRunTap.add(hbRunTabManualInfoLabel, 1, 3, 3, 1);

        CheckBox runTabCheckBoxSensor1 = new CheckBox("Sensor 1");
        runTabCheckBoxSensor1.setId("runTabCheckBoxSensor1");
        HBox hbRunTabCheckBoxSensor1 = new HBox(10);
        hbRunTabCheckBoxSensor1.setAlignment(Pos.CENTER);
        hbRunTabCheckBoxSensor1.getChildren().add(runTabCheckBoxSensor1);
        gridRunTap.add(hbRunTabCheckBoxSensor1, 0, 4);

        CheckBox runTabCheckBoxSensor2 = new CheckBox("Sensor 2");
        runTabCheckBoxSensor2.setId("runTabCheckBoxSensor2");
        HBox hbRunTabCheckBoxSensor2 = new HBox(10);
        hbRunTabCheckBoxSensor2.setAlignment(Pos.CENTER);
        hbRunTabCheckBoxSensor2.getChildren().add(runTabCheckBoxSensor2);
        gridRunTap.add(hbRunTabCheckBoxSensor2, 1, 4);

        CheckBox runTabCheckBoxSensor3 = new CheckBox("Sensor 3");
        runTabCheckBoxSensor3.setId("runTabCheckBoxSensor3");
        HBox hbRunTabCheckBoxSensor3 = new HBox(10);
        hbRunTabCheckBoxSensor3.setAlignment(Pos.CENTER);
        hbRunTabCheckBoxSensor3.getChildren().add(runTabCheckBoxSensor3);
        gridRunTap.add(hbRunTabCheckBoxSensor3, 2, 4);

        CheckBox runTabCheckBoxSensor4 = new CheckBox("Sensor 4");
        runTabCheckBoxSensor4.setId("runTabCheckBoxSensor4");
        HBox hbRunTabCheckBoxSensor4 = new HBox(10);
        hbRunTabCheckBoxSensor4.setAlignment(Pos.CENTER);
        hbRunTabCheckBoxSensor4.getChildren().add(runTabCheckBoxSensor4);
        gridRunTap.add(hbRunTabCheckBoxSensor4, 3, 4);

        CheckBox runTabCheckBoxIrrigation = new CheckBox("Auto Irrigation");
        runTabCheckBoxIrrigation.setId("runTabCheckBoxAutoIrrigation");
        HBox hbRunTabCheckBoxIrrigation = new HBox(10);
        hbRunTabCheckBoxIrrigation.setAlignment(Pos.CENTER);
        hbRunTabCheckBoxIrrigation.getChildren().add(runTabCheckBoxIrrigation);
        gridRunTap.add(hbRunTabCheckBoxIrrigation, 0, 5);

        CheckBox runTabCheckBoxDropIrrigation = new CheckBox("Drop Irrigation");
        runTabCheckBoxDropIrrigation.setId("runTabCheckBoxDropIrrigation");
        HBox hbRunTabCheckBoxDropIrrigation = new HBox(10);
        hbRunTabCheckBoxDropIrrigation.setAlignment(Pos.CENTER);
        hbRunTabCheckBoxDropIrrigation.getChildren().add(runTabCheckBoxDropIrrigation);
        gridRunTap.add(hbRunTabCheckBoxDropIrrigation, 1, 5);

        CheckBox runTabCheckBoxManualStart = new CheckBox("Manual Start");
        runTabCheckBoxManualStart.setId("runTabCheckBoxManualStart");
        HBox hbRunTabCheckBoxManualStart = new HBox(10);
        hbRunTabCheckBoxManualStart.setAlignment(Pos.CENTER);
        hbRunTabCheckBoxManualStart.getChildren().add(runTabCheckBoxManualStart);
        gridRunTap.add(hbRunTabCheckBoxManualStart, 2, 5);

        CheckBox runTabCheckBoxManualStop = new CheckBox("Manual Stop");
        runTabCheckBoxManualStop.setId("runTabCheckBoxManualStop");
        HBox hbRunTabCheckBoxManualStop = new HBox(10);
        hbRunTabCheckBoxManualStop.setAlignment(Pos.CENTER);
        hbRunTabCheckBoxManualStop.getChildren().add(runTabCheckBoxManualStop);
        gridRunTap.add(hbRunTabCheckBoxManualStop, 3, 5);

        Button runTabStartButton = new Button("Run Homegarden Irrigation System");
        runTabStartButton.setId("runTabStartButton");
        HBox hbRunTabStartButton = new HBox(10);
        hbRunTabStartButton.setAlignment(Pos.CENTER);
        hbRunTabStartButton.getChildren().add(runTabStartButton);
        gridRunTap.add(hbRunTabStartButton, 0, 6, 4, 1);

        runTabStartButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                if (runTabCheckBoxIrrigation.isSelected()) {
                    if (runTabCheckBoxSensor1.isSelected()) {
                        new MultiThreadManager(GuiModel.this.referenceDryValueSensor1, GuiModel.this.referenceIdealValueSensor1, true).start();
                    }
                    if (runTabCheckBoxSensor2.isSelected()) {
                        new MultiThreadManager(GuiModel.this.referenceDryValueSensor2, GuiModel.this.referenceIdealValueSensor2, true).start();
                    }
                    if (runTabCheckBoxSensor3.isSelected()) {
                        new MultiThreadManager(GuiModel.this.referenceDryValueSensor3, GuiModel.this.referenceIdealValueSensor3, true).start();
                    }
                    if (runTabCheckBoxSensor4.isSelected()) {
                        new MultiThreadManager(GuiModel.this.referenceDryValueSensor4, GuiModel.this.referenceIdealValueSensor4, true).start();
                    }
                } else if (runTabCheckBoxDropIrrigation.isSelected()) {
                    if (runTabCheckBoxSensor1.isSelected()) {
                        System.out.println("Drop Irrigation selsected, Sensor 1");
                    }
                    if (runTabCheckBoxSensor2.isSelected()) {
                        System.out.println("Drop Irrigation selsected, Sensor 2");
                    }
                    if (runTabCheckBoxSensor3.isSelected()) {
                        System.out.println("Drop Irrigation selsected, Sensor 3");
                    }
                    if (runTabCheckBoxSensor4.isSelected()) {
                        System.out.println("Drop Irrigation selsected, Sensor 4");
                    }
                } else if (runTabCheckBoxManualStart.isSelected()) {
                    if (runTabCheckBoxSensor1.isSelected()) {
                        CloseableHttpClient httpClient = HttpClients.createDefault();
                        HttpGet httpGet = new HttpGet("http://192.168.0.11:8080/relay/" + "/start/" + 1);
                        try {
                            CloseableHttpResponse response = httpClient.execute(httpGet);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    if (runTabCheckBoxSensor2.isSelected()) {
                        CloseableHttpClient httpClient = HttpClients.createDefault();
                        HttpGet httpGet = new HttpGet("http://192.168.0.11:8080/relay/" + "/start/" + 2);
                        try {
                            CloseableHttpResponse response = httpClient.execute(httpGet);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    if (runTabCheckBoxSensor3.isSelected()) {
                        CloseableHttpClient httpClient = HttpClients.createDefault();
                        HttpGet httpGet = new HttpGet("http://192.168.0.11:8080/relay/" + "/start/" + 3);
                        try {
                            CloseableHttpResponse response = httpClient.execute(httpGet);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    if (runTabCheckBoxSensor4.isSelected()) {
                        CloseableHttpClient httpClient = HttpClients.createDefault();
                        HttpGet httpGet = new HttpGet("http://192.168.0.11:8080/relay/" + "/start/" + 4);
                        try {
                            CloseableHttpResponse response = httpClient.execute(httpGet);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }else if (runTabCheckBoxManualStop.isSelected()) {
                    if (runTabCheckBoxSensor1.isSelected()) {
                        CloseableHttpClient httpClient = HttpClients.createDefault();
                        HttpGet httpGet = new HttpGet("http://192.168.0.11:8080/relay/" + "/stop/" + 1);
                        try {
                            CloseableHttpResponse response = httpClient.execute(httpGet);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    if (runTabCheckBoxSensor2.isSelected()) {
                        CloseableHttpClient httpClient = HttpClients.createDefault();
                        HttpGet httpGet = new HttpGet("http://192.168.0.11:8080/relay/" + "/stop/" + 2);
                        try {
                            CloseableHttpResponse response = httpClient.execute(httpGet);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    if (runTabCheckBoxSensor3.isSelected()) {
                        CloseableHttpClient httpClient = HttpClients.createDefault();
                        HttpGet httpGet = new HttpGet("http://192.168.0.11:8080/relay/" + "/stop/" + 3);
                        try {
                            CloseableHttpResponse response = httpClient.execute(httpGet);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    if (runTabCheckBoxSensor4.isSelected()) {
                        CloseableHttpClient httpClient = HttpClients.createDefault();
                        HttpGet httpGet = new HttpGet("http://192.168.0.11:8080/relay/" + "/stop/" + 4);
                        try {
                            CloseableHttpResponse response = httpClient.execute(httpGet);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        });


        /**
        *  Set GridPane Lines setupTab Visible
        */
        gridSetupTab.setGridLinesVisible(false);
        /**
         * Set GridPane Lines of runTab Visible
         */
        gridRunTap.setGridLinesVisible(false);

        /**
         * Create Tab Controls
         */
        Tab tabSetup = new Tab("Setup", gridSetupTab);
        Tab tabRun = new Tab("Run", gridRunTap);
        //Tab tabData = new Tab("Data", new Label("Get Irrigation Data"));
        /**
         * Add Tabs to TabPane
         */
        tabPane.getTabs().add(tabSetup);
        tabPane.getTabs().add(tabRun);
        //tabPane.getTabs().add(tabData);
        /**
         * Create VBox
         */
        VBox vBox = new VBox(tabPane);
        /**
         * Create Scene and add VBox
         */
        Scene scene = new Scene(vBox, 500, 350);

        /**
         * add 'userinterface.css' to the scene
        */
        scene.getStylesheets().add("userinterface.css");
        /**
        * make stage non resizable
         * */
        stage.setResizable(false);
        /**
         * Add Scene to Stage
         */
        stage.setScene(scene);

        stage.setTitle("Homegarden Irrigation");
        /**
         * Male Stage Visible
         */
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
