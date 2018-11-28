package com.eduardo.appagenda.DAO;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class CrierDB extends SQLiteOpenHelper {
    private static final String NOME_BANCO = "bancodados";
    private static final String TABELA_COMPR = "create table if not exists compromisso ("
            + " id_compr integer not null PRIMARY KEY autoincrement,"
            + "	titulo text, descr text, date DATE, status INTEGER DEFAULT 0, id_pessoa NOT NULL, "
            + "CONSTRAINT fk_pessoa_comp FOREIGN KEY(id_pessoa) REFERENCES pessoa(id_pessoa))";
    private static final String TABELA_PESSOA = "create table if not exists pessoa ("
            + " id_pessoa integer not null primary key autoincrement,"
            + "	nome text, sobrenome text, email text, tel INTEGER )";

    private static final int VERSAO = 1;

    public CrierDB(Context context){
        super(context, NOME_BANCO,null,VERSAO);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(TABELA_PESSOA);
        db.execSQL(TABELA_COMPR);

        Log.d("CREATE TABLE","Create Table Successfully.");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE compromisso");
        db.execSQL("DROP TABLE pessoa");
    }
}
