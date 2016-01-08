package org.kles.model;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Responder extends AbstractDataModel {

    private final StringProperty content;
    private final BooleanProperty copy;
    private final ObjectProperty<ZonedDateTime> fromDate;
    private final ObjectProperty<ZonedDateTime> toDate;
    private final ObjectProperty<LocalTime> fromTime;
    private final ObjectProperty<LocalTime> toTime;

    public Responder() {
        this("");

    }

    public Responder(String name) {
        super("Responder");
        this.content = new SimpleStringProperty("");
        this.copy = new SimpleBooleanProperty(true);
        this.fromDate = new SimpleObjectProperty<>();
        this.toDate = new SimpleObjectProperty<>();
        this.fromTime = new SimpleObjectProperty<>();
        this.toTime = new SimpleObjectProperty<>();
    }

    @Override
    public ArrayList<?> extractData() {
        ArrayList a = new ArrayList();
        a.add(content.get());
        a.add(copy.get());
        a.add(fromDate.get());
        a.add(fromTime.get());
        a.add(toDate.get());
        a.add(fromTime.get());
        return a;
    }

    @Override
    public void populateData(ArrayList<?> data) {
        if (data != null) {
            if (data.size() == 6) {
                content.set((String) data.get(0));
                copy.set((Boolean) data.get(1));
                fromDate.set((ZonedDateTime) data.get(2));
                fromTime.set((LocalTime) data.get(3));
                toDate.set((ZonedDateTime) data.get(4));
                toTime.set((LocalTime) data.get(5));
            }
        }
    }

    @Override
    public String toString() {
        return content.get();
    }

    @Override
    public AbstractDataModel newInstance() {
        return new Responder();
    }

    public String getContent() {
        return content.get();
    }

    public void setContent(String content) {
        this.content.set(content);
    }

    public StringProperty getContentProperty() {
        return this.content;
    }

    public boolean isCopy() {
        return copy.get();
    }

    public void setCopy(boolean copy) {
        this.copy.set(copy);
    }

    public BooleanProperty getCopyProperty() {
        return this.copy;
    }

    public ZonedDateTime getFromDate() {
        return fromDate.get();
    }

    public void setFromDate(ZonedDateTime from) {
        this.fromDate.set(from);
    }

    public ObjectProperty<ZonedDateTime> getFromDateProperty() {
        return this.fromDate;
    }

    public LocalTime getFromTime() {
        return fromTime.get();
    }

    public void setFromTime(LocalTime from) {
        this.fromTime.set(from);
    }

    public ObjectProperty<LocalTime> getFromProperty() {
        return this.fromTime;
    }

    public ZonedDateTime getToDate() {
        return toDate.get();
    }

    public void setToDate(ZonedDateTime to) {
        this.toDate.set(to);
    }

    public ObjectProperty<ZonedDateTime> getToDateProperty() {
        return this.toDate;
    }

    public LocalTime getToTime() {
        return toTime.get();
    }

    public void setToTime(LocalTime to) {
        this.toTime.set(to);
    }

    public ObjectProperty<LocalTime> getToTimeProperty() {
        return this.toTime;
    }
}
