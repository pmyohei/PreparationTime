package com.example.preparationtime;

import android.os.AsyncTask;
import android.util.Log;

import java.util.List;

/*
 * 非同期-DBアクセス-やることセット
 */
public class AsyncSetTableOperaion extends AsyncTask<Void, Void, Integer> {

    //-- DB操作種別
    public enum DB_OPERATION {
        CREATE,         //生成
        READ,           //参照
        UPDATE,         //更新
        DELETE;         //削除
    }

    private AppDatabase                 db;
    private DB_OPERATION                operation;
    private String                      preSet;
    private String                      set;
    private List<SetTable>              setList;
    private SetOperationListener        listener;

    /*
     * コンストラクタ
     *   表示
     */
    public AsyncSetTableOperaion(AppDatabase db, SetOperationListener listener, DB_OPERATION operation){
        this.db        = db;
        this.listener  = listener;
        this.operation = operation;
    }

    /*
     * コンストラクタ
     *   生成・削除
     */
    public AsyncSetTableOperaion(AppDatabase db, SetOperationListener listener, DB_OPERATION operation, String set){
        this.db        = db;
        this.listener  = listener;
        this.operation = operation;
        this.set = set;
    }

    /*
     * コンストラクタ
     *   更新
     */
    public AsyncSetTableOperaion(AppDatabase db, SetOperationListener listener, DB_OPERATION operation, String preSet, String set){
        this.db             = db;
        this.listener       = listener;
        this.operation      = operation;
        this.preSet = preSet;
        this.set = set;
    }

    @Override
    protected Integer doInBackground(Void... params) {

        Integer ret = 0;

        SetTableDao setTableDao = db.setTableDao();

        //--操作種別に応じた処理
        if(this.operation == DB_OPERATION.CREATE){
            //登録
            ret = this.createSetData(setTableDao);

        } else if(this.operation == DB_OPERATION.READ ){
            //表示
            Log.i("test", "READ");
            this.displaySetData(setTableDao);

        } else if(this.operation == DB_OPERATION.UPDATE ){
            //編集
            this.updateSetData(setTableDao);

        } else if(this.operation == DB_OPERATION.DELETE ){
            //削除
            this.deleteSetData(setTableDao);

        } else{
            //do nothing
        }

        return ret;
    }

    /*
     * 「やること」の生成処理
     */
    private Integer createSetData(SetTableDao dao ){

        //プライマリーキー取得
        int pid = dao.getPid( this.set);
        Log.i("test", "pid=" + pid);
        if( pid > 0 ){
            //すでに登録済みであれば、DBには追加しない
            return -1;
        }

        //DBに追加
        dao.insert( new SetTable( this.set) );
        //正常終了
        return 0;
    }

    /*
     * 「やること」の表示処理
     */
    private void displaySetData(SetTableDao dao ){

        //DBから、保存済みのタスクリストを取得
        this.setList = dao.getAll();
    }

    /*
     * 「やること」の編集処理
     */
    private void updateSetData(SetTableDao dao ){
        //更新対象のPidを取得
        int pid = dao.getPid( this.preSet);

        //更新
        dao.updateByPid( pid, this.set);
    }

    /*
     * 「やること」の削除処理
     */
    private void deleteSetData(SetTableDao dao ){
        //Pidを取得
        int pid = dao.getPid( this.set);

        //削除
        dao.deleteByPid( pid );
    }

    @Override
    protected void onPostExecute(Integer code) {
        //super.onPostExecute(code);

        //リスナーを実装していれば、成功後の処理を行う
        if (listener != null) {

            if( this.operation == DB_OPERATION.READ ){
                //処理終了：読み込み
                listener.onSuccessSetRead(this.setList);

            } else if( this.operation == DB_OPERATION.CREATE ){
                //処理終了：新規作成
                listener.onSuccessSetCreate(code, this.set);

            } else if( this.operation == DB_OPERATION.DELETE ){
                //処理終了：削除
                listener.onSuccessSetDelete(this.set);

            } else if( this.operation == DB_OPERATION.UPDATE ){
                //処理終了：更新
                listener.onSuccessSetUpdate(this.preSet, this.set);

            } else {
                //do nothing
            }
        }
    }

    /*
     * インターフェース（リスナー）の設定
     */
    void setListener(SetOperationListener listener) {
        //リスナー設定
        this.listener = listener;
    }

    /*
     * 処理結果通知用のインターフェース
     */
    interface SetOperationListener {

        /*
         * 取得完了時
         */
        void onSuccessSetRead(List<SetTable> taskSetList );

        /*
         * 新規生成完了時
         */
        void onSuccessSetCreate(Integer code, String taskSet );

        /*
         * 削除完了時
         */
        void onSuccessSetDelete(String task);

        /*
         * 更新完了時
         */
        void onSuccessSetUpdate(String preTask, String task);

    }
}
