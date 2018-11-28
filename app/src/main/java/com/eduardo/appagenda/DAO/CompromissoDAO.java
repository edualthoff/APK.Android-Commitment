package com.eduardo.appagenda.DAO;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.eduardo.appagenda.object.Compromisso;
import com.eduardo.appagenda.object.Pessoa;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CompromissoDAO {

    private SQLiteDatabase db;
    private CrierDB banco;
    private  Context context;

    public CompromissoDAO(Context context) {
        this.banco = new CrierDB(context);
        this.context = context;
    }

    public void insere(Compromisso comp) {
        try {
            db = banco.getWritableDatabase();
            String query = "insert into compromisso (titulo, descr, date, status, id_pessoa) "
                    + "values ('" + comp.getTitulo() + "', '"
                    + comp.getDescr() + "', '" + persistDate(comp.getDate()) + "', '"+ comp.getStatus() + "', '"+ comp.getPessoa().getId_pessoa() + "')";
            db.execSQL(query);
        } catch (Exception e) {
            e.printStackTrace();
        }
        db.close();
    }
    public void update(Compromisso comp){
        try {
            db = banco.getWritableDatabase();
            String query = "update compromisso set titulo= '" + comp.getTitulo()
                    + "', descr= '" + comp.getDescr() + "', date='"+ persistDate(comp.getDate())+ "', status='" + comp.getStatus()
                    + "', id_pessoa= '" + comp.getPessoa().getId_pessoa()
                    + "'where id_compr='" + comp.getId() + "'";
            db.execSQL(query);
        } catch (Exception e) {
            e.printStackTrace();
        }
        db.close();
    }
    public Compromisso consultar (int idCompro) {
        Compromisso p = new Compromisso();
        try {
            String selectQuery = "SELECT * FROM compromisso WHERE id_compr = ?";

            db = banco.getWritableDatabase();

            Cursor cursor  = db.rawQuery(selectQuery, new String[] { Integer.toString(idCompro) });
            if(cursor.moveToFirst()) {
                p.setId(cursor.getInt(0));
                p.setTitulo(cursor.getString(1));
                p.setDescr(cursor.getString(2));

                p.setDate(loadDate(cursor, 3));
                p.setStatus(cursor.getInt(4));

                PessoaDAO pdao = new PessoaDAO(context);
                Pessoa pe = pdao.consultar(cursor.getInt(5));
                p.setPessoa(pe);
                cursor.close();
            }
        } catch (Exception e) {
        }
        db.close();
        return p;
    }
    public List<Compromisso> consultartodos () {
        List<Compromisso> comp = new ArrayList<Compromisso>();

        try {
            String selectQuery = "SELECT * FROM compromisso";
            db = banco.getWritableDatabase();
            Cursor cursor = db.rawQuery(selectQuery, null);
            // looping through all rows and adding to list
            if (cursor.moveToFirst()) {
                do {
                    Compromisso c = new Compromisso();
                    c.setId(cursor.getInt(0));
                    c.setTitulo(cursor.getString(1));
                    c.setDescr(cursor.getString(2));

                    //String date = cursor.getString(3);
                    //SimpleDateFormat formato = new SimpleDateFormat();
                    //Date d = formato.parse(date);
                    //long milliseconds = d.getTime();
                    c.setDate(loadDate(cursor, 3));
                    c.setStatus(cursor.getInt(4));

                    PessoaDAO pdao = new PessoaDAO(context);
                    Pessoa pe = pdao.consultar(cursor.getInt(5));
                    c.setPessoa(pe);

                    comp.add(c);
                } while (cursor.moveToNext());
                cursor.close();
            }
        }catch (Exception ee ){}
        db.close();
        return comp;
    }

    public void delete(int idcompromisso){
        try {
            String selectQuery = "DELETE  FROM compromisso where id_compr =" + idcompromisso;
            db = banco.getWritableDatabase();
            db.execSQL(selectQuery);

        }catch (Exception e){}
    }
    public static Long persistDate(Date date) {
        if (date != null) {
            return date.getTime();
        }
        return null;
    }
    public static Date loadDate(Cursor cursor, int index) {
        if (cursor.isNull(index)) {
            return null;
        }
        return new Date(cursor.getLong(index));
    }
}
