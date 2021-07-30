package com.example.preparationtime;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class MainActivity extends AppCompatActivity implements AsyncTaskTableOperaion.TaskOperationListener {

    private AppDatabase     db;                     //DB
    private LinearLayout    ll_rootDisplay;         //「やること」表示元のビューID

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //-- レイアウト関係
        //「やること」表示元のビューIDを保持
        this.ll_rootDisplay = (LinearLayout) findViewById(R.id.ll_rootCreatedTask);

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
                Intent intent = new Intent(MainActivity.this, SetManageActivity.class);
                startActivity(intent);
            }
        });

        findViewById(R.id.bt_task_manage).setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, TaskManageActivity.class);
                startActivity(intent);
            }
        });
        //-- test

        //現在登録されている「やること」を表示
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
        new AsyncTaskTableOperaion(this.db, this, AsyncTaskTableOperaion.DB_OPERATION.READ).execute();
    }

    /*
     * 「やること」ビューの作成・レイアウトへの追加
     *    「やること」データ単体のレイアウトを作り、ルートレイアウトへ追加する。
     */
    private void addDisplayUnitTask(TaskTable task){

        //--「やること」のレイアウトインフレータを取得
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        //「やること」データのビュー
        View taskLayout = inflater.inflate(R.layout.item_task_old, null);

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

        //「やること」データを表示先のビューに追加
        this.ll_rootDisplay.addView( taskLayout );
    }

    /*
     * 「やること」ビューの取得
     *    「やること」表示レイアウトから、指定された「やること」のビューを表示する
     */
    private View findTaskViewFromRoot(String task, int taskTime) {

        //表示中の「やること」をすべて走査する
        int num = this.ll_rootDisplay.getChildCount();
        for (int i = 0; i < num; i++) {

            //-- 「やること」レイアウトから、「やること」「やること時間」を取得
            LinearLayout ll_unit = (LinearLayout)this.ll_rootDisplay.getChildAt(i);
            //「やること」
            String v_task = ((TextView)ll_unit.findViewById(R.id.tv_taskName)).getText().toString();
            //「やること時間」（int型に変換）
            String taskTimeStr = ((TextView)ll_unit.findViewById(R.id.tv_taskTime)).getText().toString();
            taskTimeStr = taskTimeStr.replace(" min", "");
            int v_taskTime = Integer.parseInt(taskTimeStr);

            //検索対象と一致するか
            if( (v_task.equals(task)) && (v_taskTime == taskTime) ){
                //一致すれば、そのビューを返す
                return (View)ll_unit;
            }
        }

        return null;
    }

    /*
     *  -------------------------------------------------
     *  リスナー
     *  -------------------------------------------------
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
     *  -------------------------------------------------
     *  インターフェース
     *  -------------------------------------------------
     */

    /*
     * DB非同期処理の実行結果：表示
     *   非同期処理のインターフェースとして実装
     */
    @Override
    public void onSuccessTaskRead(List<TaskTable> taskList) {

        //DBから取得した「やること」をすべて表示
        for( TaskTable task: taskList ) {
            //「やること」をレイアウトに追加
            this.addDisplayUnitTask(task);
        }
    }

    /*
     * DB非同期処理の実行結果：新規生成
     *   非同期処理のインターフェースとして実装
     */
    @Override
    public void onSuccessTaskCreate(Integer code, String task, int taskTime) {

        //-- 作成結果をトーストで表示
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

        //トーストの生成
        Toast toast = new Toast(getApplicationContext());
        toast.setText(message);
        //toast.setGravity(Gravity.CENTER, 0, 0);   //E/Toast: setGravity() shouldn't be called on text toasts, the values won't be used
        toast.show();

        if( code == -1 ){
            //登録済みなら、ここで終了
            return;
        }

        //-- 新規作成された「やること」の表示
        this.addDisplayUnitTask(new TaskTable(task, taskTime));

        return;
    }

    /*
     * DB非同期処理の実行結果：削除
     *   非同期処理のインターフェースとして実装
     */
    @Override
    public void onSuccessTaskDelete(String task, int taskTime) {

        //-- 削除した「やること」のレイアウトを削除

        //削除対象のビューを取得
        View view = this.findTaskViewFromRoot(task, taskTime);
        //削除
        if( view != null ){
            this.ll_rootDisplay.removeView(view);
        }

        return;
    }

    /*
     * DB非同期処理の実行結果：更新
     *   非同期処理のインターフェースとして実装
     */
    @Override
    public void onSuccessTaskUpdate(String preTask, int preTaskTime, String task, int taskTime) {

        //-- 削除した「やること」のレイアウトを削除
        Log.i("test", "onSuccessUpdate");

        //更新対象のビューを取得
        View view = this.findTaskViewFromRoot(preTask, preTaskTime);
        //更新
        if( view != null ){
            //「やること」「やること時間」の更新
            ((TextView)view.findViewById(R.id.tv_taskName)).setText(task);
            ((TextView)view.findViewById(R.id.tv_taskTime)).setText(taskTime + " min");
        }

        return;
    }


}