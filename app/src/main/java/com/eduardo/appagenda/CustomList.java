package com.eduardo.appagenda;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.eduardo.appagenda.object.Compromisso;
import com.eduardo.appagenda.object.Pessoa;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class CustomList extends ArrayAdapter<Compromisso> {
    private Context context;
    int resource, textViewResourceId;
    private List<Compromisso> compro;
    private TextView textViewTitulo;
    private TextView textViewContato;
    private TextView textViewDate;
    private TextView textViewStatus;

    CustomList(Context context, List<Compromisso> objects) {
        super(context, R.layout.custon_list, objects);
        this.context = context;
        this.resource = resource;
        this.textViewResourceId = textViewResourceId;
        compro = objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            LayoutInflater inflater = LayoutInflater.from(this.context) ;
            view = inflater.inflate( R.layout.custon_list, null, false );

        }
        Compromisso comp = compro.get(position);
        if (comp != null) {
            textViewTitulo = view.findViewById(R.id.textViewTitulo);
            textViewContato = view.findViewById(R.id.textViewContato);
            textViewDate = view.findViewById(R.id.textViewDate);
            textViewStatus = view.findViewById(R.id.textViewStatus);

            SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");
            textViewTitulo.setText(comp.getTitulo());
            Pessoa p = comp.getPessoa();
            textViewContato.setText(p.getNome());
            textViewDate.setText(formato.format(comp.getDate()));
            textViewStatus.setText(Integer.toString(comp.getStatus()));


        }
        return view;
    }
}
