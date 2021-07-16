package com.example.preparationtime;

import android.os.AsyncTask;

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

    private DB_OPERATION operation;
    private int p_key;
    private String task;
    private int taskTime;

    /*
     * コンストラクタ
     *   表示
     */
    public DataStoreAsyncTask(DB_OPERATION operation){
        this.operation = operation;
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

        //操作種別に応じた処理



        return 0;
    }

    @Override
    protected void onPostExecute(Integer code) {

    }
}
