package com.example.preparationtime;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

/*
 * RecyclerViewアダプター：「やること」用
 */
public class TaskRecyclerAdapter extends RecyclerView.Adapter<TaskRecyclerAdapter.ViewHolder> {

    private List<TaskTable> mData;
    private Context         mContext;
    //private OnRecyclerListener mListener;

    /*
     * コンストラクタ
     */
    public TaskRecyclerAdapter(Context context, List<TaskTable> data) {
        this.mData    = data;
        this.mContext = context;
        //mListener = listener;
        Log.i("test", "mData=" + mData);
    }

    /*
     *　ViewHolderの生成
     */
    @Override
    public TaskRecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        //表示レイアウトの設定
        LayoutInflater inflater = LayoutInflater.from(this.mContext);
        View view = inflater.inflate(R.layout.unit_task, viewGroup, false);

        Log.i("test", "onCreateViewHolder i=" + i);

        return new ViewHolder(view);
    }

    /*
     * ViewHolderの設定
     */
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

    /*
     * 表示データ数の取得
     */
    @Override
    public int getItemCount() {
        //表示データ数を返す
        return mData.size();
    }


    /*
     * ViewHolder：リスト内の各アイテムのレイアウトを含む View のラッパー
     * (固有のためインナークラスで定義)
     */
    class ViewHolder extends RecyclerView.ViewHolder {
        //表示内容
        TextView task;
        TextView taskTime;

        /*
         * コンストラクタ
         */
        public ViewHolder(View itemView) {
            super(itemView);
            this.task     = (TextView) itemView.findViewById(R.id.tv_taskName);
            this.taskTime = (TextView) itemView.findViewById(R.id.tv_taskTime);
        }
    }

}

