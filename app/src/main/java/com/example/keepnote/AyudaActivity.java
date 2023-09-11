package com.example.keepnote;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.example.keepnote.help.Ayuda1Fragment;
import com.example.keepnote.help.Ayuda2Fragment;
import com.example.keepnote.help.Ayuda3Fragment;
import com.example.keepnote.help.Ayuda4Fragment;

public class AyudaActivity extends AppCompatActivity {
    Ayuda1Fragment fragmentInicio;
    Ayuda2Fragment fragmentVista;
    Ayuda3Fragment fragmentBusqueda;
    Ayuda4Fragment fragmentNota;

    ImageView atras;
    MediaPlayer sonido;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ayuda);

        fragmentInicio = new Ayuda1Fragment();
        fragmentVista = new Ayuda2Fragment();
        fragmentBusqueda = new Ayuda3Fragment();
        fragmentNota = new Ayuda4Fragment();

        getSupportFragmentManager().beginTransaction().add(R.id.contenedor_ayuda, fragmentInicio).commit();

        atras = (ImageView) findViewById(R.id.image_atras);
        sonido = MediaPlayer.create(this, R.raw.transicion);

        atras.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sonido.start();
                Intent inicio = new Intent(AyudaActivity.this, MainActivity.class);
                startActivity(inicio);
            }
        });
    }

    public void onClick(View view) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        if (view.getId() == R.id.button_inicio) {
            transaction.replace(R.id.contenedor_ayuda, fragmentInicio).commit();
        } else if (view.getId() == R.id.button_vista) {
            transaction.replace(R.id.contenedor_ayuda, fragmentVista).commit();
        } else if (view.getId() == R.id.button_busqueda) {
            transaction.replace(R.id.contenedor_ayuda, fragmentBusqueda).commit();
        } else {
            transaction.replace(R.id.contenedor_ayuda, fragmentNota).commit();
        }
    }
}