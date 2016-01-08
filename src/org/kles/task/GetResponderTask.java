/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.kles.task;

import java.time.ZonedDateTime;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;
import org.kles.model.Responder;

/**
 *
 * @author JCHAUT
 */
public class GetResponderTask extends TaskAPIModel<JSONObject> {

    @Override
    protected JSONObject call() throws ParseException {
        JSONObject resp = this.mainApp.getRestFulOVH().getResponder();
        Responder respond = null;
        if (resp != null) {
            respond = new Responder();
            respond.setContent(resp.get("content").toString());
            respond.setCopy(Boolean.getBoolean(resp.get("copy").toString()));
            ZonedDateTime from = ZonedDateTime.parse(resp.get("from").toString());
            ZonedDateTime to = ZonedDateTime.parse(resp.get("to").toString());
            respond.setFromDate(from);
            respond.setToDate(to);
        }
        if (mainApp != null) {
            if (mainApp.getAccountData() != null) {
                mainApp.getAccountData().getResponderProperty().set(respond);
            }
        }
        return resp;
    }
}
