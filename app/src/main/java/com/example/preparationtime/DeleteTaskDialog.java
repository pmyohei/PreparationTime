package com.example.preparationtime;

import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.NumberPicker;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;


/*
 * 「やること」新規生成のダイアログ
 */
public class DeleteTaskDialog extends DialogFragment {

    public DeleteTaskDialog() {
        // Required empty public constructor
    }
    /*
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //ダイアログにレイアウトを設定
        return inflater.inflate(R.layout.dialog_create_task, container, false);
    }
    */

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){
        //Builder取得
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        //--呼び出し元から情報を取得
        String taskName = getArguments().getString("TaskName");
        int taskTime    = getArguments().getInt("TaskTime");

        Log.i("test", "delete=" + taskName + taskTime);

        //表示内容の設定
        builder.setMessage("削除しますか？" + taskName + " " + taskTime + " min")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //DBから削除
                        AppDatabase db = AppDatabaseSingleton.getInstanceNotFirst();
                        new DataStoreAsyncTask(db, (MainActivity) getActivity(), DataStoreAsyncTask.DB_OPERATION.DELETE, taskName, taskTime).execute();
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //do nothing
                    }
                });

        //ダイアログを生成し、返す
        return builder.create();
    }

    /*
    @Override
    public void onStart() {
        super.onStart();

        //ダイアログ取得
        Dialog dialog = getDialog();

        //-- ダイアログデザインの設定
        //画面メトリクスの取得
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        //レイアウトパラメータの取得
        WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
        lp.width   = metrics.widthPixels;                   //横幅=画面サイズ
        lp.gravity = Gravity.BOTTOM;                        //位置=画面下部
        //ダイアログのデザインとして設定
        dialog.getWindow().setAttributes(lp);

        //---- ビューの設定も本メソッドで行う必要あり（onCreateDialog()内だと落ちる）
        //-- NumberPicker の設定
        //ビュー取得
        NumberPicker np100th = (NumberPicker) dialog.findViewById(R.id.np_dialogTime100th);
        NumberPicker np10th  = (NumberPicker) dialog.findViewById(R.id.np_dialogTime10th);
        NumberPicker np1th   = (NumberPicker) dialog.findViewById(R.id.np_dialogTime1th);
        //値の範囲を設定
        np100th.setMaxValue(9);
        np100th.setMinValue(0);
        np10th.setMaxValue(9);
        np10th.setMinValue(0);
        np1th.setMaxValue(9);
        np1th.setMinValue(0);

        //-- 「保存ボタン」のリスナー設定
        Button btEntry = (Button)dialog.findViewById(R.id.bt_entryTask);
        btEntry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //-- DBへ保存
                //タスク名を取得
                String task = ((EditText)dialog.findViewById(R.id.et_dialogTask)).getText().toString();

                //時間を取得
                int time;
                NumberPicker inputTime = (NumberPicker) dialog.findViewById(R.id.np_dialogTime100th);
                time = inputTime.getValue() * 100;
                inputTime = (NumberPicker) dialog.findViewById(R.id.np_dialogTime10th);
                time += inputTime.getValue() * 10;
                inputTime = (NumberPicker) dialog.findViewById(R.id.np_dialogTime1th);
                time += inputTime.getValue();

                //DBへ保存
                AppDatabase db = AppDatabaseSingleton.getInstanceNotFirst();
                new DataStoreAsyncTask(db, DataStoreAsyncTask.DB_OPERATION.CREATE, task, time).execute();
            }
        });

        return;
    }
    */
}