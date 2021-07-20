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

    /*
     * 取得：プライマリーキー
     *   ※未登録の場合、プライマリーキーは「0」が返される（実証）
     *     ＝プライマリーキーは「１」から割り当てられる
     */
    @Query("SELECT id FROM taskTable WHERE task_name=(:taskName) and task_time=(:taskTime)")
    int getPid(String taskName, int taskTime);

    /*
     * 削除：プライマリーキー指定
     */
    @Query("DELETE FROM taskTable WHERE id=(:pid)")
    void deleteByPid(int pid);

    @Insert
    void insertAll(TaskTable... taskTables);

    @Insert
    void insert(TaskTable taskTable);

    @Delete
    void delete(TaskTable taskTable);
}
