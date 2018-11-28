package com.eduardo.appagenda;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.app.AlertDialog.Builder;
import android.widget.Toast;

import com.eduardo.appagenda.DAO.PessoaDAO;
import com.eduardo.appagenda.object.Pessoa;

public class ActivityContato extends AppCompatActivity implements OnClickListener {

    private static final int MY_PERMISSIONS_REQUEST_READ_CONTACTS = 1;
    private View buttonSalvar;
    private View buttonBuscarContAgenda;
    private EditText editTextNome;
    private EditText editTextSobrenome;
    private EditText editTextEmail;
    private EditText editTextPhone;
    private int idPessoa;
    private Pessoa pessoaSelect;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contato);

        this.editTextNome = findViewById(R.id.editTextNome);
        this.editTextSobrenome = findViewById(R.id.editTextSobrenome);
        this.editTextEmail = findViewById(R.id.editTextEmail);
        this.editTextPhone = findViewById(R.id.editTextPhone);
        buttonSalvar = findViewById(R.id.buttonSalvar);
        buttonSalvar.setOnClickListener(this);
        buttonBuscarContAgenda = findViewById(R.id.buttonBuscarContAgenda);
        buttonBuscarContAgenda.setOnClickListener(this);

        Bundle args = getIntent().getExtras();
        if (getIntent().hasExtra("id")) {
            idPessoa = args.getInt("id");
            editTextNome.setText(args.getString("nome"));
            editTextSobrenome.setText(args.getString("sobrenome"));
            editTextEmail.setText(args.getString("email"));
            editTextPhone.setText(Long.toString(args.getInt("tel")));
            pessoaSelect  = new Pessoa(idPessoa,
                    args.getString("nome"),
                    args.getString("sobrenome"),
                    args.getString("email"),
                    args.getLong("tel") );
        }
    }

    @Override
    public void onClick(View v) {
        PessoaDAO pDAO = new PessoaDAO(this);
        switch (v.getId()) {
            // Button para Salvar e Atualizar contato
            case R.id.buttonSalvar:
                try {
                   // pessoa = pDAO.consultar(this.idPessoa);
                    if (pessoaSelect != null) {
                        Pessoa pessoa = new Pessoa(
                                pessoaSelect.getId_pessoa(),
                                editTextNome.getText().toString(),
                                editTextSobrenome.getText().toString(),
                                editTextEmail.getText().toString(),
                                Long.parseLong(editTextPhone.getText().toString()));
                        pDAO.update(pessoa);
                        Intent data = new Intent();
                        Bundle parms = new Bundle();
                        parms.putInt("id", pessoa.getId_pessoa());
                        parms.putString("nome", pessoa.getNome());
                        parms.putString("sobrenome", pessoa.getSobrenome());
                        parms.putString("email", pessoa.getEmail());
                        parms.putLong("tel", pessoa.getTel());
                        data.putExtras(parms);
                        setResult(RESULT_OK, data);
                    } else {
                        Pessoa p = new Pessoa(
                            editTextNome.getText().toString(),
                            editTextSobrenome.getText().toString(),
                            editTextEmail.getText().toString(),
                            Long.parseLong(editTextPhone.getText().toString()));
                        /*p.setNome(editTextNome.getText().toString());
                        p.setSobrenome(editTextSobrenome.getText().toString());
                        p.setEmail(editTextEmail.getText().toString());
                        p.setTel(Long.parseLong(editTextPhone.getText().toString()));*/
                        int id = pDAO.insere(p);
                        Intent data = new Intent();
                        Bundle parms = new Bundle();
                        parms.putInt("id", id);
                        data.putExtras(parms);
                        setResult(RESULT_OK, data);
                    }
                    limpar();
                    finish();
                 } catch (Exception e) {
                    this.dialogo("ERRO", "Favor preencher os campos");
                }

            break;
            // Button para selecionar um contato na Agenda do Telefone
            case R.id.buttonBuscarContAgenda:
                askForContactPermission();
                break;
        }
    }
    // Activity da lista de Contatos do Telefone
    private void getContact(){
        Intent intent_contact = new Intent(Intent.ACTION_PICK,ContactsContract.Contacts.CONTENT_URI);
        startActivityForResult(intent_contact, 1);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (resultCode == RESULT_OK) {
            try {
                //Pegar a URI e a Query do contactProvider e o numero do telefone
                Uri contatoUri = data.getData();
                String contactNumber = "";
                String emailAddress ="";
                Cursor cursor = getContentResolver().query(contatoUri, null, null, null, null);

                //SE O CURSOR RETORNAR UM VALOR VALIDO ENTÃO PEGA O NUMERO
                if (cursor != null && cursor.moveToFirst()) {
                    String indexName = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME_PRIMARY));
                    String contactId = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
                    String hasPhone = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER));

                    // Buscar Phone
                    try {
                        if ( hasPhone.equalsIgnoreCase("1"))
                            hasPhone = "true";
                        else
                            hasPhone = "false" ;

                        if (Boolean.parseBoolean(hasPhone)) {
                            Cursor cursorPhone = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                                    new String[]{ContactsContract.CommonDataKinds.Phone.NUMBER},
                                    ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ? ",
                                    new String[]{contactId}, null);
                            if (cursorPhone.moveToFirst()) {
                                contactNumber = cursorPhone.getString(cursorPhone.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                                editTextPhone.setText(contactNumber);
                            }
                            cursorPhone.close();
                        }
                        // Buscar Email Addresses
                        Cursor cursorEmail = getContentResolver().query(
                                ContactsContract.CommonDataKinds.Email.CONTENT_URI,
                                new String[]{ContactsContract.CommonDataKinds.Email.ADDRESS},
                                ContactsContract.CommonDataKinds.Email.CONTACT_ID + " = ? ",
                                new String[]{contactId}, null);
                        if (cursorEmail.moveToFirst()) {
                            emailAddress = cursorEmail.getString(cursorEmail.getColumnIndex(ContactsContract.CommonDataKinds.Email.ADDRESS));
                            editTextEmail.setText(emailAddress);
                        }
                        cursorEmail.close();
                    } catch (Exception e) {
                        Log.e( "erro ", "Error parsing contacts");
                    }
                    //ação do que recebe o numero do contato e envia para a activity
                    String[] resul;
                    resul = indexName.split(" ", 2);
                    editTextNome.setText(resul[0]);
                    editTextSobrenome.setText(resul[1]);

                }
                cursor.close();
            } catch (Exception e) {
                Log.e( "erro ", "Error contacts");
            }

        }
    }

    public void limpar() {
        editTextNome.setText("");
        editTextSobrenome.setText("");
        editTextEmail.setText("");
        editTextPhone.setText("");
    }
    public void dialogo(String titulo, String conteudo) {
        Builder builder = new Builder(ActivityContato.this);
        builder.setTitle(titulo);
        builder.setMessage(conteudo);
        builder.setNeutralButton("OK", null);
        builder.show();
    }
    // Activity que solicita a autorizacao de acesso do Contact
    public void askForContactPermission(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this,Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {

                if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                        Manifest.permission.READ_CONTACTS)) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setTitle("Autorização de acesso aos Contatos");
                    builder.setPositiveButton(android.R.string.ok, null);
                    builder.setMessage("Confirme a solicitação de acesso");
                    builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
                        @TargetApi(Build.VERSION_CODES.M)
                        @Override
                        public void onDismiss(DialogInterface dialog) {
                            requestPermissions(
                                    new String[]
                                            {Manifest.permission.READ_CONTACTS}
                                    , MY_PERMISSIONS_REQUEST_READ_CONTACTS);
                        }
                    });
                    builder.show();
                } else {
                    // A autorização ja foi solicitada e autorizada
                    ActivityCompat.requestPermissions(this,
                            new String[]{Manifest.permission.READ_CONTACTS},
                            MY_PERMISSIONS_REQUEST_READ_CONTACTS);
                }
            }else{
                getContact();
            }
        }
        else{
            getContact();
        }
    }
    // Verifica se o pedido de autorização de acessos do contato foi negado ou autorizado
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_READ_CONTACTS: {
                //Caso a solicitação for cancelada, o Array retornado será vazio..
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    getContact();
                    // Permissao autorizada
                } else {
                    Toast.makeText(this, "No Permissions ", Toast.LENGTH_SHORT).show();
                    // Permissão foi negada
                }
                return;
            }
        }
    }
}
