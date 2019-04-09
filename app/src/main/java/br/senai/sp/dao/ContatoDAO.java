package br.senai.sp.dao;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

import br.senai.sp.modelo.Contato;

public class ContatoDAO extends SQLiteOpenHelper{


    public ContatoDAO(Context context) {
        super(context, "db_contato", null, 2);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String slq = "CREATE TABLE tbl_contato (" +
                "id INTEGER PRIMARY KEY," +
                "nome TEXT NOT NULL," +
                "endereco TEXT NOT NULL," +
                "telefone TEXT NOT NULL," +
                "email TEXT NOT NULL," +
                "linkedin TEXT NOT NULL)";

        db.execSQL(slq);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String sql = "ALTER TABLE tbl_contato ADD COLUMN foto BLOB;";
        db.execSQL(sql);

    }

    public ContentValues getContentValues(Contato contato){
        ContentValues dados = new ContentValues();
        dados.put("nome", contato.getNome());
        dados.put("endereco", contato.getEndereco());
        dados.put("telefone", contato.getTelefone());
        dados.put("email", contato.getEmail());
        dados.put("linkedin", contato.getLinkedin());
        dados.put("foto", contato.getFoto());

        return dados;
    }

    public void salvar(Contato contato){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues dados = getContentValues(contato);
        db.insert("tbl_contato", null, dados);
    }

    public List<Contato> getListaContatos(){
        SQLiteDatabase db = getReadableDatabase();

        String sql = "SELECT * FROM tbl_contato";

        Cursor c = db.rawQuery(sql, null);

        List<Contato> contatos = new ArrayList<>();

        while(c.moveToNext()){
            Contato contato = new Contato();
            contato.setId(c.getInt(c.getColumnIndex("id")));
            contato.setNome(c.getString(c.getColumnIndex("nome")));
            contato.setEndereco(c.getString(c.getColumnIndex("endereco")));
            contato.setTelefone(c.getString(c.getColumnIndex("telefone")));
            contato.setEmail(c.getString(c.getColumnIndex("email")));
            contato.setLinkedin(c.getString(c.getColumnIndex("linkedin")));
            contato.setFoto(c.getBlob(c.getColumnIndex("foto")));
            contatos.add(contato);
        }

        return contatos;
    }

    public void atualizar(Contato contato){
        SQLiteDatabase db = getWritableDatabase();
        String[] params = {String.valueOf(contato.getId())};
        ContentValues dados = getContentValues(contato);

        db.update("tbl_contato", dados, "id = ?", params);
    }

    public void excluir(Contato contato){
        SQLiteDatabase db = getWritableDatabase();
        String[] params = {String.valueOf(contato.getId())};

        db.delete("tbl_contato", "id = ?", params);
    }
}
