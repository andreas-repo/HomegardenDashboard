package org.homegarden.gui.logic;

import org.homegarden.gui.model.Sensor;

public class MultiThreadManager extends Thread {
    private Sensor dry;
    private Sensor ideal;
    private boolean running;
    private Humidification humidification;

    public MultiThreadManager(Sensor dry, Sensor ideal, boolean running) {
        this.dry = dry;
        this.ideal = ideal;
        this.running = running;
    }

    @Override
    public void run() {
        try {
            this.humidification = new Humidification(this.dry, this.ideal, this.running);
            this.humidification.wateringLogic(true);

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public boolean isRunning() {
        return running;
    }

    public void setRunning(boolean running) {
        this.running = running;
    }

    public Humidification getHumidification() {
        return humidification;
    }

    public void setHumidification(Humidification humidification) {
        this.humidification = humidification;
    }
}
