package com.example.preparationtime;

import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;

/*
 * 「やること」管理画面
 */
public class TaskManageActivity extends AppCompatActivity implements AsyncTaskTableOperaion.TaskOperationListener {

    private AppDatabase     db;                     //DB

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_manage);

        //DB操作インスタンスを取得
        this.db = AppDatabaseSingleton.getInstance(getApplicationContext());

        //現在登録されている「やること」を表示
        this.displayTask();

        // FloatingActionButton
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab_addTask);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CoordinatorLayout coordinatorLayout
                        = (CoordinatorLayout) findViewById(R.id.cl_taskManage);
                Snackbar
                        .make(coordinatorLayout, "Hello, Snackbar!", Snackbar.LENGTH_SHORT)
                        .show();
            }
        });
    }

    /*
     * 「やること」の表示
     *    登録済みの「やること」データを全て表示する。
     */
    private void displayTask(){

        //-- 非同期スレッドにて、読み込み
        //「やること」
        new AsyncTaskTableOperaion(this.db, this, AsyncTaskTableOperaion.DB_OPERATION.READ).execute();
    }

    /* -------------------
     * インターフェース：「やること」
     */
    @Override
    public void onSuccessTaskRead(List<TaskTable> taskList) {

        //-- 「やること」の表示（セットへ追加の選択用）
        //レイアウトからリストビューを取得
        RecyclerView rv  = (RecyclerView) findViewById(R.id.rv_taskList);

        //レイアウトマネージャの生成・設定（横スクロール）
        LinearLayoutManager ll_manager = new LinearLayoutManager(this);
        ll_manager.setOrientation(LinearLayoutManager.HORIZONTAL);
        rv.setLayoutManager(ll_manager);

        //グリッド表示の設定
        rv.setLayoutManager(new GridLayoutManager(this, 2));

        //アダプタの生成・設定
        TaskRecyclerAdapter adapter = new TaskRecyclerAdapter(this, taskList);
        rv.setAdapter(adapter);
    }

    @Override
    public void onSuccessTaskCreate(Integer code, String task, int taskTime) {
        //do nothing
    }
    @Override
    public void onSuccessTaskDelete(String task, int taskTime) {
        //do nothing
    }
    @Override
    public void onSuccessTaskUpdate(String preTask, int preTaskTime, String task, int taskTime) {
        //do nothing
    }



}