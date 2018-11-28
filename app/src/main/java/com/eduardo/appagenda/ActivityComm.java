package com.eduardo.appagenda;

import android.app.AlertDialog;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.eduardo.appagenda.DAO.CompromissoDAO;
import com.eduardo.appagenda.DAO.PessoaDAO;
import com.eduardo.appagenda.object.Compromisso;
import com.eduardo.appagenda.object.Pessoa;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ActivityComm extends AppCompatActivity implements View.OnClickListener {
    private View buttonAdd;
    private View buttonConsultar;
    private View buttonSalvarComp;
    private View buttonRemComp;
    private EditText editTextTitulo;
    private AutoCompleteTextView autoCompleteTextView;
    private EditText editTextDescr;
    private EditText editTextDate;
    private RadioGroup groupRadio;
    private RadioButton radioButtonConf;
    private RadioButton radioButtonCancel;
    private Pessoa pessoaSelect;
    private Compromisso compromissoSelect;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comm);
        // Button
        buttonAdd = findViewById(R.id.buttonAdd);
        buttonAdd.setOnClickListener(this);
        buttonConsultar = findViewById(R.id.buttonConsultar);
        buttonConsultar.setOnClickListener(this);
        buttonSalvarComp = findViewById(R.id.buttonSalvarComp);
        buttonSalvarComp.setOnClickListener(this);
        buttonRemComp = findViewById(R.id.buttonRemComp);
        buttonRemComp.setOnClickListener(this);
        // Text
        editTextTitulo = findViewById(R.id.editTextTitulo);
        editTextDescr = findViewById(R.id.editTextDescr);
        editTextDate = findViewById(R.id.editTextDate);
        groupRadio = findViewById(R.id.groupRadio);
        radioButtonCancel = findViewById(R.id.radioButtonCancel);
        radioButtonConf = findViewById(R.id.radioButtonConf);

        autoCompleteTextView = findViewById(R.id.autoCompleteTextView);
        // Implementa a Activity com o Compromisso Clickado na Activity Main
        Bundle args = getIntent().getExtras();
        if (getIntent().hasExtra("id")) {
            int idComp = args.getInt("id");
            CompromissoDAO cdao = new CompromissoDAO(this);
            compromissoSelect = cdao.consultar(idComp);

            editTextTitulo.setText(compromissoSelect.getTitulo());
            editTextDescr.setText(compromissoSelect.getDescr());
            SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");
            editTextDate.setText(formato.format(compromissoSelect.getDate()));
            autoCompleteTextView.setText(compromissoSelect.getPessoa().getNome() + " "+compromissoSelect.getPessoa().getSobrenome());
            if(compromissoSelect.getStatus() == 1){
                radioButtonConf.toggle();
            }else {
                radioButtonCancel.toggle();
            }
        }
        PessoaDAO pDAO = new PessoaDAO(this);
        try {
            List<Pessoa> item = pDAO.autocomplete();
            // Filtro Custom para o ArrayAdapter implementando a Activity row_people
            ContatoAdapter adapter = new ContatoAdapter(this,
                    R.layout.activity_comm, R.id.autoCompleteTextView, item);
            autoCompleteTextView.setAdapter(adapter);
            autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    pessoaSelect = (Pessoa) parent.getItemAtPosition(position);
                }
            });
        } catch (NullPointerException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            // Button Adicionar um novo contato
            case R.id.buttonAdd:
                Intent it = new Intent(ActivityComm.this, ActivityContato.class);
                startActivityForResult(it, 1);
                break;
            //Button consultar um contato selecionado
            case R.id.buttonConsultar:
                if (pessoaSelect != null) {
                    Intent intent = new Intent(ActivityComm.this, ActivityContato.class);
                    Bundle parms = new Bundle();
                    parms.putInt("id", pessoaSelect.getId_pessoa());
                    parms.putString("nome", pessoaSelect.getNome());
                    parms.putString("sobrenome", pessoaSelect.getSobrenome());
                    parms.putString("email", pessoaSelect.getEmail());
                    parms.putLong("tel", pessoaSelect.getTel());
                    intent.putExtras(parms);
                    startActivityForResult(intent, 0);
                }
                break;
            // Button salvar e Update Compromisso
            case R.id.buttonSalvarComp:
                try {
                    int status = 0;
                    if(R.id.radioButtonConf == groupRadio.getCheckedRadioButtonId()){
                        status = 1;
                    }
                    String dateString = editTextDate.getText().toString();
                    SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                    Date convertedDate = dateFormat.parse(dateString);

                    CompromissoDAO compdao = new CompromissoDAO(this);
                    if (compromissoSelect != null) {
                        Compromisso comp = new Compromisso(
                                compromissoSelect.getId(),
                                editTextTitulo.getText().toString(),
                                editTextDescr.getText().toString(),
                                convertedDate,
                                status,
                                pessoaSelect );
                        compdao.update(comp);
                    }else {
                        Compromisso comp = new Compromisso(
                                editTextTitulo.getText().toString(),
                                editTextDescr.getText().toString(),
                                convertedDate,
                                status,
                                pessoaSelect );
                        compdao.insere(comp);
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                finish();
                break;
            // Button remover um compromisso
            case R.id.buttonRemComp:
                CompromissoDAO compdao = new CompromissoDAO(this);
                compdao.delete(compromissoSelect.getId());
                setResult(RESULT_OK);
                finish();
                break;
        }
    }

    // Implementa o Result vindo da Activity Contato
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        switch (requestCode) {
            // Resultado vindo da Activity Contato quando é alterado um contato, button buttonConsultar.
            case 0:
                if (resultCode == RESULT_OK) {
                    PessoaDAO pDAO = new PessoaDAO(this);
                    List<Pessoa> item = pDAO.autocomplete();
                    ContatoAdapter adapter = new ContatoAdapter(this,
                            R.layout.activity_comm, R.id.autoCompleteTextView, item);
                    autoCompleteTextView.setAdapter(adapter);
                    pessoaSelect = new Pessoa(data.getExtras().getInt("id"),
                            data.getExtras().getString("nome"),
                            data.getExtras().getString("sobrenome"),
                            data.getExtras().getString("email"),
                            data.getExtras().getLong("tel"));
                    autoCompleteTextView.setText(pessoaSelect.getNome() + " " + pessoaSelect.getSobrenome());
                }
             break;
            // Resultado vindo da Activity Contato quando é adicionado um novo contato, button buttonAdd.
            case 1:
                if (resultCode == RESULT_OK) {
                    PessoaDAO pDAO = new PessoaDAO(this);
                    List<Pessoa> item = pDAO.autocomplete();
                    ContatoAdapter adapter = new ContatoAdapter(this,
                            R.layout.activity_comm, R.id.autoCompleteTextView, item);
                    autoCompleteTextView.setAdapter(adapter);
                    pessoaSelect = pDAO.consultar(data.getExtras().getInt("id"));
                    autoCompleteTextView.setText(pessoaSelect.getNome() + " " + pessoaSelect.getSobrenome());
                }
            break;
        }
    }

    public void dialogo(String titulo, String conteudo) {
        AlertDialog.Builder builder = new AlertDialog.Builder(ActivityComm.this);
        builder.setTitle(titulo);
        builder.setMessage(conteudo);
        builder.setNeutralButton("OK", null);
        builder.show();
    }
}