package com.cristianodevpro.contab;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class Sobre extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sobre);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    /**
     * Take care of popping the fragment back stack or finishing the activity
     * as appropriate.
     */
    @Override
    public void onBackPressed() {
        finish();
    }

    /************************Functions***************************/

    /**
     * preencher campos TO, SUBJECT, TEXT de uma mensagem de correi
     *
     * @param view
     */
    public void sendEmail(View view) {
        Intent i = new Intent();
        i.setAction(Intent.ACTION_SEND);
        i.putExtra(Intent.EXTRA_TEXT, "A sua mensagem de texto...");
        i.putExtra(Intent.EXTRA_EMAIL, "cristianop1998@gmail.com");
        i.putExtra(Intent.EXTRA_SUBJECT, "Suporte Contab");
        i.setType("text/plain");
        startActivity(i);
    }
}
