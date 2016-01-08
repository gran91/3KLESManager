/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.kles.task;

import javafx.concurrent.Task;
import org.json.simple.JSONObject;
import org.kles.MainApp;
import org.kles.restful.RestFulOVH;

/**
 *
 * @author JCHAUT
 * @param <T>
 */
public class TaskAPIModel<T> extends Task<T> {

    protected MainApp mainApp;
    protected RestFulOVH restful;
    protected int type = 0;
    protected JSONObject inData;

    @Override
    protected T call() throws Exception {
        return null;
    }

    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
        this.restful = this.mainApp.getRestFulOVH();
    }

    public void setRestFulAPI(RestFulOVH ovhApi) {
        this.restful = ovhApi;
    }

    public void setType(int type) {
        this.type = type;
    }

    public void setDataIn(JSONObject in) {
        this.inData = in;
    }
}
