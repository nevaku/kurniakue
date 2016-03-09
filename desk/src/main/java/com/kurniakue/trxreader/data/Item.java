package com.kurniakue.trxreader.data;

import com.kurniakue.common.EnumField;
import com.mongodb.DBObject;

/**
 *
 * @author harun1
 */
public class Item extends Record implements Comparable<Item> {

    public enum F implements EnumField {

        ID, ItemNo, ItemName, Description, Price, Modal;
    }

    public Item() {

    }

    public Item(DBObject dbobject) {
        putAll(dbobject.toMap());
    }

    @Override
    public String toString() {
        return "{" + getString(F.ID) + ", "
                + getString(F.ItemNo) + ", "
                + getString(F.ItemName) + ", "
                + getString(F.Description) + ", "
                + getString(F.Price) + ", "
                + getString(F.Modal) + "}\n";
    }

    @Override
    public int compareTo(Item item) {
        if (item == null) {
            return 1;
        } else {
            return getString(F.ItemNo).compareTo(item.getString(F.ItemNo));
        }
    }

    public ItemD load() {
        ItemD data = Persistor.get(ItemD.class);
        data.find(getString(F.ItemNo), this);

        return data;
    }

    public String store(ItemD data) {
        data.persist(this);

        String id = this.getString(F.ID);
        return id;
    }
}
