package com.example.preparationtime;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class SetManageActivity extends AppCompatActivity implements AsyncSetTableOperaion.SetOperationListener, AsyncTaskTableOperaion.TaskOperationListener {

    private AppDatabase     db;                     //DB
//    private LinearLayout    ll_rootDisplay;         //「やること」表示元のビューID

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_manage);
        //-- レイアウト関係

        //「やること」表示元のビューIDを保持
        //this.ll_rootDisplay = (LinearLayout) findViewById(R.id.ll_rootCreatedTask);

        //DB操作インスタンスを取得
        this.db = AppDatabaseSingleton.getInstance(getApplicationContext());

        //-- test
        findViewById(R.id.bt_createTask).setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //やることセット登録ダイアログの生成
                createTaskSetDialog();
            }
        });
        //--

        //現在登録されている「やること」を表示
        this.displaySetList();
    }

    /*
     * タスク生成ダイアログの生成
     */
    private void createTaskSetDialog(){
        //Bundle生成
        Bundle bundle = new Bundle();
        //FragmentManager生成
        FragmentManager transaction = getSupportFragmentManager();

        //ダイアログを生成
        DialogFragment dialog = new CreateSetDialog();
        dialog.setArguments(bundle);
        dialog.show(transaction, "CreateTask");
    }

    /*
     * 「やること」の表示
     *    登録済みの「やること」データを全て表示する。
     */
    private void displaySetList(){

        //-- 非同期スレッドにて、読み込み開始
        //「やること」
        new AsyncTaskTableOperaion(this.db, this, AsyncTaskTableOperaion.DB_OPERATION.READ).execute();
        //「やることセット」
        new AsyncSetTableOperaion(this.db, this, AsyncSetTableOperaion.DB_OPERATION.READ).execute();
    }

    /*
     * 「やること」ビューの作成・レイアウトへの追加
     *    「やること」データ単体のレイアウトを作り、ルートレイアウトへ追加する。
     */
    private void addDisplayUnitTask(TaskTable task){
        /*
        //--「やること」のレイアウトインフレータを取得
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        //「やること」データのビュー
        View taskLayout = inflater.inflate(R.layout.unit_task_for_taskset, null);

        //-- 「やること」にリスナーを登録
        //リスナー設定ビューの取得
        LinearLayout ll_taskInfo = taskLayout.findViewById(R.id.ll_taskInfo);
        //リスナー設定
        ll_taskInfo.setOnClickListener(
                new TaskSelectListener()
        );

        ll_taskInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.i("test", "is=" + v.isEnabled());

                if(v.isEnabled()){
                    //有効 → 無効
                    Log.i("test", "false");
                    //v.setEnabled(false);
                } else {
                    //無効 → 有効
                    Log.i("test", "true");
                    v.setEnabled(true);
                }
            }
        });

        //-- 「やること」データの表示内容を設定
        //「やること」
        TextView tv_data = taskLayout.findViewById(R.id.tv_taskName);
        tv_data.setText(task.getTaskName());
        //「やること」の時間
        tv_data = taskLayout.findViewById(R.id.tv_taskTime);
        tv_data.setText(task.getTaskTime() + " min");

        //「やること」データを表示先のビューに追加
        this.ll_rootDisplay.addView( taskLayout );
        */
    }

    /*
     * 「やること」選択リスナー
     */
    private class TaskSelectListener implements View.OnClickListener{

        boolean isDisplay;              //操作アイコンの表示状態
        LinearLayout ll_targetView;     //操作対象ビュー
        Animation animation_in;         //アニメーション

        TaskSelectListener() {
            this.isDisplay = false;
            //this.ll_targetView = view;
            this.animation_in = AnimationUtils.loadAnimation(SetManageActivity.this, R.anim.unit_open_ctrl);
        }
        TaskSelectListener(LinearLayout view) {
            this.isDisplay = false;
            //this.ll_targetView = view;
            this.animation_in = AnimationUtils.loadAnimation(SetManageActivity.this, R.anim.unit_open_ctrl);
        }

        @Override
        public void onClick(View view) {
            /*
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
             */
        }
    }


    /*
     *  -------------------------------------------------
     *  インターフェース
     *  -------------------------------------------------
     */

    /* -------------------
     * 「やることセット」
     */

    @Override
    public void onSuccessSetRead(List<SetTable> taskSetList) {
        /*

         */

        //DBから取得した「やること」をすべて表示
        for( SetTable task: taskSetList ) {
            Log.i("test", "set=" + task.getSetName());
        }
    }

    @Override
    public void onSuccessSetCreate(Integer code, String task) {

    }

    @Override
    public void onSuccessSetDelete(String task) {

    }

    @Override
    public void onSuccessSetUpdate(String preTask, String task) {

    }

    /* -------------------
     * 「やること」
     */

    @Override
    public void onSuccessTaskRead(List<TaskTable> taskList) {
        /*
        //レイアウトからリストビューを取得
        ListView listView  = (ListView) findViewById(R.id.lv_task);
        //リストビューに付与するアダプタを生成
        BaseAdapter adapter = new TaskSelectListAdapter(this.getApplicationContext(), R.layout.unit_task_for_taskset, taskList);
        // ListViewにadapterをセット
        listView.setAdapter(adapter);
        */

        //レイアウトからリストビューを取得
        RecyclerView rv  = (RecyclerView) findViewById(R.id.rv_taskList);
        //レイアウトマネージャの生成
        LinearLayoutManager l_manager = new LinearLayoutManager(this);
        //横スクロール設定
        l_manager.setOrientation(LinearLayoutManager.HORIZONTAL);
        //レイアウトマネージャの設定
        rv.setLayoutManager(l_manager);
        //アダプタを生成
        TaskRecyclerAdapter adapter = new TaskRecyclerAdapter(taskList);
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

    /*
     *  -------------------------------------------------
     *  インナークラス
     *  -------------------------------------------------
     */

    private class TaskRecyclerAdapter extends RecyclerView.Adapter<TaskRecyclerAdapter.ViewHolder> {

        private List<TaskTable> mData;
        //private OnRecyclerListener mListener;

        /*
         * コンストラクタ
         */
        public TaskRecyclerAdapter(List<TaskTable> data) {
            mData = data;
            //mListener = listener;
            Log.i("test", "mData=" + mData);
        }

        @Override
        public TaskRecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            //表示レイアウトの設定
            LayoutInflater inflater = LayoutInflater.from(SetManageActivity.this);
            View view = inflater.inflate(R.layout.unit_task, viewGroup, false);

            Log.i("test", "onCreateViewHolder i=" + i);

            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ViewHolder viewHolder, final int i) {
            //データ表示
            viewHolder.task.setText(mData.get(i).getTaskName());
            viewHolder.taskTime.setText(mData.get(i).getTaskTime() + "min");

            //クリック処理
            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //mListener.onRecyclerClicked(v, i);
                }
            });

        }

        @Override
        public int getItemCount() {
            return mData.size();
        }

        // ViewHolder(固有ならインナークラスでOK)
        class ViewHolder extends RecyclerView.ViewHolder {

            TextView task;
            TextView taskTime;

            public ViewHolder(View itemView) {
                super(itemView);
                this.task     = (TextView) itemView.findViewById(R.id.tv_taskName);
                this.taskTime = (TextView) itemView.findViewById(R.id.tv_taskTime);
            }
        }

    }

    public interface OnRecyclerListener {
        void onRecyclerClicked(View v, int position);
    }
}