package com.study.uberpb.activity;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;
import com.study.uberpb.R;
import com.study.uberpb.config.ConfiguracaoFirebase;
import com.study.uberpb.helper.Permissoes;
import com.study.uberpb.helper.UsuarioFirebase;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth autenticacao;
    private String[] permissoes = new String[]{
            Manifest.permission.ACCESS_FINE_LOCATION
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportActionBar().hide();

        // Validation permissions
        Permissoes.validarPermissoes(permissoes, this, 1);

        autenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao();
        autenticacao.signOut();
    }

    public void abrirTelaLogin(View view){
        startActivity( new Intent(this, LoginActivity.class));
    }
    public void abrirTelaRegister(View view){
        startActivity( new Intent(this, RegisterActivity.class));
    }

    @Override
    protected void onStart() {
        super.onStart();
        UsuarioFirebase.redirecionaLoggedUser(MainActivity.this);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        for(int permissãoResultado: grantResults){
            if(permissãoResultado == PackageManager.PERMISSION_DENIED){
                alertaValidacaoPermissao();
            }
        }
    }
    private void alertaValidacaoPermissao(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Permissões Negadas");
        builder.setMessage("Necessário aceitar as permissões para utilizar o app!");
        builder.setCancelable(false);
        builder.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
