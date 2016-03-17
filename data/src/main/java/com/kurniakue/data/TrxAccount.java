/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kurniakue.data;

import com.kurniakue.common.EnumField;

/**
 *
 * @author Harun Al Rasyid
 */
public class TrxAccount extends Record<Transaction> {
    
    
    public enum F implements EnumField {
        AccountNo, AccountName, Amount, DCFlag
    }

    @Override
    public EnumField getKey() {
        return F.AccountNo;
    }

    @Override
    public String toString() {
        return "{" + getString(F.AccountNo) + ", "
                + getString(F.AccountName) + ", "
                + getString(F.Amount) + ", "
                + getString(F.DCFlag) + "}";
    }

    @Override
    public Transaction getNoRecord() {
        return null;
    }
}
