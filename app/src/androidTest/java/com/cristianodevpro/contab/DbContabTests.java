package com.cristianodevpro.contab;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class DbContabTests {
    @Before
    public void setUp(){
        getContext().deleteDatabase(DbContabOpenHelper.DATABASE_NAME);
    }


    @Test
    public void openDbContabTest() {
        // Context of the app under test.
        Context appContext = getContext();

        DbContabOpenHelper dbContabOpenHelper = new DbContabOpenHelper(appContext);

        SQLiteDatabase db = dbContabOpenHelper.getReadableDatabase(); //Db para leitura
        assertTrue("Não foi possível abrir/criar a Db Contab",db.isOpen()); //devolve a mensagem se não conseguir criar/abrir a Base de Dados
        db.close();
    }

    private Context getContext(){
        return InstrumentationRegistry.getTargetContext();
    }
}
