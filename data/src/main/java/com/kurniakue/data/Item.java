package com.kurniakue.data;

import com.kurniakue.common.EnumField;
import com.mongodb.client.MongoCollection;
import java.util.List;
import java.util.Map;
import org.bson.Document;
import static com.kurniakue.data.KurniaKueDb.getDbCollection;

/**
 *
 * @author harun1
 */
public class Item extends Record<Item> implements Comparable<Item> {


    public enum F implements EnumField {

        ItemNo, ItemName, Description, Price, Modal, State,
        ItemType;
    }

    public static final String COLLECTION_NAME = "items";
    public static final Item NO_ITEM = new Item();

    public Item() {

    }

    @Override
    public EnumField getKey() {
        return F.ItemNo;
    }

    public Item(Map<String, Object> dbobject) {
        putAll(dbobject);
    }

    @Override
    public String toString() {
        return "{" + getString(F.ItemNo) + ", "
                + getString(F.ItemName) + ", "
                + getString(F.Price) + "}\n";
    }

    @Override
    public int compareTo(Item record) {
        if (record == null) {
            return 1;
        } else {
            return getString(F.ItemNo).compareTo(record.getString(F.ItemNo));
        }
    }

    @Override
    public Item getNoRecord() {
        return NO_ITEM;
    }

    @Override
    public MongoCollection<Document> getCollection() {
        return getDbCollection(COLLECTION_NAME);
    }

    public List<Item> getProductList() {
        Document filter = new Document()
                .append(F.State.name(),
                        new Document("$ne", "NA"))
                .append(F.ItemType.name(), "product");
        Document sort = new Document(F.ItemName.name(), 1);
        return loadList(this, filter, sort);
    }

    public List<Item> getAllItems() {
        Document filter = new Document()
                .append(F.State.name(),
                        new Document("$ne", "NA"));
        Document sort = new Document(F.ItemName.name(), 1);
        return loadList(this, filter, sort);
    }

    public List<Item> getItemList(String keyword) {
        Document filter = new Document()
                .append(F.ItemName.name(),
                        new Document("$regex", ".*" + keyword + ".*")
                        .append("$options", "i"))
                .append(F.State.name(),
                        new Document("$ne", "NA"));

        Document sort = new Document(F.ItemName.name(), 1);

        List<Item> list = Record.loadList(this, filter, sort);
        return list;
    }
}
