package com.example.preparationtime;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.NumberPicker;

import androidx.fragment.app.DialogFragment;


/*
 * 「やること」新規生成のダイアログ
 */
public class CreateTaskDialog extends DialogFragment {

    public CreateTaskDialog() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //ダイアログにレイアウトを設定
        return inflater.inflate(R.layout.dialog_create_task, container, false);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){
        //ダイアログ取得
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        //背景を透明にする(デフォルトテーマに付いている影などを消す) ※これをしないと、画面横サイズまで拡張されない
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        //アニメーションを設定
        dialog.getWindow().getAttributes().windowAnimations = R.style.dialogAnimation;

        //ダイアログを返す
        return dialog;
    }

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

}
