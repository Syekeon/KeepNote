package com.example.keepnote.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = Nota.class, version = 1, exportSchema = false)
public abstract class RoomBD extends RoomDatabase {
    private static RoomBD bbdd;
    private static final String nombreBbdd = "KeepNote";

    public synchronized static RoomBD getInstance(Context context) {
        if (bbdd == null) {
            bbdd = Room.databaseBuilder(context.getApplicationContext(), RoomBD.class, nombreBbdd).allowMainThreadQueries().fallbackToDestructiveMigration().build();
        }

        return bbdd;
    }

    public abstract NotaDao notaDao();
}
