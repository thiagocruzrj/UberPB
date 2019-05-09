package com.study.uberpb.config;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ConfiguracaoFirebase {
    private static DatabaseReference database;
    private static FirebaseAuth auth;

    // returning FirebaseDatabase instance
    public static DatabaseReference getFirebaseDatabase(){
        if( database == null){
            database = FirebaseDatabase.getInstance().getReference();
        }
        return database;
    }

    // returning FirebaseAuth instance
    public static FirebaseAuth getFirebaseAutenticacao(){
        if(auth == null){
            auth = FirebaseAuth.getInstance();
        }
        return auth;
    }
}
