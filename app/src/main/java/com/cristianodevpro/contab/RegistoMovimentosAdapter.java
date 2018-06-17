package com.cristianodevpro.contab;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.List;

public class RegistoMovimentosAdapter extends RecyclerView.Adapter<RegistoMovimentosAdapter.RegistoViewHolder> {

    private List<RegistoMovimentos> registoMovimentosList;
    private Context context;
    private RecyclerView recyclerView;
    private Cursor cursor = null;

    public class RegistoViewHolder extends RecyclerView.ViewHolder{ //representa um item dentro da recycler view

        public TextView textViewDesignacao;
        public TextView textViewData;
        public TextView textViewCategoriaDespesa;
        public TextView textViewCategoriaReceita;
        public TextView textViewTipo;
        public TextView textViewValor;

        public RegistoViewHolder(View itemView) {
            super(itemView);
            textViewDesignacao = (TextView) itemView.findViewById(R.id.textViewDesignacao);
            textViewData = (TextView) itemView.findViewById(R.id.textViewData);
            textViewCategoriaDespesa = (TextView) itemView.findViewById(R.id.textViewCategoriaDespesa);
            textViewCategoriaReceita = (TextView) itemView.findViewById(R.id.textViewCategoriaReceita);
            textViewTipo = (TextView) itemView.findViewById(R.id.textViewTipo);
            textViewValor = (TextView) itemView.findViewById(R.id.textViewValor);
        }

        DbContabOpenHelper dbContabOpenHelper = new DbContabOpenHelper(context);

        public void setRegistoMovimento(RegistoMovimentos registoMovimento) {
            String catDespesa = dbContabOpenHelper.getTipoDespesaByID(registoMovimento.getTipodespesa());
            String catReceita = dbContabOpenHelper.getTipoReceitaByID(registoMovimento.getTiporeceita());

            if (registoMovimento.getDesignacao().equals("")) {
                textViewDesignacao.setText(""+catDespesa);
                //textViewDesignacao.setText(""+catReceita);
            }else{
                textViewDesignacao.setText("" + registoMovimento.getDesignacao());
            }
            textViewCategoriaDespesa.setText(""+catDespesa);
            textViewCategoriaReceita.setText(""+catReceita);
            textViewData.setText(""+registoMovimento.getDia()+"/"+registoMovimento.getMes()+"/"+registoMovimento.getAno());
            textViewTipo.setText(""+registoMovimento.getReceitadespesa());

            if(registoMovimento.getReceitadespesa().equals("Despesa")) {
                textViewValor.setText("-" + registoMovimento.getValor() + "€");
                textViewValor.setTextColor(Color.parseColor("#ff384c"));
            }else{
                textViewValor.setText(""+registoMovimento.getValor()+"€");
                textViewValor.setTextColor(Color.parseColor("#93D67C"));
            }
        }
    }

    public RegistoMovimentosAdapter(List<RegistoMovimentos> myDataset, Context context, RecyclerView recyclerView) {
        this.registoMovimentosList = myDataset;
        this.context = context;
        this.recyclerView = recyclerView;
    }


    @Override
    public RegistoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_view_listar_todos, parent, false);
        return new RegistoViewHolder(itemView);
    }


    @Override
    public void onBindViewHolder(RegistoViewHolder holder, int position) {
        //get element from dataset at this position
        final RegistoMovimentos registoMovimentos = registoMovimentosList.get(position);
        holder.setRegistoMovimento(registoMovimentos);
    }

    /**
     * Returns the total number of items in the data set held by the adapter.
     *
     * @return The total number of items in this adapter.
     */
    @Override
    public int getItemCount() {
        return registoMovimentosList.size();
    }
}
