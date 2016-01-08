package org.kles.fx.custom;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.concurrent.TimeUnit;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.event.ActionEvent;
import javafx.scene.control.Label;
import javafx.util.Duration;

/**
 *
 * @author jeremy.chaut
 */
public class DigitalClockLabel extends Label {

    private DigitalClock digitalClock;

    public DigitalClockLabel(int type) {
        this(type, null);
    }

    public DigitalClockLabel(int type, String format) {
        digitalClock = new DigitalClock(type, format);
        this.textProperty().bind(digitalClock.getTimeText());
    }

    public DigitalClock getClock() {
        return digitalClock;
    }

    public void start() {
        digitalClock.start();
    }

    public void stop() {
        digitalClock.stop();
    }
}
