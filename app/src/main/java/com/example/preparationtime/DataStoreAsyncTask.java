package com.example.preparationtime;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.LinearLayout;

import java.util.List;

/*
 * 非同期-DBアクセスクラス
 */
public class DataStoreAsyncTask extends AsyncTask<Void, Void, Integer> {

    //DB操作種別
    public enum DB_OPERATION {
        CREATE,         //生成
        READ,           //参照
        UPDATE,         //更新
        DELETE;         //削除
    }

    private AppDatabase     db;
    private DB_OPERATION    operation;
    private int             p_key;
    private String          task;
    private int             taskTime;
    private LinearLayout    rootView;
    private List<TaskTable> taskList;

    private Listener listener;

    /*
     * コンストラクタ
     *   表示
     */
    public DataStoreAsyncTask(AppDatabase db, DB_OPERATION operation){
        this.db        = db;
        this.operation = operation;
        //this.rootView  = rootView;
    }

    /*
     * コンストラクタ
     *   生成
     */
    public DataStoreAsyncTask(AppDatabase db, DB_OPERATION operation, String task, int taskTime){
        this.db        = db;
        this.operation = operation;
        this.task      = task;
        this.taskTime  = taskTime;
    }

    /*
     * コンストラクタ
     *   更新
     */
    public DataStoreAsyncTask(AppDatabase db, DB_OPERATION operation, int p_key, String task, int taskTime){
        this.db         = db;
        this.operation  = operation;
        this.p_key      = p_key;
        this.task       = task;
        this.taskTime   = taskTime;
    }

    /*
     * コンストラクタ
     *   削除
     */
    public DataStoreAsyncTask(AppDatabase db, DB_OPERATION operation, int p_key){
        this.db         = db;
        this.operation  = operation;
        this.p_key      = p_key;
    }

    @Override
    protected Integer doInBackground(Void... params) {

        TaskTableDao taskTableDao = db.taskTableDao();

        //--操作種別に応じた処理
        if(this.operation == DB_OPERATION.CREATE){
            //登録
            this.createTaskData(taskTableDao);

        } else if(this.operation == DB_OPERATION.READ ){
            //表示
            this.displayTaskData(taskTableDao);

        } else if(this.operation == DB_OPERATION.UPDATE ){
            //編集
            this.updateTaskData();

        } else if(this.operation == DB_OPERATION.DELETE ){
            //削除
            this.deleteTaskData();

        } else{
            //do nothing
        }

        return 0;
    }

    /*
     * 「やること」の生成処理
     */
    private void createTaskData( TaskTableDao dao ){
        //DBに追加
        dao.insert( new TaskTable( this.task, this.taskTime ) );
    }

    /*
     * 「やること」の表示処理
     */
    private void displayTaskData( TaskTableDao dao ){

        //DBから、保存済みのタスクリストを取得
        this.taskList = dao.getAll();
    }

    /*
     * 「やること」の編集処理
     */
    private void updateTaskData(){

    }

    /*
     * 「やること」の削除処理
     */
    private void deleteTaskData(){

    }

    @Override
    protected void onPostExecute(Integer code) {
        super.onPostExecute(code);

        if (listener != null) {
            listener.onSuccess(this.taskList);
        }
    }

    /*
     * インターフェース（リスナー）の設定
     */
    void setListener(Listener listener) {
        this.listener = listener;
    }

    /*
     * 処理結果通知用のインターフェース
     */
    interface Listener {
        void onSuccess( List<TaskTable> taskList );
    }
}
