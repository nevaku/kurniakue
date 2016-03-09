/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kurniakue.trxreader;

import com.kurniakue.common.EnumField;
import com.kurniakue.trxreader.data.Record;

/**
 *
 * @author harun1
 */
public class Saldo extends Record {

    public enum F implements EnumField {

        ID, CustomerName, Date, Amount
    }
}
