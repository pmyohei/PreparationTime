package com.example.preparationtime;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.List;
import java.util.ListIterator;

/*
 * エンティティ
 *   テーブルに相当
 */
@Entity
public class SetTable {
    //主キー
    @PrimaryKey(autoGenerate = true)
    private int id;

    //「やることセット」名
    @ColumnInfo(name = "set_name")
    private String setName;

    //「やること」の時間
    @ColumnInfo(name = "task_primary_ids")
    private int taskPids;

    /*
     * コンストラクタ
     */
    public SetTable(String setName) {
        this.setName = setName;
    }

    /*
     * コンストラクタ
     */
    public SetTable(String setName, List<Integer> taskPids) {
        this.setName = setName;
        //this.taskPids = taskPids;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return this.id;
    }

    public String getSetName() {
        return this.setName;
    }

    public void setSetName(String setName) {
        this.setName = setName;
    }

    public int getTaskPids() {
        return taskPids;
    }

    public void setTaskPids(int taskPids) {
        this.taskPids = taskPids;
    }
}