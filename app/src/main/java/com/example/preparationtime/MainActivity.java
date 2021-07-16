package com.example.preparationtime;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //やること新規作成ボタン
        Button createTask = (Button)findViewById(R.id.bt_createTask);
        createTask.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //やること登録ダイアログの生成
                createTaskDialog();
            }
        });

        //test
        findViewById(R.id.bt_test).setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //シングルトンでのDBアクセス
                new DataStoreAsyncTask(DataStoreAsyncTask.DB_OPERATION.CREATE, "test", 10).execute();
            }
        });
        //test

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
                new TaskCtrlListener( ll_taskCtrl )
        );

        //-- 「やること」データの表示内容を設定
        //「やること」
        TextView tv_data = taskLayout.findViewById(R.id.tv_taskName);
        tv_data.setText("test");
        //「やること」の時間
        tv_data = taskLayout.findViewById(R.id.tv_taskTime);
        tv_data.setText("test min");

        //-- 「やること」データの操作用リスナーを設定
        //アイコン-追加
        Button bt_ctrl = (Button) taskLayout.findViewById(R.id.bt_addPreparation);
        bt_ctrl.setOnClickListener(
                new TaskCtrlAddListener()
        );

        //アイコン-編集
        bt_ctrl = (Button) taskLayout.findViewById(R.id.bt_editTask);
        bt_ctrl.setOnClickListener(
                new TaskCtrlEditListener()
        );

        //アイコン-削除
        bt_ctrl = (Button) taskLayout.findViewById(R.id.bt_deleteTask);
        bt_ctrl.setOnClickListener(
                new TaskCtrlDeleteListener()
        );

        //-- 「やること」データを表示先のビューに追加
        LinearLayout ll_rootDisplay = (LinearLayout)findViewById(R.id.ll_rootCreatedTask);
        ll_rootDisplay.addView(taskLayout);
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

        @Override
        public void onClick(View view) {

        }
    }

    /*
     * 「やること」-削除アイコンリスナー
     */
    private class TaskCtrlDeleteListener implements View.OnClickListener {

        @Override
        public void onClick(View view) {

        }
    }
}