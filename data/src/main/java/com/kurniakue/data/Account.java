/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kurniakue.data;

import com.kurniakue.common.EnumField;
import static com.kurniakue.data.KurniaKueDb.getDbCollection;
import com.mongodb.client.MongoCollection;
import org.bson.Document;

/**
 *
 * @author Harun Al Rasyid
 */
public class Account extends Record<Transaction> {
    
    
    public enum F implements EnumField {
        AccountNo, AccountName, AccountGroup
    }

    public static final String COLLECTION_NAME = "accounts";

    @Override
    public EnumField getKey() {
        return F.AccountNo;
    }

    @Override
    public String toString() {
        return "{" + getString(F.AccountNo) + ", "
                + getString(F.AccountName) + ", "
                + getString(F.AccountGroup) + "}";
    }

    @Override
    public Transaction getNoRecord() {
        return null;
    }

    @Override
    public MongoCollection<Document> getCollection() {
        return getDbCollection(COLLECTION_NAME);
    }
}
