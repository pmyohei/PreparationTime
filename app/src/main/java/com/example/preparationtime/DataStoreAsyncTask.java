package com.example.preparationtime;

import android.os.AsyncTask;
import android.widget.LinearLayout;

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

    private DB_OPERATION    operation;
    private int             p_key;
    private String          task;
    private int             taskTime;
    private LinearLayout    rootView;

    /*
     * コンストラクタ
     *   表示
     */
    public DataStoreAsyncTask(DB_OPERATION operation, LinearLayout rootView){
        this.operation = operation;
        this.rootView  = rootView;
    }

    /*
     * コンストラクタ
     *   生成
     */
    public DataStoreAsyncTask(DB_OPERATION operation, String task, int taskTime){
        this.operation = operation;
        this.task = task;
        this.taskTime = taskTime;
    }

    /*
     * コンストラクタ
     *   更新
     */
    public DataStoreAsyncTask(DB_OPERATION operation, int p_key, String task, int taskTime){
        this.operation  = operation;
        this.p_key      = p_key;
        this.task       = task;
        this.taskTime   = taskTime;
    }

    /*
     * コンストラクタ
     *   削除
     */
    public DataStoreAsyncTask(DB_OPERATION operation, int p_key){
        this.operation  = operation;
        this.p_key      = p_key;
    }

    @Override
    protected Integer doInBackground(Void... params) {

        //--操作種別に応じた処理
        if(this.operation == DB_OPERATION.CREATE){
            //登録
            this.createTaskData();

        } else if(this.operation == DB_OPERATION.READ ){
            //表示
            this.displayTaskData();

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

    @Override
    protected void onPostExecute(Integer code) {

    }


    /*
     * 「やること」の表示処理
     */
    private void displayTaskData(){


    }

    /*
     * 「やること」の編集処理
     */
    private void updateTaskData(){

    }


    /*
     * 「やること」の生成処理
     */
    private void createTaskData(){

    }

    /*
     * 「やること」の削除処理
     */
    private void deleteTaskData(){

    }
}
