package com.publisherwall.android.publisherwall.data;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

@Dao
public interface EventDao {

    @Query("SELECT * FROM event ORDER BY id")
    LiveData<List<EventEntry>> loadEventEntry();

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateTask(EventEntry eventEntry);

    @Insert
    void insertEntry(EventEntry eventEntry);

    @Delete
    void deleteEvent(EventEntry eventEntry);

    @Query("SELECT * FROM event WHERE id=:id")
    LiveData<EventEntry> getEventEntryById(int id);

    @Query("SELECT * FROM event WHERE edi=:eid")
    LiveData<EventEntry> getEventEntryByEid(String eid);

    @Query("DELETE FROM event")
    public void nukeTable();

    @Insert
    long[] insertAll(EventEntry[] networkEntries);

    @Query("SELECT id FROM event ORDER BY id")
    List<EventEntry> checkEventEntry();

}
