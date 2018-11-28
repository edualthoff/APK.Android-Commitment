package com.eduardo.appagenda;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Filter;

import com.eduardo.appagenda.object.Pessoa;

import java.util.ArrayList;
import java.util.List;

public class ContatoAdapter extends ArrayAdapter<Pessoa> {

    private Context context;
    int resource, textViewResourceId;
    private List<Pessoa> pessoas, itemsList, suggestions;

    ContatoAdapter(Context context, int resource, int textViewResourceId,
                   List<Pessoa> objects) {
        super(context, resource, textViewResourceId, objects);
        this.context = context;
        this.resource = resource;
        this.textViewResourceId = textViewResourceId;
        pessoas = objects;
        itemsList = new ArrayList<Pessoa>(objects);
        suggestions = new ArrayList<Pessoa>();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) this.context.getSystemService( Context.LAYOUT_INFLATER_SERVICE );
            view = inflater.inflate( R.layout.row_people, null, false );

        }
        Pessoa pess = pessoas.get(position);
        if (pess != null) {
            TextView lblName = view.findViewById(R.id.textViewPeople);
            if (lblName != null)
                lblName.setText(pess.getNome() +" "+ pess.getSobrenome());
        }
        return view;
    }
    @Override
    public Filter getFilter() {
        return nameFilter;
    }

    /**
     * Custom Filter implementation for custom suggestions we provide.
     */
    private Filter nameFilter = new Filter() {
        @Override
        public CharSequence convertResultToString(Object resultValue) {
            String str = ((Pessoa) resultValue).getNome() +" "+ ((Pessoa) resultValue).getSobrenome();
            return str;
        }

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            if (constraint != null) {
                suggestions.clear();
                for (Pessoa p : itemsList) {
                    if (p.getNome().toLowerCase().contains(constraint.toString().toLowerCase())) {
                        suggestions.add(p);
                    }
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = suggestions;
                filterResults.count = suggestions.size();
                return filterResults;
            } else {
                return new FilterResults();
            }
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            List<Pessoa> filterList = (ArrayList<Pessoa>) results.values;
            if (results != null && results.count > 0) {
                clear();
                for (Pessoa people : filterList) {
                    add(people);
                    notifyDataSetChanged();
                }
            }
        }
    };
}