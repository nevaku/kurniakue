/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kurniakue.data;

import java.io.FileInputStream;
import java.util.Properties;

/**
 *
 * @author harun1
 */
public class TheConfig extends Properties {
    public void load(String fileName)
    {
        try {
            load(new FileInputStream(fileName));
        } catch (Exception e) {
            e.printStackTrace();
        }
        
    }

    public int getInt(String key) {
        try {
            return Integer.parseInt(getProperty(key));
        } catch (Exception e) {
            return 0;
        }
    }
}
