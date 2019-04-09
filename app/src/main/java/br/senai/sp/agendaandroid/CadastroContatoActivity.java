package br.senai.sp.agendaandroid;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.Serializable;

import br.senai.sp.dao.ContatoDAO;
import br.senai.sp.modelo.Contato;

public class CadastroContatoActivity extends AppCompatActivity  {
    public static final int GALERIA_REQUEST = 1000;
    public static final int CAMERA_REQUEST = 2000;
    private String acao = "";
    private Contato contatoAtualizar;
    private ImageButton btn_add_ft_camera;
    private ImageButton btn_add_ft_salva;
    private ImageView foto_cadastro;
    private String caminhoFoto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_contato);
        ContatoHelper helper = new ContatoHelper(this);

        btn_add_ft_camera = findViewById(R.id.btn_add_ft_camera);
        btn_add_ft_salva =  findViewById(R.id.btn_add_ft_salva);
        foto_cadastro = findViewById(R.id.foto_cadastro);

        btn_add_ft_camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

                String nomeArquivo = "/IMG_" + System.currentTimeMillis() + ".jpg";

                caminhoFoto = getExternalFilesDir(null) + nomeArquivo;

                File arquivoFoto = new File(caminhoFoto);

                Uri fotoUri = FileProvider.getUriForFile(
                        CadastroContatoActivity.this,
                        BuildConfig.APPLICATION_ID + ".provider",
                        arquivoFoto
                );

                intent.putExtra(MediaStore.EXTRA_OUTPUT, fotoUri);
                startActivityForResult(intent, CAMERA_REQUEST);
            }
        });

        btn_add_ft_salva.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent, GALERIA_REQUEST);
            }
        });

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();

        if(extras == null){
            acao = "salvar";
        } else {
            acao = "atualizar";
            contatoAtualizar = (Contato)extras.getSerializable("contato");
            helper.setContato(contatoAtualizar);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(resultCode == RESULT_OK){
            try {
                if(requestCode == GALERIA_REQUEST){
                    InputStream inputStream = getContentResolver().openInputStream(data.getData());
                    Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                    foto_cadastro.setImageBitmap(bitmap);
                }

                if(requestCode == CAMERA_REQUEST){
                    Bitmap bitmap = BitmapFactory.decodeFile(caminhoFoto);
                    Bitmap smallBitmap = Bitmap.createScaledBitmap(bitmap, 300, 300, true);
                    foto_cadastro.setImageBitmap(smallBitmap);
                }

            } catch (FileNotFoundException e){
                e.printStackTrace();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_cadastro_contato, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        ContatoHelper helper = new ContatoHelper(this);
        ContatoDAO dao = new ContatoDAO(CadastroContatoActivity.this);

        boolean temErros = helper.getErros();

        int id = item.getItemId();

        if(id == R.id.mi_cc_salvar){
            if(temErros){
                Toast.makeText(CadastroContatoActivity.this, "Verifique os erros!!! e seu nome é "+ helper.nome(), Toast.LENGTH_LONG).show();
            }

            if(acao.equals("salvar") && !temErros){
                Contato contato =  new Contato();
                contato = helper.getContato();
                dao.salvar(contato);
                Toast.makeText(CadastroContatoActivity.this, contato.getNome()+" foi salvo com sucesso!!!", Toast.LENGTH_LONG).show();
                dao.close();
                finish();
            } else if (acao.equals("atualizar")){
                int id_cont_atualizar = contatoAtualizar.getId();
                contatoAtualizar = helper.getContato();
                contatoAtualizar.setId(id_cont_atualizar);
                dao.atualizar(contatoAtualizar);
                Toast.makeText(CadastroContatoActivity.this, contatoAtualizar.getNome()+" foi atualizado com sucesso!!!", Toast.LENGTH_LONG).show();
                dao.close();
                finish();
            }


        }

        return super.onOptionsItemSelected(item);
    }
}