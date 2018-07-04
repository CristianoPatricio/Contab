package com.cristianodevpro.contab;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Parcelable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.List;

public class RegistoMovimentosAdapter extends RecyclerView.Adapter<RegistoMovimentosAdapter.RegistoViewHolder> {

    private List<RegistoMovimentos> registoMovimentosList;
    private Context context;
    private RecyclerView recyclerView;
    private Cursor cursor = null;
    private DbContabOpenHelper dbContabOpenHelper;

    public class RegistoViewHolder extends RecyclerView.ViewHolder{ //representa um item dentro da recycler view

        public TextView textViewDesignacao;
        public TextView textViewData;
        public TextView textViewCategoriaDespesa;
        public TextView textViewCategoriaReceita;
        public TextView textViewTipo;
        public TextView textViewValor;

        /**
         * @param itemView
         *
         * construção dos objetos
         */
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

        /**
         * @param registoMovimento
         *
         * conteudo de um registo na recycler view
         */
        public void setRegistoMovimento(RegistoMovimentos registoMovimento) {
            int idDespesa = registoMovimento.getTipodespesa();
            int idReceita = registoMovimento.getTiporeceita();
            String catDespesa = dbContabOpenHelper.getTipoDespesaByID(idDespesa);
            String catReceita = dbContabOpenHelper.getTipoReceitaByID(idReceita);

            if (registoMovimento.getDesignacao().equals("")) { //caso a designacao não exista, colocar a categoria
                if(!catDespesa.equals("")) {
                    textViewDesignacao.setText("" + catDespesa);
                }else {
                    textViewDesignacao.setText("" + catReceita);
                }
            }else{
                textViewDesignacao.setText("" + registoMovimento.getDesignacao());
            }

            textViewCategoriaDespesa.setText(""+catDespesa);
            textViewCategoriaReceita.setText(""+catReceita);
            textViewData.setText(""+registoMovimento.getDia()+"/"+registoMovimento.getMes()+"/"+registoMovimento.getAno());

            if (registoMovimento.getReceitadespesa().equals("Despesa")){
                textViewTipo.setTextColor(Color.parseColor("#ff384c"));
            }else{
                textViewTipo.setTextColor(Color.parseColor("#93D67C"));
            }
            textViewTipo.setText(""+registoMovimento.getReceitadespesa());

            if(registoMovimento.getReceitadespesa().equals("Despesa")) {
                textViewValor.setText("-" +String.format("%.2f",registoMovimento.getValor()) + "€");
                textViewValor.setTextColor(Color.parseColor("#ff384c"));
            }else{
                textViewValor.setText(""+String.format("%.2f",registoMovimento.getValor())+"€");
                textViewValor.setTextColor(Color.parseColor("#93D67C"));
            }
        }
    }

    public RegistoMovimentosAdapter(List<RegistoMovimentos> myDataset, Context context, RecyclerView recyclerView) {
        this.registoMovimentosList = myDataset;
        this.context = context;
        this.recyclerView = recyclerView;
    }


    /**
     *
     * @param parent
     * @param viewType
     * @return layout de um elemento da recycler view
     */
    @Override
    public RegistoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_view_listar_todos, parent, false);
        return new RegistoViewHolder(itemView);
    }


    /**
     * @param holder
     * @param position
     *
     */
    @Override
    public void onBindViewHolder(final RegistoViewHolder holder, final int position) {
        //get element from dataset at this position
        //cursor.moveToPosition(position);
        final RegistoMovimentos registoMovimentos = registoMovimentosList.get(position);
        holder.setRegistoMovimento(registoMovimentos);

        //listen to single view click
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle(R.string.escolha_uma_opcao);
                builder.setMessage(R.string.atualizar_eliminar_registo);
                builder.setPositiveButton(R.string.atualizar, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        goToUpdateActivity(registoMovimentos.getId_movimento());
                        notifyDataSetChanged();
                    }
                });
                builder.setNeutralButton(R.string.eliminar, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        DbContabOpenHelper db = new DbContabOpenHelper(context);

                        try {
                            db.deleteRegistoMovimento(registoMovimentos.getId_movimento());
                            Toast.makeText(context,R.string.registo_eliminado_success,Toast.LENGTH_LONG).show();
                            registoMovimentosList.remove(position); //remover o item da lista
                            notifyItemRangeChanged(position, registoMovimentosList.size()); //alterar tamanho da lista
                            notifyDataSetChanged(); //notificar de que foram dados alterados
                        } catch (Exception e) {
                            Toast.makeText(context,R.string.erro_eliminar_registo,Toast.LENGTH_LONG).show();
                        }

                    }
                });
                builder.setNegativeButton(R.string.cancelar_adapter, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //Cancelar
                        dialogInterface.dismiss();
                    }
                });
                builder.create().show();
            }
        });
    }


    /**
     * @param id_registo
     *
     * Passa o id do registo selecionado através de um intent para a atividade editar receita/despesa
     */
    private void goToUpdateActivity(String id_registo){
        dbContabOpenHelper = new DbContabOpenHelper(context);
        String tipo = dbContabOpenHelper.checkReceitaDespesa(id_registo);
        if (tipo.equals("Receita")){ //Ir para atividade Edit Receita
            Intent i = new Intent(context, EditReceita.class);
            i.putExtra("ID",id_registo);
            context.startActivity(i);
        }else{ //Ir para a atividade Edit Despesa
            Intent i = new Intent(context, EditDespesa.class);
            i.putExtra("ID",id_registo);
            context.startActivity(i);
        }
    }

    //NÃO USADO
    public void refreshData(Cursor cursor) {
        if (this.cursor != cursor) {
            this.cursor = cursor;
            notifyDataSetChanged();
        }
    }

    /**
     * Returns the total number of items in the data set held by the adapter.
     *
     * @return The total number of items in this adapter.
     */
    @Override
    public int getItemCount() {
        return registoMovimentosList.size();
        //if (cursor == null) return 0;
        //return cursor.getCount();
    }

}
