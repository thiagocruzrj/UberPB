package com.study.uberpb.helper;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.study.uberpb.activity.MapsActivity;
import com.study.uberpb.activity.RegisterActivity;
import com.study.uberpb.activity.RequestsActivity;
import com.study.uberpb.config.ConfiguracaoFirebase;
import com.study.uberpb.model.User;

public class UsuarioFirebase {

    public static FirebaseUser getUsuarioAtual(){
        FirebaseAuth usuario = ConfiguracaoFirebase.getFirebaseAutenticacao();
        return usuario.getCurrentUser();
    }
    public static boolean attUserName(String name){
        try{
            FirebaseUser user = getUsuarioAtual();
            UserProfileChangeRequest profile = new UserProfileChangeRequest.Builder()
                    .setDisplayName( name )
                    .build();
            user.updateProfile(profile).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if( !task.isSuccessful()){
                        Log.d("Perfil", "Erro ao atualizar nome do Perfil");
                    }
                }
            });
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }
    public static void redirecionaLoggedUser(final Activity activity){
        FirebaseUser user = getUsuarioAtual();
        if(user != null){
            DatabaseReference userRef = ConfiguracaoFirebase.getFirebaseDatabase()
                    .child("users")
                    .child(getUserId());
            userRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    User user = dataSnapshot.getValue(User.class);
                    String userType = user.getTipo();
                    if(userType.equals("M")){
                        Intent i = new Intent(activity, RequestsActivity.class);
                        activity.startActivity(i);
                    }else{
                        Intent i = new Intent(activity, MapsActivity.class);
                        activity.startActivity(i);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }

    }
    public static String getUserId(){
        return getUsuarioAtual().getUid();
    }
}
