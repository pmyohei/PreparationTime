package com.example.preparationtime;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class MainActivity extends AppCompatActivity implements DataStoreAsyncTask.Listener {

    private AppDatabase db;                     //DB

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //DB操作インスタンスを取得
        this.db = AppDatabaseSingleton.getInstance(getApplicationContext());

        //やること新規作成ボタン
        Button createTask = (Button)findViewById(R.id.bt_createTask);
        createTask.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //やること登録ダイアログの生成
                createTaskDialog();
            }
        });

        //-- test
        findViewById(R.id.button2).setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, DummyActivity.class);
                startActivity(intent);
            }
        });

        findViewById(R.id.bt_test).setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //非同期-DBアクセス
                //new DataStoreAsyncTask(db, DataStoreAsyncTask.DB_OPERATION.READ, findViewById(R.id.ll_rootCreatedTask)).execute();
            }
        });
        //-- test

        //登録されている「やること」を表示
        this.displayTaskList();
    }

    /*
     * タスク生成ダイアログの生成
     */
    private void createTaskDialog(){
        //Bundle生成
        Bundle bundle = new Bundle();
        //FragmentManager生成
        FragmentManager transaction = getSupportFragmentManager();

        //ダイアログを生成
        DialogFragment dialog = new CreateTaskDialog();
        dialog.setArguments(bundle);
        dialog.show(transaction, "CreateTask");
    }

    /*
     * 「やること」の表示
     *    登録済みの「やること」データを全て表示する。
     */
    private void displayTaskList(){

        //非同期スレッドにて、読み込み開始
        new DataStoreAsyncTask(this.db, this, DataStoreAsyncTask.DB_OPERATION.READ).execute();
    }


    /*
     *  ---------------------------
     *  リスナー
     *  ---------------------------
     */

    /*
     * 「やること」操作リスナー
     */
    private class TaskCtrlListener implements View.OnClickListener{

        boolean isDisplay;              //操作アイコンの表示状態
        LinearLayout ll_targetView;     //操作対象ビュー
        Animation animation_in;         //アニメーション

        TaskCtrlListener(LinearLayout view) {
            this.isDisplay = false;
            this.ll_targetView = view;
            this.animation_in = AnimationUtils.loadAnimation(MainActivity.this, R.anim.unit_open_ctrl);
        }

        @Override
        public void onClick(View view) {

            //-- 操作UIの表示/非表示
            //表示中
            if(isDisplay){
                //-- 隠す
                this.ll_targetView.setVisibility(View.GONE);
                //control.startAnimation(animation_out);
                this.isDisplay = false;

            //非表示中
            }else{
                //-- 表示する
                this.ll_targetView.setVisibility(View.VISIBLE);
                this.ll_targetView.startAnimation(this.animation_in);
                this.isDisplay = true;
            }
        }
    }

    /*
     * 「やること」-追加アイコンリスナー
     */
    private class TaskCtrlAddListener implements View.OnClickListener {

        @Override
        public void onClick(View view) {

        }
    }


    /*
     * 「やること」-編集アイコンリスナー
     */
    private class TaskCtrlEditListener implements View.OnClickListener {

        private View rootView;      //「やること」レイアウトのビュー

        /*
         * コンストラクタ
         */
        public TaskCtrlEditListener( View v ){
            this.rootView = v;
        }

        @Override
        public void onClick(View view) {
            //---- 更新ダイアログを生成

            //--選択されたやることをダイアログへ渡す
            //「やること」情報
            String taskName = ((TextView)this.rootView.findViewById(R.id.tv_taskName)).getText().toString();
            String taskTimeStr = ((TextView)this.rootView.findViewById(R.id.tv_taskTime)).getText().toString();

            taskTimeStr = taskTimeStr.replace(" min", "");
            int taskTime = Integer.parseInt(taskTimeStr);

            //渡すデータ設定
            Bundle bundle = new Bundle();
            bundle.putString("TaskName", taskName);
            bundle.putInt("TaskTime", taskTime);

            //FragmentManager生成
            FragmentManager transaction = getSupportFragmentManager();

            //ダイアログを生成
            DialogFragment dialog = new CreateTaskDialog();
            dialog.setArguments(bundle);
            dialog.show(transaction, "UpdateTask");
        }
    }

    /*TaskCtrlDeleteListener
     * 「やること」-削除アイコンリスナー
     */
    private class TaskCtrlDeleteListener implements View.OnClickListener {

        private View rootView;      //「やること」レイアウトのビュー

        public TaskCtrlDeleteListener( View v ){
            this.rootView = v;
        }

        @Override
        public void onClick(View view) {
            //---- 削除確認ダイアログを生成

            //--選択されたやることをダイアログへ渡す
            //「やること」情報
            String taskName = ((TextView)this.rootView.findViewById(R.id.tv_taskName)).getText().toString();
            String taskTimeStr = ((TextView)this.rootView.findViewById(R.id.tv_taskTime)).getText().toString();

            taskTimeStr = taskTimeStr.replace(" min", "");
            int taskTime = Integer.parseInt(taskTimeStr);

            //渡すデータ設定
            Bundle bundle = new Bundle();
            bundle.putString("TaskName", taskName);
            bundle.putInt("TaskTime", taskTime);

            //FragmentManager生成
            FragmentManager transaction = getSupportFragmentManager();

            //ダイアログを生成
            DialogFragment dialog = new DeleteTaskDialog();
            dialog.setArguments(bundle);
            dialog.show(transaction, "DeleteTask");
        }
    }

    /*
     * DB非同期処理の実行結果
     *   非同期処理のインターフェースとして実装
     */
    @Override
    public void onSuccessRead(List<TaskTable> taskList) {

        //DBから取得した「やること」を表示
        for( TaskTable task: taskList ) {

            //--「やること」のレイアウトインフレータを取得
            LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            //「やること」データのビュー
            View taskLayout = inflater.inflate(R.layout.unit_task, null);

            //-- 「やること」にリスナーを登録
            //リスナー設定ビューの取得
            LinearLayout ll_taskInfo = taskLayout.findViewById(R.id.ll_taskInfo);
            //操作対象ビューの取得
            LinearLayout ll_taskCtrl = taskLayout.findViewById(R.id.ll_taskCtrl);
            //リスナー設定
            ll_taskInfo.setOnClickListener(
                    new TaskCtrlListener(ll_taskCtrl)
            );

            //-- 「やること」データの表示内容を設定
            //「やること」
            TextView tv_data = taskLayout.findViewById(R.id.tv_taskName);
            tv_data.setText(task.getTaskName());
            //「やること」の時間
            tv_data = taskLayout.findViewById(R.id.tv_taskTime);
            tv_data.setText(task.getTaskTime() + " min");

            //-- 「やること」データの操作用リスナーを設定
            //アイコン-追加
            Button bt_ctrl = (Button) taskLayout.findViewById(R.id.bt_addPreparation);
            bt_ctrl.setOnClickListener(
                    new TaskCtrlAddListener()
            );

            //アイコン-編集
            bt_ctrl = (Button) taskLayout.findViewById(R.id.bt_editTask);
            bt_ctrl.setOnClickListener(
                    new TaskCtrlEditListener( (View)taskLayout )
            );

            //アイコン-削除
            bt_ctrl = (Button) taskLayout.findViewById(R.id.bt_deleteTask);
            bt_ctrl.setOnClickListener(
                    new TaskCtrlDeleteListener( (View)taskLayout )
            );

            //-- 「やること」データを表示先のビューに追加
            LinearLayout ll_rootDisplay = (LinearLayout) findViewById(R.id.ll_rootCreatedTask);
            ll_rootDisplay.addView( taskLayout );
        }
    }

    /*
     * DB非同期処理の実行結果
     *   非同期処理のインターフェースとして実装
     */
    @Override
    public void onSuccessCreate(Integer code) {

        //結果メッセージ
        String message;

        //戻り値に応じてトースト表示
        if( code == -1 ){
            //エラーメッセージを表示
            message = "登録済みです";

        } else {
            //正常メッセージを表示
            message = "登録しました";
        }

        //結果を表示
        Toast toast = new Toast(getApplicationContext());
        toast.setText(message);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();

        return;
    }

    /*
     * DB非同期処理の実行結果
     *   非同期処理のインターフェースとして実装
     */
    @Override
    public void onSuccessDelete() {

        //-- 削除した「やること」のレイアウトを削除
        Log.i("test", "onSuccessDelete");

        return;
    }

    /*
     * DB非同期処理の実行結果
     *   非同期処理のインターフェースとして実装
     */
    @Override
    public void onSuccessUpdate() {

        //-- 削除した「やること」のレイアウトを削除
        Log.i("test", "onSuccessUpdate");

        return;
    }

}