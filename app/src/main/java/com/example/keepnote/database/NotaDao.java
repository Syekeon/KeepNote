package com.example.keepnote.database;

import static androidx.room.OnConflictStrategy.REPLACE;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface NotaDao {
    @Insert(onConflict = REPLACE)
    void insert(Nota nota);

    @Query("SELECT * FROM nota ORDER BY pinned DESC")
    List<Nota> getAll();

    @Query("UPDATE nota SET titulo = :titulo, contenido = :contenido WHERE id = :id")
    void update(int id, String titulo, String contenido);

    @Delete
    void delete(Nota nota);

    @Query("UPDATE nota SET pinned = :pin WHERE id = :id")
    void pin(int id, boolean pin);
}
