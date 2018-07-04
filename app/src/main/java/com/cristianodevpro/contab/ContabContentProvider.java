package com.cristianodevpro.contab;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

public class ContabContentProvider extends ContentProvider {

    public static final String AUTHORITY = "com.cristianodevpro.contab.ContabContentProvider";

    public static final Uri BASE_URI = Uri.parse("content://"+AUTHORITY);
    public static final Uri CONTAB_URI = Uri.withAppendedPath(BASE_URI,DbTableRegistoMovimentos.TABLE_NAME);
    public static final Uri CATEGORIAS_RECEITAS_URI = Uri.withAppendedPath(BASE_URI, DbTableTipoReceita.TABLE_NAME);

    private static final int CATEGORIAS_RECEITAS = 200;
    private static final int CATEGORIAS_RECEITAS_ID = 201;

    private static final String MULTIPLE_ITEMS = "vdn.android.cursor.dir";
    private static final String SINGLE_ITEM = "vnd.android.cursor.item";

    DbContabOpenHelper dbContabOpenHelper;

    private static UriMatcher getContabUriMatcher(){
        UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

        uriMatcher.addURI(AUTHORITY, "categorias_receitas", CATEGORIAS_RECEITAS);
        uriMatcher.addURI(AUTHORITY, "categorias_receitas/#", CATEGORIAS_RECEITAS_ID);

        return uriMatcher;
    }
    /**
     * @return
     */
    @Override
    public boolean onCreate() {
        dbContabOpenHelper = new DbContabOpenHelper(getContext());
        return true;
    }

    /**
     *
     * @param uri
     * @param projection
     * @param selection
     * @param selectionArgs
     * @param sortOrder
     * @return
     */
    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        SQLiteDatabase db = dbContabOpenHelper.getReadableDatabase();

        String id = uri.getLastPathSegment();

        UriMatcher matcher = getContabUriMatcher();

        switch (matcher.match(uri)){

            case CATEGORIAS_RECEITAS:
                return new DbTableTipoReceita(db).query(projection,selection,selectionArgs,null,null,sortOrder);
            case CATEGORIAS_RECEITAS_ID:
                return new DbTableTipoReceita(db).query(projection, DbTableTipoReceita._ID+"=?",new String[]{id},null,null,sortOrder);

            default:
                throw new UnsupportedOperationException("Invalid URI: " + uri);
        }
    }

    /**
     *
     * @param uri
     * @return
     */
    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        UriMatcher matcher = getContabUriMatcher();

        switch (matcher.match(uri)){
            case CATEGORIAS_RECEITAS:
                return MULTIPLE_ITEMS+"/"+AUTHORITY+"/"+DbTableTipoReceita.TABLE_NAME;

            case CATEGORIAS_RECEITAS_ID:
                return SINGLE_ITEM+"/"+AUTHORITY+"/"+DbTableTipoReceita.TABLE_NAME;
            default:
                throw new UnsupportedOperationException("Unknown URI: "+uri);
        }
    }

    /**
     *
     * @param uri
     * @param values
     * @return
     */
    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        SQLiteDatabase db = dbContabOpenHelper.getWritableDatabase();

        UriMatcher matcher = getContabUriMatcher();

        long id = -1;

        switch (matcher.match(uri)){
            case CATEGORIAS_RECEITAS:
                id = new DbTableTipoReceita(db).insert(values);
                break;
            default:
                throw new UnsupportedOperationException("Invalid URI: "+uri);
        }

        if (id > 0){ //foram inseridos registos
            notifyChanges(uri);
            return Uri.withAppendedPath(uri,Long.toString(id));
        }else{
            throw new SQLException("Não foi possível inserir o registo");
        }

    }

    private void notifyChanges(@NonNull Uri uri) {
        getContext().getContentResolver().notifyChange(uri,null);
    }

    /**
     *
     * @param uri
     * @param s
     * @param strings
     * @return
     */
    @Override
    public int delete(@NonNull Uri uri, @Nullable String s, @Nullable String[] strings) {
        SQLiteDatabase db = dbContabOpenHelper.getWritableDatabase();

        UriMatcher matcher = getContabUriMatcher();

        String id = uri.getLastPathSegment();

        int rows = 0;

        switch (matcher.match(uri)){
            case CATEGORIAS_RECEITAS_ID:
                rows = new DbTableTipoReceita(db).delete(DbTableTipoReceita._ID+"=?",new String[]{id});
                break;

            default:
                throw new UnsupportedOperationException("Invalid URI: "+uri);
        }

        if (rows > 0) notifyChanges(uri);

        return rows;
    }

    /**
     *
     * @param uri
     * @param values
     * @param s
     * @param strings
     * @return
     */
    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String s, @Nullable String[] strings) {
        SQLiteDatabase db = dbContabOpenHelper.getWritableDatabase();

        UriMatcher matcher = getContabUriMatcher();

        String id = uri.getLastPathSegment();

        int rows = 0;

        switch (matcher.match(uri)){
            case CATEGORIAS_RECEITAS_ID:
                rows = new DbTableTipoReceita(db).update(values,DbTableTipoReceita._ID+"=?",new String[]{id});
                break;

            default:
                throw new UnsupportedOperationException("Invalid URI: "+uri);

        }

        if (rows > 0) notifyChanges(uri);

        return rows;
    }
}
