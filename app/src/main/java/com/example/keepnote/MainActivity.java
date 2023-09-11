package com.example.keepnote;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;
import androidx.appcompat.widget.SearchView;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.app.Activity;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.keepnote.database.RoomBD;
import com.example.keepnote.database.Nota;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MainActivity extends AppCompatActivity implements PopupMenu.OnMenuItemClickListener {
    RoomBD bbdd;
    ListaNotasAdapter listaNotasAdapter;
    List<Nota> notas = new ArrayList<>();
    Nota notaSeleccionada;

    RecyclerView vistaNotas;
    FloatingActionButton agregarNota;
    SearchView busqueda;
    ImageView menu;
    boolean vistaLista = false;

    MediaPlayer[] sonidos = new MediaPlayer[5];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Objects.requireNonNull(getSupportActionBar()).setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.mipmap.ic_launcher);

        bbdd = RoomBD.getInstance(this);
        notas = bbdd.notaDao().getAll();

        vistaNotas = (RecyclerView) findViewById(R.id.view_notas);
        agregarNota = (FloatingActionButton) findViewById(R.id.button_agregar_nota);
        busqueda = (SearchView) findViewById(R.id.view_busqueda);
        menu = (ImageView) findViewById(R.id.image_menu);

        updateRecycler(notas, vistaLista);

        sonidos[0] = MediaPlayer.create(this, R.raw.transicion);
        sonidos[1] = MediaPlayer.create(this, R.raw.eliminado);
        sonidos[2] = MediaPlayer.create(this, R.raw.fijado);
        sonidos[3] = MediaPlayer.create(this, R.raw.desfijado);
        sonidos[4] = MediaPlayer.create(this, R.raw.cambiovista);

        agregarNota.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sonidos[0].start();
                Intent nota = new Intent(MainActivity.this, NotaActivity.class);
                startActivityForResult(nota, 101);
            }
        });

        busqueda.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filtrar(newText);
                return false;
            }
        });

        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showOpcionesMenu(menu);
            }
        });
    }

    private void filtrar(String newText) {
        List<Nota> listaFiltrada = new ArrayList<>();

        for (Nota nota : notas) {
            if (nota.getTitulo().toLowerCase().contains(newText.toLowerCase()) || nota.getContenido().toLowerCase().contains(newText.toLowerCase())) {
                listaFiltrada.add(nota);
            }
        }

        listaNotasAdapter.filtrarLista(listaFiltrada);
    }

    private void showOpcionesMenu(ImageView imageView) {
        PopupMenu popupMenu = new PopupMenu(this, imageView);
        popupMenu.setOnMenuItemClickListener(this);
        popupMenu.inflate(R.menu.menu_opciones);
        popupMenu.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 101) {
            if (resultCode == Activity.RESULT_OK) {
                Nota nuevaNota = (Nota) data.getSerializableExtra("nota");
                bbdd.notaDao().insert(nuevaNota);
                notas.clear();
                notas.addAll(bbdd.notaDao().getAll());
                listaNotasAdapter.notifyDataSetChanged();
            }
        } else if (requestCode == 102) {
            if (resultCode == Activity.RESULT_OK) {
                Nota nuevaNota = (Nota) data.getSerializableExtra("nota");
                bbdd.notaDao().update(nuevaNota.getId(), nuevaNota.getTitulo(), nuevaNota.getContenido());
                notas.clear();
                notas.addAll(bbdd.notaDao().getAll());
                listaNotasAdapter.notifyDataSetChanged();
            }
        }
    }

    private void updateRecycler(List<Nota> notas, boolean vistaLista) {
        vistaNotas.setHasFixedSize(true);

        if (vistaLista) {
            vistaNotas.setLayoutManager(new StaggeredGridLayoutManager(1, LinearLayoutManager.VERTICAL));
        } else {
            vistaNotas.setLayoutManager(new StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL));
        }

        listaNotasAdapter = new ListaNotasAdapter(MainActivity.this, notas, notaClickListener);
        vistaNotas.setAdapter(listaNotasAdapter);
    }

    private final NotaClickListener notaClickListener = new NotaClickListener() {
        @Override
        public void onClick(Nota nota) {
            sonidos[0].start();
            Intent intent = new Intent(MainActivity.this, NotaActivity.class);
            intent.putExtra("notaAntigua", nota);
            startActivityForResult(intent, 102);
        }

        @Override
        public void onLongClick(Nota nota, CardView cardView) {
            notaSeleccionada = nota;
            showOpcionesNota(cardView);
        }
    };

    private void showOpcionesNota(CardView cardView) {
        PopupMenu popupMenu = new PopupMenu(this, cardView);
        popupMenu.setOnMenuItemClickListener(this);
        popupMenu.inflate(R.menu.nota_opciones);
        popupMenu.show();
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        if (item.getItemId() == R.id.opcion_pin) {
            if (notaSeleccionada.isPinned()) {
                bbdd.notaDao().pin(notaSeleccionada.getId(), false);
                sonidos[3].start();
                Toast.makeText(MainActivity.this, "¡Desfijado!", Toast.LENGTH_SHORT).show();
            } else {
                bbdd.notaDao().pin(notaSeleccionada.getId(), true);
                sonidos[2].start();
                Toast.makeText(MainActivity.this, "¡Fijado!", Toast.LENGTH_SHORT).show();
            }

            notas.clear();
            notas.addAll(bbdd.notaDao().getAll());
            listaNotasAdapter.notifyDataSetChanged();
            return true;
        } else if (item.getItemId() == R.id.opcion_eliminar) {
            bbdd.notaDao().delete(notaSeleccionada);
            notas.remove(notaSeleccionada);
            listaNotasAdapter.notifyDataSetChanged();
            sonidos[1].start();
            Toast.makeText(MainActivity.this, "Nota eliminada", Toast.LENGTH_SHORT).show();
            return true;
        } else if (item.getItemId() == R.id.opcion_cambiar_vista) {
            if (vistaLista) {
                vistaLista = false;
                updateRecycler(notas, vistaLista);
                sonidos[4].start();
            } else {
                vistaLista = true;
                updateRecycler(notas, vistaLista);
                sonidos[4].start();
            }

            return true;
        } else if (item.getItemId() == R.id.opcion_ayuda) {
            sonidos[0].start();
            Intent ayuda = new Intent(MainActivity.this, AyudaActivity.class);
            startActivity(ayuda);
            return true;
        } else {
            return false;
        }
    }
}