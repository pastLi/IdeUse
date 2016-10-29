package com.feicui.android.ideuse;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

//import com.feicui.android.ideuse.R;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2016/10/28 0028.
 */

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.MyViewHoder> {

    private List<String> mDatas;
    private Context context;
    private LayoutInflater inflater;//填充器，用来填充布局
    private OnItemClickListener onItemClickListener;

    public RecyclerAdapter(Context context,List<String> mDatas){
        this.context = context;
        this.mDatas = mDatas;
        inflater = LayoutInflater.from(context);
    }
    //重写onCreateViewHolder方法，返回一个自定义的ViewHolder
    @Override
    public MyViewHoder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.view_recycle_item,parent,false);
        MyViewHoder myViewHoder =new MyViewHoder(view);
        return myViewHoder;
    }

    //填充onCreateViewHolder方法返回的holder中的控件
    @Override
    public void onBindViewHolder(MyViewHoder holder, int position) {
        holder.mTv.setText(mDatas.get(position));
    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }

    class MyViewHoder extends RecyclerView.ViewHolder{
        @BindView(R.id.item_tv)
        TextView mTv;
        @BindView(R.id.item_btn)
        Button mBtn;
        public MyViewHoder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }

    //添加数据
    public void addData(int position){
        mDatas.add(position,"Insert One");
        notifyItemChanged(position);
        notifyItemRangeChanged(position,mDatas.size());//批量更新
    }
    //删除数据
    public void removeData(int position){
        mDatas.remove(position);
        notifyItemChanged(position);
        notifyItemRangeChanged(position,mDatas.size());//批量更新
    }

    //添加点击事件监听
    public interface OnItemClickListener{
        //点击
        void onClick(int position);
        //长按
        void onLongClick(int position);
    }

    public void setOnItemCikckListener(OnItemClickListener onItemClickListener){
        this.onItemClickListener = onItemClickListener;
    }
}
