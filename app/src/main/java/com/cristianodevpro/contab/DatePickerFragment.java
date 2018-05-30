package com.cristianodevpro.contab;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;

import java.util.Calendar;

public class DatePickerFragment extends DialogFragment{

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        //Usa a data atual como vaor por omissão para o picker
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        //Criar uma nova isntância do DatePickerDialog e devolve-o à atividade
        return new DatePickerDialog(getActivity(), (DatePickerDialog.OnDateSetListener) this, year, month, day);
    }
}
