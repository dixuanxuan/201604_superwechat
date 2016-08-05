package cn.ucai.fulicenter.activity;

import android.app.Activity;
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
import cn.ucai.fulicenter.I;
import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.adapter.GoodAdapter;
import cn.ucai.fulicenter.bean.NewGoodBean;
import cn.ucai.fulicenter.data.OkHttpUtils2;
import cn.ucai.fulicenter.utils.Utils;
import cn.ucai.fulicenter.view.DisplayUtils;

public class CategoryChildActivity extends Activity {
    private  final  static  String TAG=CategoryChildActivity.class.getSimpleName();
    CategoryChildActivity mContext;
    SwipeRefreshLayout mSwipeRefreshLayout;
    RecyclerView mRecyclerView;
    GridLayoutManager mGridLayoutManager;
    GoodAdapter mGoodAdapter;
    List<NewGoodBean> mGoodList;

    int pageId=0;
    int goodId;
    TextView tvRefreshing;
    int action=I.ACTION_DOWNLOAD;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_child);
        mContext=this;
        mGoodList=new ArrayList<>();
        initView();
        initData();
        setListener();
    }

    private void setListener() {
        setPullDownListener();
        setPullUpRefreshListener();
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
        goodId=getIntent().getIntExtra(I.NewAndBoutiqueGood.CAT_ID,0);
        if (goodId>0){
            findViewGoodList(new OkHttpUtils2.OnCompleteListener<String>() {
                @Override
                public void onSuccess(String result) {
                    mSwipeRefreshLayout.setRefreshing(false);
                    tvRefreshing.setVisibility(View.GONE);
                    mGoodAdapter.setMore(true);
                 //   mGoodAdapter.setFooterText("加载更多");
                    if (result!=null){
                        Log.e(TAG,"result="+result);
                        Gson gson=new Gson();
                        NewGoodBean[] newGoodBeen = gson.fromJson(result, NewGoodBean[].class);
                        ArrayList<NewGoodBean> goodBeanArrayList = Utils.array2List(newGoodBeen);
                        if (action==I.ACTION_PULL_DOWN||action==I.ACTION_DOWNLOAD){
                            mGoodAdapter.initItem(goodBeanArrayList);
                        }else {
                            mGoodAdapter.addItem(goodBeanArrayList);
                        }
                        if (goodBeanArrayList.size()<I.PAGE_SIZE_DEFAULT){
                            mGoodAdapter.setMore(false);
                            mGoodAdapter.setFooterText("没有更多加载");
                        }
                    }else {
                        mGoodAdapter.setMore(false);
                        mGoodAdapter.setFooterText("没有更多");
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

    }

    private  void findViewGoodList(OkHttpUtils2.OnCompleteListener<String> listener){
        final  OkHttpUtils2<String> utils=new OkHttpUtils2<>();
        utils.setRequestUrl(I.REQUEST_FIND_GOODS_DETAILS)
                .addParam(I.NewAndBoutiqueGood.CAT_ID,String.valueOf(goodId))
                .addParam(I.PAGE_ID,String.valueOf(pageId))
                .addParam(I.PAGE_SIZE,String.valueOf(I.PAGE_SIZE_DEFAULT))
                .targetClass(String.class)
                .execute(listener);
    }

    private void initView() {
//        String name=getIntent().getStringExtra(D.Category.KEY_NAME);
//        DisplayUtils.initBackTitle(mContext,name);
       mSwipeRefreshLayout= (SwipeRefreshLayout) findViewById(R.id.srlCategoryChild);
        mSwipeRefreshLayout.setColorSchemeColors(
                R.color.google_yellow,
                R.color.google_red,
                R.color.google_green,
                R.color.google_blue
        );
        mRecyclerView = (RecyclerView) findViewById(R.id.rvCategoryChild);
        mGridLayoutManager=new GridLayoutManager(mContext,2);
        mGridLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(mGridLayoutManager);
        mGoodAdapter=new GoodAdapter(mContext,mGoodList);
        mRecyclerView.setAdapter(mGoodAdapter);
        tvRefreshing = (TextView) findViewById(R.id.tvRefreshHint);
    }

}
