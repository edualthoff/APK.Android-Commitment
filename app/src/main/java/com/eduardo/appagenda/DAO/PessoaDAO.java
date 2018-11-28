package com.eduardo.appagenda.DAO;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.eduardo.appagenda.object.Pessoa;

import java.util.ArrayList;
import java.util.List;

public class PessoaDAO {

    private SQLiteDatabase db;
    private CrierDB banco;

    public PessoaDAO(Context context) {
        this.banco = new CrierDB(context);
    }

    public int insere(Pessoa pessoa1) {
        int idpessoa = 0;
        Pessoa pessoa = new Pessoa();
        pessoa = pessoa1;
        try {
            db = banco.getWritableDatabase();

            String query = "insert into pessoa (nome, sobrenome, email, tel) "
                    + "values ('" + pessoa.getNome() + "', '"
                    + pessoa.getSobrenome() + "', '" + pessoa.getEmail() + "', '"+ pessoa.getTel() + "')";
            db.execSQL(query);
            String queryRow = "SELECT * from pessoa order by id_pessoa DESC limit 1";
            Cursor cursor  = db.rawQuery(queryRow, null);
            if(cursor.moveToFirst()){
                int id = cursor.getInt(0);
                idpessoa = id;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        db.close();
        return idpessoa;
    }
    public Pessoa consultar (int idPessoa) {
        Pessoa p = new Pessoa();
        try {
            String selectQuery = "SELECT nome, sobrenome, email, tel FROM pessoa WHERE id_pessoa = ?";

            db = banco.getWritableDatabase();
            Cursor cursor  = db.rawQuery(selectQuery, new String[] { Integer.toString(idPessoa) });
            if(cursor.moveToFirst()){
                p.setId_pessoa(idPessoa);
                p.setNome(cursor.getString(0));
                p.setSobrenome(cursor.getString(1));
                p.setEmail(cursor.getString(2));
                p.setTel(cursor.getLong(3));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        db.close();
        return p;
    }

    public void update (Pessoa pessoa) {
        try {
            db = banco.getWritableDatabase();
            String query = "update pessoa set nome= '" + pessoa.getNome()
                    + "', sobrenome='" + pessoa.getSobrenome() + "', email='"+ pessoa.getEmail()+ "', tel='" + pessoa.getTel()
                    + "'where id_pessoa= '" + pessoa.getId_pessoa() + "'";
            db.execSQL(query);
        } catch (Exception e) {
            e.printStackTrace();
        }
        db.close();
    }
/*
    public String[] autocomplete (){

        String query = "SELECT * FROM pessoa";

        int x = 0;
        db = banco.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        String item[] = new String[cursor.getCount()];
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Pessoa p = new Pessoa();
                p.setId_pessoa(cursor.getInt(0));
                p.setNome(cursor.getString(1));
                p.setSobrenome(cursor.getString(2));
                p.setEmail(cursor.getString(3));
                p.setTel(cursor.getInt(4));

                String nome = p.getNome() +" "+p.getSobrenome();
                item[x] = nome;
                x++;
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return item;
    }*/

    public List<Pessoa> autocomplete () {

        String query = "SELECT * FROM pessoa";

        db = banco.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        List<Pessoa> pessoa = new ArrayList<Pessoa>();
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Pessoa p = new Pessoa();
                p.setId_pessoa(cursor.getInt(0));
                p.setNome(cursor.getString(1));
                p.setSobrenome(cursor.getString(2));
                p.setEmail(cursor.getString(3));
                p.setTel(cursor.getLong(4));

                pessoa.add(p);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();

        return pessoa;
    }
}
