package org.kles.model;

import java.util.ArrayList;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.LongProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Account extends AbstractDataModel {

    private final StringProperty email;
    private final StringProperty name;
    private final LongProperty size;
    private final LongProperty quota;
    private final IntegerProperty countEmail;
    private final IntegerProperty inEmail;
    private final ObjectProperty<Responder> responder;

    public Account() {
        this("");

    }

    public Account(String name) {
        super("Account");
        this.email = new SimpleStringProperty("");
        this.name = new SimpleStringProperty("");
        this.size = new SimpleLongProperty(1);
        this.quota = new SimpleLongProperty(1);
        this.countEmail = new SimpleIntegerProperty();
        this.inEmail = new SimpleIntegerProperty();
        this.responder = new SimpleObjectProperty<>();
    }

    @Override
    public ArrayList<?> extractData() {
        ArrayList a = new ArrayList();
        a.add(email.get());
        a.add(name.get());
        a.add(size.get());
        a.add(quota.get());
        a.add(countEmail.get());
        a.add(inEmail.get());
        return a;
    }

    @Override
    public void populateData(ArrayList<?> data) {
        if (data != null) {
            if (data.size() == 6) {
                email.set((String) data.get(0));
                name.set((String) data.get(1));
                size.set((Long) data.get(2));
                quota.set((Long) data.get(3));
                countEmail.set((Integer) data.get(4));
                inEmail.set((Integer) data.get(5));
            }
        }
    }

    @Override
    public String toString() {
        return email.get();
    }

    @Override
    public AbstractDataModel newInstance() {
        return new Account();
    }

    public String getEmail() {
        return email.get();
    }

    public void setEmail(String name) {
        this.email.set(name);
    }

    public StringProperty getEmailProperty() {
        return this.email;
    }

    public String getName() {
        return name.get();
    }

    public void setName(String ip) {
        this.name.set(ip);
    }

    public StringProperty getNameProperty() {
        return this.name;
    }

    public long getSize() {
        return size.get();
    }

    public void setSize(long size) {
        this.size.set(size);
    }

    public LongProperty getSizeProperty() {
        return this.size;
    }

    public long getQuota() {
        return quota.get();
    }

    public void setQuota(long size) {
        this.quota.set(size);
    }

    public LongProperty getQuotaProperty() {
        return this.quota;
    }

    public Integer getCountEmail() {
        return countEmail.get();
    }

    public void setCountEmail(int count) {
        this.countEmail.set(count);
    }

    public IntegerProperty getCountEmailProperty() {
        return this.countEmail;
    }

    public Integer getInEmail() {
        return inEmail.get();
    }

    public void setInEmail(int count) {
        this.inEmail.set(count);
    }

    public IntegerProperty getInEmailProperty() {
        return this.inEmail;
    }

    public Responder getResponder() {
        return responder.get();
    }

    public void setResponderProperty(Responder responder) {
        this.responder.set(responder);
    }

    public ObjectProperty<Responder> getResponderProperty() {
        return responder;
    }

}
