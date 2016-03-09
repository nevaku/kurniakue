/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kurniakue.common;

/**
 *
 * @author harun1
 */
public class DefaultEnumField implements EnumField {
    
    private final String name;

    public DefaultEnumField(String name) {
        this.name = name;
    }

    @Override
    public String name() {
        return name;
    }

    @Override
    public int ordinal() {
        return 0;
    }
    
}
