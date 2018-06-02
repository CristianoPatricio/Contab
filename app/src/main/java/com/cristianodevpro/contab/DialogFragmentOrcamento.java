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

public class DialogFragmentOrcamento extends AppCompatDialogFragment {

    private EditText editTextInput;
    private DialogFragmentOrcamento.ExampleDialogListener listener;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        //Get the layout inflater
        LayoutInflater inflater = getActivity().getLayoutInflater();

        //Pass null as the parent view because its going in the dialog layout
        View view = inflater.inflate(R.layout.dialog_orcamento_layout, null);
        builder.setView(view)

                //Add action buttons
                .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setPositiveButton("Definir", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //atribuir os valores às variáveis
                        double orcamento = Double.parseDouble(editTextInput.getText().toString());
                        listener.setValue(orcamento);
                    }
                });

        editTextInput = view.findViewById(R.id.editTextDialogOrcamento);

        return builder.create();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            listener = (DialogFragmentOrcamento.ExampleDialogListener) context;
        } catch (ClassCastException e) {
            throw  new ClassCastException(context.toString() +
                    "must implement ExampleDialogListener");
        }
    }

    public interface ExampleDialogListener {
        void setValue(double orcamento);
    }
}
