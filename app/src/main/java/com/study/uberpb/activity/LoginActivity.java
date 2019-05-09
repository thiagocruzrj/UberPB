package com.study.uberpb.activity;

import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseUser;
import com.study.uberpb.R;
import com.study.uberpb.config.ConfiguracaoFirebase;
import com.study.uberpb.helper.UsuarioFirebase;
import com.study.uberpb.model.User;

public class LoginActivity extends AppCompatActivity {

    private TextInputEditText campoEmail, campoSenha;
    private FirebaseAuth autenticacao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Initializing components
        campoEmail = findViewById(R.id.editLoginEmail);
        campoSenha = findViewById(R.id.editLoginSenha);
    }

    public void validarUserLogin(View view){
        //Recovering fields text
        String textoEmail = campoEmail.getText().toString();
        String textoSenha = campoSenha.getText().toString();

        if(!textoEmail.isEmpty()) {
            if(!textoSenha.isEmpty()) {
                User user = new User();
                user.setEmail(textoEmail);
                user.setSenha(textoSenha);

                userLogin( user );
            }else{
                Toast.makeText(LoginActivity.this, "Preencha a senha!",
                        Toast.LENGTH_SHORT).show();
            }
        }else{
            Toast.makeText(LoginActivity.this, "Preencha o Email!",
                    Toast.LENGTH_SHORT).show();
        }
    }
    public void userLogin( User user){
        autenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao();
        autenticacao.signInWithEmailAndPassword(
                user.getEmail(),
                user.getSenha()
        ).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    // Checking the user type logged
                    UsuarioFirebase.redirecionaLoggedUser(LoginActivity.this);
                }else{
                    String excecao = "";
                    try{
                        throw task.getException();
                    }catch (FirebaseAuthInvalidUserException e){
                        excecao = "Usuário não cadastrado!";
                    }catch (FirebaseAuthInvalidCredentialsException e){
                        excecao = "E-mail e senha não correspondem a um cadastro";
                    }catch (Exception e){
                        excecao = "Erro ao cadastrar usuário:" + e.getMessage();
                        e.printStackTrace();
                    }
                    Toast.makeText(LoginActivity.this,
                            excecao,
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
