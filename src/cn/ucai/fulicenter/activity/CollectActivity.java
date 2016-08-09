package cn.ucai.fulicenter.activity;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import cn.ucai.fulicenter.D;
import cn.ucai.fulicenter.FuLiCenterApplication;
import cn.ucai.fulicenter.I;
import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.adapter.CollectAdapter;
import cn.ucai.fulicenter.adapter.GoodAdapter;
import cn.ucai.fulicenter.bean.CollectBean;
import cn.ucai.fulicenter.bean.NewGoodBean;
import cn.ucai.fulicenter.data.OkHttpUtils2;
import cn.ucai.fulicenter.utils.Utils;
import cn.ucai.fulicenter.view.DisplayUtils;

public class CollectActivity extends Activity {
    private  final  static  String TAG=CollectActivity.class.getSimpleName();
    CollectActivity mContext;
    SwipeRefreshLayout mSwipeRefreshLayout;
    RecyclerView mRecyclerView;
    GridLayoutManager mGridLayoutManager;
    CollectAdapter  mGoodAdapter;
    List<CollectBean> mGoodList;

    int pageId=0;
    int goodId;
    TextView tvRefreshing;
    int action=I.ACTION_DOWNLOAD;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collect);
        mContext=this;
        mGoodList=new ArrayList<>();
        initView();
        initData();
        setListener();
    }

    private void setListener() {
        setPullDownListener();
        setPullUpRefreshListener();
        setUpdateCollectListListener();
    }

    private void setPullUpRefreshListener() {
        mRecyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            int lastItemPositon;
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                Log.e(TAG,"newState="+newState);
                if (newState==RecyclerView.SCROLL_STATE_IDLE
                        &&lastItemPositon==mGoodAdapter.getItemCount()-1){
                    if (mGoodAdapter.isMore()){
                        action=I.ACTION_PULL_UP;
                        pageId+=I.PAGE_ID_DEFAULT;
                        initData();
                    }
                }
            }
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                lastItemPositon=mGridLayoutManager.findLastVisibleItemPosition();
            }
        });

    }

    private void setPullDownListener() {
       mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
           @Override
           public void onRefresh() {
               mSwipeRefreshLayout.setRefreshing(true);
               pageId=1;
               initData();
           }
       });
    }

    private void initData() {
        String userName = FuLiCenterApplication.getInstance().getUserName();
        Log.e(TAG,"username="+userName);
        if (userName.isEmpty()){
            finish();
        }

            findCollectList(new OkHttpUtils2.OnCompleteListener<String>() {
                @Override
                public void onSuccess(String result) {
                    mSwipeRefreshLayout.setRefreshing(false);
                    tvRefreshing.setVisibility(View.GONE);
                    mGoodAdapter.setMore(true);
                 //   mGoodAdapter.setFooterText("加载更多");
                    if (result!=null){
                        Log.e(TAG,"result="+result);
                        Gson gson=new Gson();
                        CollectBean[] newGoodBeen = gson.fromJson(result, CollectBean[].class);
                        ArrayList<CollectBean> collectBeanArrayList = Utils.array2List(newGoodBeen);
                        if (action==I.ACTION_PULL_DOWN||action==I.ACTION_DOWNLOAD){
                            mGoodAdapter.initItem(collectBeanArrayList);
                        }else {
                            mGoodAdapter.addItem(collectBeanArrayList);
                        }
                        if (collectBeanArrayList.size()<I.PAGE_SIZE_DEFAULT){
                            mGoodAdapter.setMore(false);
                         // mGoodAdapter.setFooterText("没有更多加载");
                        }
                    }else {
                        mGoodAdapter.setMore(false);
                       // mGoodAdapter.setFooterText("没有更多");
                    }
                }
                @Override
                public void onError(String error) {
                    Log.e(TAG,"error="+error);
                    mSwipeRefreshLayout.setRefreshing(false);
                    tvRefreshing.setVisibility(View.GONE);
                }
            });
        }



    private  void findCollectList(OkHttpUtils2.OnCompleteListener<String> listener){
        final  OkHttpUtils2<String> utils=new OkHttpUtils2<>();
        utils.setRequestUrl(I.REQUEST_FIND_COLLECTS)
                .addParam(I.Collect.USER_NAME, FuLiCenterApplication.getInstance().getUserName())
                .addParam(I.PAGE_ID,String.valueOf(pageId))
                .addParam(I.PAGE_SIZE,String.valueOf(I.PAGE_SIZE_DEFAULT))
                .targetClass(String.class)
                .execute(listener);
    }

    private void initView() {
        DisplayUtils.initBackTitle(mContext,"收藏的宝贝");
       mSwipeRefreshLayout= (SwipeRefreshLayout) findViewById(R.id.srl_collect);

        mSwipeRefreshLayout.setColorSchemeColors(
                Color.RED

        );
        mRecyclerView = (RecyclerView) findViewById(R.id.rv_collect);
        mGridLayoutManager=new GridLayoutManager(mContext,2);
        mGridLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(mGridLayoutManager);
        mGoodAdapter=new CollectAdapter(mContext,mGoodList);
        mRecyclerView.setAdapter(mGoodAdapter);
        tvRefreshing = (TextView) findViewById(R.id.tvRefreshHint);
    }
    class UpdateCollectListReceiver extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {
            initData();
        }
    }
    UpdateCollectListReceiver mReceiver;
    private  void setUpdateCollectListListener(){
        mReceiver=new UpdateCollectListReceiver();
        IntentFilter filter=new IntentFilter("update_collec_list");
        registerReceiver(mReceiver,filter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mReceiver!=null){
            unregisterReceiver(mReceiver);
        }
    }
}
