/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kurniakue.common;

import java.lang.reflect.Field;

/**
 *
 * @author harun1
 */
public class MemberFinder {
    private String name;
    private Object object;

    public MemberFinder(String name) {
        this.name = name;
    }

    public MemberFinder of(Object object) {
        if (object == null) {
            return this;
        }
        
        this.object = object;
        
        return this;
    }

    public String getString() {
        return String.valueOf(value());
    }
    
    public Object value() {
        if (object == null) {
            return null;
        }
        
        try {
            Field field = object.getClass().getField(name);
            return field.get(object);
        } catch (Exception exc) {
            exc.printStackTrace();
            return null;
        }
    }
}
