package com.example.keepnote;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.keepnote.database.Nota;

import java.text.SimpleDateFormat;
import java.util.Date;

public class NotaActivity extends AppCompatActivity {
    EditText titulo, contenido;
    ImageView guardar, cancelar;
    Nota nota;
    boolean esNotaAntigua = false;

    MediaPlayer[] sonidos = new MediaPlayer[3];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nota);

        titulo = (EditText) findViewById(R.id.text_titulo);
        contenido = (EditText) findViewById(R.id.text_contenido);
        guardar = (ImageView) findViewById(R.id.image_guardar);
        cancelar = (ImageView) findViewById(R.id.image_cancelar);

        sonidos[0] = MediaPlayer.create(this, R.raw.transicion);
        sonidos[1] = MediaPlayer.create(this, R.raw.guardado);
        sonidos[2] = MediaPlayer.create(this, R.raw.error);

        nota = new Nota();
        try {
            nota = (Nota) getIntent().getSerializableExtra("notaAntigua");
            titulo.setText(nota.getTitulo());
            contenido.setText(nota.getContenido());
            esNotaAntigua = true;
        } catch (Exception e) {
            e.printStackTrace();
        }

        guardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String tituloNota = titulo.getText().toString();
                String contenidoNota = contenido.getText().toString();

                if (contenidoNota.isEmpty()) {
                    sonidos[2].start();
                    Toast.makeText(NotaActivity.this, "No se puede dejar la nota en blanco", Toast.LENGTH_SHORT).show();
                    return;
                }

                SimpleDateFormat dateFormat = new SimpleDateFormat("EEE, d MMM yyyy HH:mm a");
                Date date = new Date();

                if (!esNotaAntigua) {
                    nota = new Nota();
                }

                nota.setTitulo(tituloNota);
                nota.setContenido(contenidoNota);
                nota.setFecha(dateFormat.format(date));

                sonidos[1].start();
                Toast.makeText(NotaActivity.this, "Nota guardada", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent();
                intent.putExtra("nota", nota);
                setResult(Activity.RESULT_OK, intent);
                finish();
            }
        });

        cancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sonidos[0].start();
                Intent intent = new Intent();
                setResult(Activity.RESULT_CANCELED, intent);
                finish();
            }
        });
    }
}