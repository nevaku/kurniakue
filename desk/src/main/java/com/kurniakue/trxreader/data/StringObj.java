/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kurniakue.trxreader.data;

import com.kurniakue.common.EnumField;
import java.util.Objects;

/**
 *
 * @author harun1
 */
public class StringObj {
    private final Record record;
    private final EnumField field;

    public StringObj(Record record, EnumField field) {
        this.record = record;
        this.field = field;
    }

    @Override
    public String toString() {
        if (record == null)
        {
            return null;
        }
        
        if (field == null)
        {
            return record.toString();
        }
        return record.getString(field);
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof StringObj)) {
            return super.equals(obj);
        }
        
        StringObj compared = (StringObj) obj;
        return toString().equals(compared.toString());
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 71 * hash + Objects.hashCode(this.record);
        hash = 71 * hash + Objects.hashCode(this.field);
        return hash;
    }

    public Record getRecord() {
        return record;
    }
    
}
