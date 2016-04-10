/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kurniakue.trxreader.data;

import com.kurniakue.common.Common;
import com.kurniakue.common.EnumField;
import com.kurniakue.common.Tool;
import java.util.HashMap;

/**
 *
 * @author harun1
 */
public class Record extends HashMap<String, Object> {

    public Object put(EnumField key, Object value) {
        return super.put(String.valueOf(key), value);
    }

    public Object get(EnumField key) {
        return super.get(String.valueOf(key));
    }

    public boolean containsKey(EnumField key) {
        return super.containsKey(String.valueOf(key));
    }

    public Object remove(EnumField key) {
        return super.remove(String.valueOf(key));
    }

    public String getString(EnumField key) {
        return String.valueOf(get(key));
    }

    public String getStringNumber(EnumField key) {
        int val = getInt(key);
        return Tool.formatNumber(val);
    }

    public String tstr(EnumField key) {
        return Tool.tstr(get(key));
    }

    public int getInt(EnumField key) {
        return Tool.tint(get(key));
    }

    @Override
    public String toString() {
        if (isEmpty()) {
            return "[]";
        }
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        for (Entry<String, Object> entrySet : entrySet()) {
            sb.append(entrySet.getKey());
            sb.append(": ");
            sb.append(entrySet.getValue());
            sb.append(", ");
        }
        sb.delete(sb.length() - 2, sb.length() - 1);
        sb.append("]");
        return sb.toString();
    }
}
