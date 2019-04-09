package br.senai.sp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import br.senai.sp.agendaandroid.R;
import br.senai.sp.conversores.Imagem;
import br.senai.sp.modelo.Contato;

public class ContatosAdapter extends BaseAdapter {
    private List<Contato> contatos;
    private Context context;

    public ContatosAdapter (Context context, List<Contato> contatos){
        this.contatos = contatos;
        this.context = context;
    }

    @Override
    public int getCount() {
        return contatos.size();
    }

    @Override
    public Object getItem(int position) {
        return contatos.get(position);
    }

    @Override
    public long getItemId(int position) {
        return contatos.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Contato contato = contatos.get(position);
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.layout_lista_contato, null);

        TextView txt_nome_contato = view.findViewById(R.id.txt_nome_contato);
        txt_nome_contato.setText(contato.getNome());

        TextView txt_telefone_contato = view.findViewById(R.id.txt_telefone_contato);
        txt_telefone_contato.setText(contato.getTelefone());

        if(contato.getFoto() != null){
            ImageView foto_cadastro = view.findViewById(R.id.foto_contato);
            foto_cadastro.setImageBitmap(Imagem.arrayToBitmap(contato.getFoto()));
        }


        return view;
    }
}
