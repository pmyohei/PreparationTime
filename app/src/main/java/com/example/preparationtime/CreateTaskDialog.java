package com.example.preparationtime;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import androidx.appcompat.app.AlertDialog;
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

        // タイトル非表示
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        // フルスクリーン
        dialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN);
        // 背景を透明にする(デフォルトテーマに付いている影などを消す)
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        //アニメーションを設定
        dialog.getWindow().getAttributes().windowAnimations = R.style.dialogAnimation;

        //-- ダイアログデザインの設定
        //画面メトリクスの取得
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        //レイアウトパラメータの取得
        WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
        lp.width = metrics.widthPixels;                 //横幅=画面サイズ
        lp.gravity = Gravity.BOTTOM;                    //位置=画面下部
        //ダイアログのデザインとして設定
        dialog.getWindow().setAttributes(lp);

        //ダイアログを返す
        return dialog;
    }

}
