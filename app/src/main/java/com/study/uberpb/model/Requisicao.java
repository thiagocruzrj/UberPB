package com.study.uberpb.model;

import com.google.firebase.database.DatabaseReference;
import com.study.uberpb.config.ConfiguracaoFirebase;

public class Requisicao {
    private String id;
    private String status;
    private User passageiro;
    private User motorista;
    private Destino destino;

    public static final String STATUS_AGUARDANDO = "aguardando";
    public static final String STATUS_A_CAMINHO = "acaminho";
    public static final String STATUS_VIAGEM = "viagem";
    public static final String STATUS_FINALIZADA = "finalizada";

    public Requisicao() {
    }

    public void salvar(){
        DatabaseReference firebaseRef = ConfiguracaoFirebase.getFirebaseDatabase();
        DatabaseReference requisicoes = firebaseRef.child("requisicoes");

        String idRequisicao = requisicoes.push().getKey();
        setId(idRequisicao);

        requisicoes.child(getId()).setValue(this);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public User getPassageiro() {
        return passageiro;
    }

    public void setPassageiro(User passageiro) {
        this.passageiro = passageiro;
    }

    public User getMotorista() {
        return motorista;
    }

    public void setMotorista(User motorista) {
        this.motorista = motorista;
    }

    public Destino getDestino() {
        return destino;
    }

    public void setDestino(Destino destino) {
        this.destino = destino;
    }
}
