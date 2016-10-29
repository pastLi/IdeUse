package com.feicui.android.ideuse;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;

import com.feicui.android.ideuse.commons.ToastUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by Administrator on 2016/10/28 0028.
 */

public class RecyclerViewActivity extends AppCompatActivity {
    @BindView(R.id.recycle_rcv) RecyclerView recyclerView;

    private Unbinder unbinder;
    private RecyclerAdapter recyclerAdapter;//适配器
    private List<String> mData;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycleview);
        unbinder = ButterKnife.bind(this);
        mData = new ArrayList<>();
        for (int i = 0; i < 30; i++) {
            mData.add("Item  "  + i);
        }
        //初始化适配器
        recyclerAdapter = new RecyclerAdapter(getApplicationContext(),mData);

        //设置item监听
        recyclerAdapter.setOnItemCikckListener(new RecyclerAdapter.OnItemClickListener() {
            @Override
            public void onClick(int position) {
                ToastUtils.showShort("点击了==" + position + "==item");
            }

            @Override
            public void onLongClick(int position) {
                ToastUtils.showShort("长按了==" + position + "==item");
            }
        });
        //设置布局
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(OrientationHelper.VERTICAL);
//        layoutManager.setOrientation(OrientationHelper.HORIZONTAL);
//        GridLayoutManager layoutManager = new GridLayoutManager(this,3);
        recyclerView.setLayoutManager(layoutManager);
        //设置adapter
        recyclerView.setAdapter(recyclerAdapter);
        //设置分割线
        recyclerView.addItemDecoration(new AAADivider(this,OrientationHelper.VERTICAL));
        //设置item动画
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        //点击事件接口实现（两个）

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();//解绑
    }

    //添加item
    @OnClick(R.id.recycle_add)
    public void add(){
        recyclerAdapter.addData(1);
    }
    //删除item
    @OnClick(R.id.recycle_delete)
    public void delete(){
        recyclerAdapter.removeData(1);
    }

}
