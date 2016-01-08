/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.kles.model;

import java.util.ArrayList;

/**
 *
 * @author JCHAUT
 */
public interface IDataModelManager {

    public ArrayList<?> extractData();

    public void populateData(ArrayList<?> data);

    public AbstractDataModel newInstance();
}
