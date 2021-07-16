package com.example.preparationtime;
import java.util.List;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

/*
 * DAO の定義
 *   これを介して、DB操作を行う。
 */
@Dao
public interface TaskTableDao {
    @Query("SELECT * FROM taskTable")
    List<TaskTable> getAll();

    @Query("SELECT * FROM taskTable WHERE id IN (:ids)")
    List<TaskTable> loadAllByIds(int[] ids);

    @Insert
    void insertAll(TaskTable... taskTables);

    @Insert
    void insert(TaskTable taskTable);

    @Delete
    void delete(TaskTable taskTable);
}
