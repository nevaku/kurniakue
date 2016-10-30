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

        /**
         * Auto generated from Prop
         */
        TransactionID, 
        /**
         * Date of transaction yyyy-MM-dd
         */
        Date, 
        /**
         * Information about the transaction
         */
        Description, 
        /**
         * ID of customer from {@link Customer$F#CustomerID}
         */
        CustomerID, 
        /**
         * {@link Customer$F#CustomerName} at the transaction done.
         * This will not changed when customer change its name
         */
        CustomerName,
        /**
         * {@link Item$F#ItemNo}
         */
        ItemNo, 
        /**
         * {@link Item$F#ItemName} at the transaction done.
         * This will not changed when item's name is modified
         */
        ItemName, 
        /**
         * {@link Item$F#Price} at the transaction done.
         * This will not changed when item's price is modified
         */
        Price,
        /**
         * The number of items in transaction
         */
        Count, 
        /**
         * The amount of transactions = Price x Count 
         */
        Amount, 
        /**
         * From supplier perspective. 
         * 1 mean debit to supplier
         * -1 mean credit from supplier
         */
        DCFlag;
        
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
