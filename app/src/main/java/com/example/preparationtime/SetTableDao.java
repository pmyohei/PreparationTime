package com.example.preparationtime;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

/*
 * DAO の定義：やることセットテーブル
 *   DB操作の仲介役
 */
@Dao
public interface SetTableDao {
    @Query("SELECT * FROM setTable")
    List<SetTable> getAll();

    @Query("SELECT * FROM setTable WHERE id IN (:ids)")
    List<SetTable> loadAllByIds(int[] ids);

    /*
     * 取得：プライマリーキー
     *   ※未登録の場合、プライマリーキーは「0」が返される（実証）
     *     ＝プライマリーキーは「１」から割り当てられる
     */
    @Query("SELECT id FROM setTable WHERE set_name=(:taskSetName)")
    int getPid(String taskSetName);

    /*
     * 更新
     *   指定されたプライマリーキーのレコードを更新
     */
    @Query("UPDATE setTable set set_name=(:taskSetName) WHERE id=(:pid)")
    int updateByPid(int pid, String taskSetName);

    /*
     * 削除：プライマリーキー指定
     */
    @Query("DELETE FROM setTable WHERE id=(:pid)")
    void deleteByPid(int pid);

    @Insert
    void insertAll(SetTable... setTables);

    @Insert
    void insert(SetTable setTable);

    @Delete
    void delete(SetTable setTable);
}
