package com.example.keepnote;

import androidx.cardview.widget.CardView;

import com.example.keepnote.database.Nota;

public interface NotaClickListener {
    void onClick(Nota nota);
    void onLongClick(Nota nota, CardView cardView);
}
