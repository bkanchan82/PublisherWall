package com.publisherwall.android.publisherwall.data;

import android.arch.lifecycle.LiveData;
import android.arch.paging.DataSource;
import android.arch.paging.LivePagedListBuilder;
import android.arch.paging.LivePagedListProvider;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

@Dao
public interface NetworkDao {

    @Query("SELECT * FROM network ORDER BY id")
    DataSource.Factory<Integer, NetworkEntry> allNetworksEntry();

    @Query("SELECT * FROM network ORDER BY id")
    LiveData<List<NetworkEntry>> loadNetworkEntry();

    @Query("SELECT * FROM network WHERE is_like=:isLike")
    LiveData<List<NetworkEntry>> loadFavoriteNetworkEntry(String isLike);

    @Query("SELECT id FROM network ORDER BY id")
    List<NetworkEntry> checkNetworkEntry();

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateTask(NetworkEntry networkEntry);

    @Insert
    void insertTask(NetworkEntry networkEntry);

    /**
     * Inserts multiple cheeses into the database
     *
     * @param networkEntries An array of new cheeses.
     * @return The row IDs of the newly inserted cheeses.
     */
    @Insert
    long[] insertAll(NetworkEntry[] networkEntries);

    @Delete
    void deleteTask(NetworkEntry networkEntry);

    @Query("DELETE FROM network")
    void nukeTable();

    @Query("SELECT * FROM network WHERE id=:id")
    LiveData<NetworkEntry> getNetworkById(int id);

    @Query("SELECT * FROM network WHERE name LIKE '%' || :networkName || '%'")
    DataSource.Factory<Integer, NetworkEntry> getNetworksByName(String networkName);

    @Query("SELECT * FROM network WHERE network_sno=:networkId")
    LiveData<NetworkEntry> getNetworksByNetworkId(String networkId);

}
