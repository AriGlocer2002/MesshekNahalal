package com.example.messheknahalal.Utils;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class DataBaseHelper {

    private FirebaseDatabase db;
    private DatabaseReference ref;
    private FirebaseAuth auth;

    public DataBaseHelper(String dataList){
        this.db = FirebaseDatabase.getInstance();
        this.ref = db.getReference(dataList);
    }

    public DataBaseHelper(FirebaseDatabase db, DatabaseReference ref) {
        this.db = db;
        this.ref = ref;
    }

    public FirebaseDatabase getDb() {
        return db;
    }

    public void setDb(FirebaseDatabase db) {
        this.db = db;
    }

    public DatabaseReference getRef() {
        return ref;
    }

    public void setRef(DatabaseReference ref) {
        this.ref = ref;
    }


    public FirebaseAuth getAuth() {
        return auth;
    }
}
