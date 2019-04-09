package br.senai.sp.agendaandroid;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.support.design.widget.TextInputLayout;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;


import java.io.ByteArrayOutputStream;

import br.senai.sp.conversores.Imagem;
import br.senai.sp.modelo.Contato;

public class ContatoHelper {
    private EditText txt_nome;
    private EditText txt_endereco;
    private EditText txt_telefone;
    private EditText txt_email;
    private EditText txt_linkedin;
    private TextInputLayout til_txt_nome;
    private TextInputLayout til_txt_endereco;
    private TextInputLayout til_txt_telefone;
    private TextInputLayout til_txt_email;
    private TextInputLayout til_txt_linkedin;
    private ImageView foto_cadastro;
    String value_txt_nome;
    String value_txt_endereco;
    String value_txt_telefone;
    String value_txt_email;
    String value_txt_linkedin;


    public ContatoHelper(CadastroContatoActivity activity){
        txt_nome = activity.findViewById(R.id.txt_nome);
        txt_endereco = activity.findViewById(R.id.txt_endereco);
        txt_telefone = activity.findViewById(R.id.txt_telefone);
        txt_email = activity.findViewById(R.id.txt_email);
        txt_linkedin = activity.findViewById(R.id.txt_linkedin);
        foto_cadastro = activity.findViewById(R.id.foto_cadastro);
        til_txt_nome = activity.findViewById(R.id.til_txt_nome);
        til_txt_endereco = activity.findViewById(R.id.til_txt_endereco);
        til_txt_telefone = activity.findViewById(R.id.til_txt_telefone);
        til_txt_email = activity.findViewById(R.id.til_txt_email);
        til_txt_linkedin = activity.findViewById(R.id.til_txt_linkedin);
    }

    public String nome(){
        String value_txt_nome = txt_nome.getText().toString();
        return value_txt_nome;
    }

    public Contato getContato(){
        Contato contato = new Contato();
        contato.setNome(txt_nome.getText().toString());
        contato.setEndereco(txt_endereco.getText().toString());
        contato.setTelefone(txt_telefone.getText().toString());
        contato.setEmail(txt_email.getText().toString());
        contato.setLinkedin(txt_linkedin.getText().toString());

        //conteudo do imageview convertido em bitmap
        Bitmap bm = ((BitmapDrawable)foto_cadastro.getDrawable()).getBitmap();
        Bitmap smallBm = Bitmap.createScaledBitmap(bm, 300, 300, true);

        //converter bitmap em array de bytes
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        smallBm.compress(Bitmap.CompressFormat.PNG, 0, stream);
        byte[] fotoArray = stream.toByteArray();

        contato.setFoto(fotoArray);
        return contato;
    }

    public void setContato(Contato contato){
        txt_nome.setText(contato.getNome());
        txt_endereco.setText(contato.getEndereco());
        txt_telefone.setText(contato.getTelefone());
        txt_email.setText(contato.getEmail());
        txt_linkedin.setText(contato.getLinkedin());

        if(contato.getFoto() != null){
            foto_cadastro.setImageBitmap(Imagem.arrayToBitmap(contato.getFoto()));
        }
    }

    public boolean getErros(){
        String value_txt_nome = txt_nome.getText().toString();
        String value_txt_endereco = txt_endereco.getText().toString();
        String value_txt_telefone = txt_telefone.getText().toString();
        String value_txt_email = txt_email.getText().toString();
        String value_txt_linkedin = txt_linkedin.getText().toString();

        String til_nulo = "Por favor, preencha o campo";
        String tel_invalido = "Digite o telefone no formato 12345678 ou 912345678";
        String nome_end_invalido = "Não digite numeros ou caracteres invalidados";
        boolean temErros = false;

        // ---------- VALIDAÇÃO DO NOME ----------
        if(isNull(value_txt_nome)){
            til_txt_nome.setError(til_nulo);
            temErros = true;
        } else if (!verificarNome(value_txt_nome)){
            til_txt_nome.setError(nome_end_invalido);
            temErros = true;
        } else {
            til_txt_nome.setError(null);
        }

        // ---------- VALIDAÇÃO DO ENDERECO ----------
        if(isNull(value_txt_endereco)){
            til_txt_endereco.setError(til_nulo);
            temErros = true;
        } else {
            til_txt_endereco.setError(null);
        }

        // ---------- VALIDAÇÃO DO TELEFONE ----------
        if(isNull(value_txt_telefone)){
            til_txt_telefone.setError(til_nulo);
            temErros = true;
        }else if(!verificarTelefone(value_txt_telefone)){
            til_txt_telefone.setError(tel_invalido);
            temErros = true;
        } else {
            til_txt_telefone.setError(null);
        }


        // ---------- VALIDAÇÃO DO EMAIL ----------
        if(isNull(value_txt_email)){
            til_txt_email.setError(til_nulo);
            temErros = true;
        }

        // ---------- VALIDAÇÃO DO LINKEDIN ----------
        if(isNull(value_txt_linkedin)){
            til_txt_linkedin.setError(til_nulo);
            temErros = true;
        }

        return temErros;
    }

    public boolean isNull(String value){
        boolean isNull = false;

        if(value.isEmpty()){
            isNull = true;
        }

        return isNull;
    }

    public boolean verificarNome(String nome){
        return nome.matches("^[A-Za-zãêáàéèêíìîõôóòúùû ]*$");
    }

    public boolean verificarTelefone(String tel){
        return tel.matches("^[0-9]{8,9}$");
    }
}
