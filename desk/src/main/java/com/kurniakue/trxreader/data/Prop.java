package com.kurniakue.trxreader.data;

import com.kurniakue.common.EnumField;
import com.mongodb.DBObject;

/**
 *
 * @author harun1
 */
public class Prop extends Record implements Comparable<Prop> {

    public enum F implements EnumField {

        PropertyName, PropertyValue, Description;
    }

    public enum N implements EnumField {
        LastTransactionID, LastDate, LastItem, LastCustomer
    }

    public Prop() {

    }

    public Prop(DBObject dbobject) {
        putAll(dbobject.toMap());
    }

    @Override
    public String toString() {
        return "{" + getString(F.PropertyName) + ", "
                + getString(F.PropertyValue) + ", "
                + getString(F.Description) + "}\n";
    }

    @Override
    public int compareTo(Prop prop) {
        if (prop == null) {
            return 1;
        } else {
            return getString(F.PropertyName).compareTo(prop.getString(F.PropertyName));
        }
    }

    public PropD load() {
        PropD data = Persistor.get(PropD.class);
        data.find(getString(F.PropertyName), this);

        return data;
    }

    public String store(PropD data) {
        data.persist(this);

        String id = this.getString(F.PropertyName);
        return id;
    }

    public static String tstr(N propName) {
        Prop prop = PropD.get().find(propName, new Prop());
        return prop.tstr(Prop.F.PropertyValue);
    }

    public static int getInt(N propName) {
        Prop prop = PropD.get().find(propName, new Prop());
        return prop.getInt(Prop.F.PropertyValue);
    }

    public static void store(N propName, Object value) {
        Prop prop = PropD.get().find(propName, new Prop());
        prop.put(F.PropertyName, propName.name());
        prop.put(F.PropertyValue, value);
        PropD.get().persist(prop);
    }

    public static int getNewTransactionId() {
        int trxId = Prop.getInt(N.LastTransactionID);
        if (trxId < 0) {
            trxId = 0;
        }
        trxId += 1;
        Prop.store(N.LastTransactionID, trxId);
        return trxId;
    }
}
