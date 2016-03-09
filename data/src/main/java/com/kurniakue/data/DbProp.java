package com.kurniakue.data;

import static com.kurniakue.common.Common.tint;
import com.kurniakue.common.EnumField;
import static com.kurniakue.data.KurniaKueDb.getDbCollection;
import com.mongodb.client.MongoCollection;
import java.util.Map;
import org.bson.Document;

/**
 *
 * @author harun1
 */
public class DbProp extends Record<DbProp> implements Comparable<DbProp> {

    public enum F implements EnumField {

        PropertyName, PropertyValue, Description;
    }

    public enum N implements EnumField {
        offset, LastTransactionID, LastDate, LastItem, LastCustomer
    }
    
    public static final String COLLECTION_NAME = "props";
    public static final DbProp NO_PROP = new DbProp();

    public DbProp() {

    }

    @Override
    public EnumField getKey() {
        return F.PropertyName;
    }

    public DbProp(Map<String, Object> dbobject) {
        putAll(dbobject);
    }

    @Override
    public String toString() {
        return "{" + getString(F.PropertyName) + ", "
                + getString(F.PropertyName) + ", "
                + getString(F.Description) + "}\n";
    }

    @Override
    public int compareTo(DbProp record) {
        if (record == null) {
            return 1;
        } else {
            return getString(F.PropertyName).compareTo(record.getString(F.PropertyName));
        }
    }

    @Override
    public MongoCollection<Document> getCollection() {
        return getDbCollection(COLLECTION_NAME);
    }

    @Override
    public DbProp getNoRecord() {
        return NO_PROP;
    }

    public static Object getProp(EnumField property) {
        DbProp prop = new DbProp();
        prop.load(property.name());
        if (!prop.isExists()) {
            return null;
        }

        return prop.get(DbProp.F.PropertyValue);

    }

    public static int getPropInt(EnumField property) {
        return tint(getProp(property));
    }

    public static void setProp(EnumField property, Object propertyValue) {
        DbProp prop = new DbProp().load(property.name());
        prop.put(DbProp.F.PropertyName, property.name());
        prop.put(DbProp.F.PropertyValue, propertyValue);
        prop.store();
    }
    
    public static int getAndInc(EnumField property) {
        int value = getPropInt(property);
        value += 1;
        setProp(property, value);
        return value;
    }
}
