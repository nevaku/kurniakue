/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kurniakue.trxreader.data;

import com.kurniakue.common.EnumField;

/**
 *
 * @author harun1
 */
public class Transaction extends Record {

    public enum F implements EnumField {

        TransactionID, Date, Category, Subcategory, Description, 
        CustomerID, CustomerName,
        ItemNo, ItemName, Price,
        Count, Amount, DCFlag;
        
        public String fieldName;

        private F(String fieldName) {
            this.fieldName = fieldName;
        }

        private F() {
            this.fieldName = name();
        }
        
    }
    
    
    private static F[] all;
    public static F f(String fieldName){
        if (all == null){
            all = Transaction.F.values();
        }
        for (F f : all) {
            if (f.fieldName.equals(fieldName)){
                return f;
            }
        }
        return null;
    }
}
