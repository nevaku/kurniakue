/*
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kurniakue.common;

/**
 *
 * @author harun1
 */
public interface EnumField {
    public String name();
    public int ordinal();
    
    public static class EnumFieldBasic implements EnumField{
        
        private final String name;
        private final int ordinal;

        public EnumFieldBasic(String name, int ordinal) {
            this.name = name;
            this.ordinal = ordinal;
        }
        
        public static final EnumField enumFieldOf(String name) {
            return new EnumFieldBasic(name, 0);
        }

        @Override
        public String name() {
            return name;
        }

        @Override
        public int ordinal() {
            return ordinal;
        }
        
    }
}
