package com.example.keepnote;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.keepnote.database.Nota;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ListaNotasAdapter extends RecyclerView.Adapter<NotaViewHolder> {
    Context context;
    List<Nota> listaNotas;
    NotaClickListener listener;

    public ListaNotasAdapter(Context context, List<Nota> listaNotas, NotaClickListener listener) {
        this.context = context;
        this.listaNotas = listaNotas;
        this.listener = listener;
    }

    private int getColorNota() {
        List<Integer> codigoColor = new ArrayList<>();
        Random random = new Random();

        codigoColor.add(R.color.color1);
        codigoColor.add(R.color.color2);
        codigoColor.add(R.color.color3);
        codigoColor.add(R.color.color4);
        codigoColor.add(R.color.color5);

        int colorRandom = random.nextInt(codigoColor.size());
        return codigoColor.get(colorRandom);
    }

    @NonNull
    @Override
    public NotaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new NotaViewHolder(LayoutInflater.from(context).inflate(R.layout.lista_notas, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull NotaViewHolder holder, int position) {
        holder.titulo.setText(listaNotas.get(position).getTitulo());
        holder.titulo.setSelected(true);

        holder.contenido.setText(listaNotas.get(position).getContenido());

        holder.fecha.setText(listaNotas.get(position).getFecha());
        holder.fecha.setSelected(true);

        if (listaNotas.get(position).isPinned()) {
            holder.pin.setImageResource(R.drawable.pin_icon);
        } else {
            holder.pin.setImageResource(0);
        }

        int color = getColorNota();
        holder.nota.setCardBackgroundColor(holder.itemView.getResources().getColor(color, null));

        holder.nota.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onClick(listaNotas.get(holder.getAdapterPosition()));
            }
        });

        holder.nota.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                listener.onLongClick(listaNotas.get(holder.getAdapterPosition()), holder.nota);
                return true;
            }
        });
    }

    @Override
    public int getItemCount() {
        return listaNotas.size();
    }

    public void filtrarLista(List<Nota> listaFiltrada) {
        listaNotas = listaFiltrada;
        notifyDataSetChanged();
    }
}

class NotaViewHolder extends RecyclerView.ViewHolder {
    CardView nota;
    TextView titulo, contenido, fecha;
    ImageView pin;

    public NotaViewHolder(@NonNull View itemView) {
        super(itemView);
        nota = itemView.findViewById(R.id.view_nota);
        titulo = itemView.findViewById(R.id.text_titulo);
        contenido = itemView.findViewById(R.id.text_contenido);
        fecha = itemView.findViewById(R.id.text_fecha);
        pin = itemView.findViewById(R.id.image_pin);
    }
}
