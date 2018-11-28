package com.eduardo.appagenda;

import android.app.AlertDialog;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.eduardo.appagenda.DAO.CompromissoDAO;
import com.eduardo.appagenda.object.Compromisso;

import java.util.List;

public class MainActivityComp extends AppCompatActivity implements View.OnClickListener {

    private FloatingActionButton floatingActionButtonAdd;
    private Button buttonListar;
    private ListView listViewComm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_comp);

        floatingActionButtonAdd = findViewById(R.id.floatingActionButtonAdd);
        floatingActionButtonAdd.setOnClickListener(this);
        buttonListar = findViewById(R.id.buttonListar);
        buttonListar.setOnClickListener(this);
        listViewComm = findViewById(R.id.listViewComm);
        try {
            CompromissoDAO compDao = new CompromissoDAO(this);
            List<Compromisso> compro = compDao.consultartodos();
            CustomList adapter = new CustomList(this, compro);
            listViewComm.setAdapter(adapter);
            listViewComm.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Compromisso p = (Compromisso) parent.getItemAtPosition(position);
                    Intent intent = new Intent(MainActivityComp.this, ActivityComm.class);
                    Bundle parms = new Bundle();
                    parms.putInt("id", p.getId());
                    intent.putExtras(parms);
                    startActivityForResult(intent, 0);
                }
            });
        }catch (Exception e){}
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.floatingActionButtonAdd:
                Intent it = new Intent(MainActivityComp.this, ActivityComm.class);
                startActivity(it);
                break;
            case R.id.buttonListar:
                CompromissoDAO compDao = new CompromissoDAO(this);
                List<Compromisso> compro = compDao.consultartodos();
                CustomList adapter = new CustomList(this, compro);
                listViewComm.setAdapter(adapter);
                break;

        }
    }
    // Implementa o Result vindo da Activity Comm - Compromisso
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(resultCode == RESULT_OK){
            CompromissoDAO compDao = new CompromissoDAO(this);
            List<Compromisso> compro = compDao.consultartodos();
            CustomList adapter = new CustomList(this, compro);
            listViewComm.setAdapter(adapter);
        }

    }
    public void dialogo(String titulo, String conteudo) {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivityComp.this);
        builder.setTitle(titulo);
        builder.setMessage(conteudo);
        builder.setNeutralButton("OK", null);
        builder.show();
    }
}
