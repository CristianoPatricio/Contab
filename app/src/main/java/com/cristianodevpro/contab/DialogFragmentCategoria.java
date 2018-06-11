package com.cristianodevpro.contab;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class DialogFragmentCategoria extends AppCompatDialogFragment {

    private EditText editTextInput;
    private ExampleDialogListener listener;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        //Get the layout inflater
        LayoutInflater inflater = getActivity().getLayoutInflater();

        //Pass null as the parent view because its going in the dialog layout
        View view = inflater.inflate(R.layout.dialog_categoria_layout, null);
        builder.setView(view)

                //Add action buttons
                .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setPositiveButton("Inserir", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //Verificar se o campo está vazio
                        String categoria = editTextInput.getText().toString();
                        if (categoria.trim().isEmpty()){
                            Toast.makeText(getContext(), "Não foram inseridas categorias!", Toast.LENGTH_LONG).show();
                        }else {
                            listener.setTexts(categoria);
                        }
                    }
                });

        editTextInput = view.findViewById(R.id.editTextDialogCategoria);

        return builder.create();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            listener = (ExampleDialogListener) context;
        } catch (ClassCastException e) {
            throw  new ClassCastException(context.toString() +
                    "must implement ExampleDialogListener");
        }
    }

    public interface ExampleDialogListener {
        void setTexts(String categoria);
    }
}
