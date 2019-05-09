package com.study.uberpb.activity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Switch;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.study.uberpb.R;
import com.study.uberpb.config.ConfiguracaoFirebase;
import com.study.uberpb.helper.UsuarioFirebase;
import com.study.uberpb.model.User;

public class RegisterActivity extends AppCompatActivity {

    private TextInputEditText campoNome, campoEmail, campoSenha;
    private Switch switchTipoUsuario;
    private FirebaseAuth autenticacao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        campoNome = findViewById(R.id.editRegisterName);
        campoEmail = findViewById(R.id.editRegisterEmail);
        campoSenha = findViewById(R.id.editRegisterPassword1);
        switchTipoUsuario = findViewById(R.id.switchUserType);
    }

    public void validarUserRegister(View view){
        // Recovering fields text
        String textoNome = campoNome.getText().toString();
        String textoEmail = campoEmail.getText().toString();
        String textoSenha = campoSenha.getText().toString();

        if(!textoNome.isEmpty()) {
            if(!textoEmail.isEmpty()) {
                if(!textoSenha.isEmpty()) {
                    User user = new User();
                    user.setNome(textoNome);
                    user.setEmail(textoEmail);
                    user.setSenha(textoSenha);
                    user.setTipo(verificarUserType());

                    registerUser(user);

                }else {
                    Toast.makeText(RegisterActivity.this, "Preencha o nome!", Toast.LENGTH_SHORT).show();
                }
            }else {
                Toast.makeText(RegisterActivity.this, "Preencha o E-mail!", Toast.LENGTH_SHORT).show();
            }
        }else {
            Toast.makeText(RegisterActivity.this, "Preencha a senha!", Toast.LENGTH_SHORT).show();
        }
    }

    public void registerUser(final User user) {
        autenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao();
        autenticacao.createUserWithEmailAndPassword(
                user.getEmail(),
                user.getSenha()
        ).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    try{
                        String idUser = task.getResult().getUser().getUid();
                        user.setId( idUser );
                        user.save();

                        // Att UserProfile name
                        UsuarioFirebase.attUserName( user.getNome());

                        // Redirect the user with type base
                        // If the user be a passenger we'll call the activity maps
                        // Else we'll call the activity requests
                        if( verificarUserType() == "P"){
                            startActivity(new Intent(RegisterActivity.this, MapsActivity.class));
                            finish();
                            Toast.makeText(RegisterActivity.this, "Sucesso ao cadastrar o Passageiro!",
                                    Toast.LENGTH_SHORT).show();
                        }else {
                            startActivity(new Intent(RegisterActivity.this, RequestsActivity.class));
                            finish();
                            Toast.makeText(RegisterActivity.this, "Sucesso ao cadastrar o Motorista!",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }else{
                    String excecao = "";
                    try{
                        throw task.getException();
                    }catch (FirebaseAuthWeakPasswordException e){
                        excecao = "Digite uma senha mais forte!";
                    }catch (FirebaseAuthInvalidCredentialsException e){
                        excecao = "Digite um E-mail válido!";
                    }catch (FirebaseAuthUserCollisionException e){
                        excecao = "Essa conta já foi cadastrada!";
                    }catch (Exception e){
                        excecao = "Erro ao cadastrar usuário: " + e.getMessage();
                        e.printStackTrace();
                    }
                    Toast.makeText(RegisterActivity.this, excecao,
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public String verificarUserType(){
        return switchTipoUsuario.isChecked() ? "M" : "P";
    }
}
