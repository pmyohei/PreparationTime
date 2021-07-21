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

        //呼び出し元から情報を取得
        String taskName = getArguments().getString("TaskName");
        int taskTime    = getArguments().getInt("TaskTime");

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

}
