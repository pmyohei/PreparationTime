package com.example.preparationtime;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button createTask = (Button)findViewById(R.id.bt_createTask);
        createTask.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createTaskDialog();
            }
        });



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



}