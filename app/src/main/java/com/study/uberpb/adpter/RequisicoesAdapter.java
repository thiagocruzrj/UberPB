package com.study.uberpb.adpter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.study.uberpb.R;
import com.study.uberpb.model.Requisicao;
import com.study.uberpb.model.User;

import java.util.List;

public class RequisicoesAdapter extends RecyclerView.Adapter<RequisicoesAdapter.MyViewHolder> {

    private List<Requisicao> requisicoes;
    private Context context;
    private User motorista;

    public RequisicoesAdapter(List<Requisicao> requisicoes, Context context, User motorista) {
        this.requisicoes = requisicoes;
        this.context = context;
        this.motorista = motorista;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View item = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_requisicoes, parent, false);
        return new MyViewHolder(item);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Requisicao requisicao = requisicoes.get(position);
        User passageiro = requisicao.getPassageiro();

        holder.nome.setText(passageiro.getNome());
        holder.distancia.setText("1 KM - Aproximadamente");
    }

    @Override
    public int getItemCount() {
        return requisicoes.size();
    }

    public class  MyViewHolder extends RecyclerView.ViewHolder{
        TextView nome, distancia;

        public MyViewHolder(View itemView){
            super(itemView);
            nome = itemView.findViewById(R.id.textRequisicaoNome);
            distancia = itemView.findViewById(R.id.textRequisicaoDistancia);
        }
    }
}
