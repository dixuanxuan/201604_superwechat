package cn.ucai.fulicenter.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.gson.Gson;

import java.util.ArrayList;

import cn.ucai.fulicenter.I;
import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.activity.ChatActivity;
import cn.ucai.fulicenter.activity.FuliCenterMainActivity;
import cn.ucai.fulicenter.adapter.GoodAdapter;
import cn.ucai.fulicenter.bean.NewGoodBean;
import cn.ucai.fulicenter.data.OkHttpUtils2;

/**
 * A simple {@link Fragment} subclass.
 */
public class NewGoodFragment extends Fragment {
    final  static  int ACTION_DOWN_LOAD=0;
    final  static  int ACTION_PULL_UP=1;
    final  static  int ACTION_PULL_DOWN=2;
    FuliCenterMainActivity mContext;
    final  static  String TAG=NewGoodFragment.class.getSimpleName();
    SwipeRefreshLayout mSwipeRefreshLayout;
    RecyclerView mRecyclerView;
    ArrayList<NewGoodBean>goodList;
    GoodAdapter mGoodAdapter;
    GridLayoutManager gm;
    int pageId=1;
    TextView tvRefreshing;
    public NewGoodFragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mContext= (FuliCenterMainActivity) getContext();
        View view = View.inflate(mContext,R.layout.fragment_new_good, null);
        goodList=new ArrayList<NewGoodBean>();
        initView(view);
        initData();
        setListener();
        return  view;
    }

    private void setListener() {
        setOnPullDownListerner();
        mRecyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if(newState==RecyclerView.SCROLL_STATE_IDLE&&lastpotison>=mGoodAdapter.getItemCount()-1&&mGoodAdapter.isMore()){
                    pageId++;
                    downloadData(ACTION_PULL_UP,pageId);
                }
            }
            int lastpotison;
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                lastpotison=gm.findLastVisibleItemPosition();
            }
        });
    }

    private void setOnPullDownListerner() {
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mSwipeRefreshLayout.setEnabled(true);
                mSwipeRefreshLayout.setRefreshing(true);
                tvRefreshing.setVisibility(View.VISIBLE);
                pageId=1;
                downloadData(ACTION_PULL_DOWN,pageId);
            }
        });
    }

    private void initData() {
        downloadData(ACTION_DOWN_LOAD,pageId);
    }

    private void downloadData( final  int action,int pageId) {
         final  OkHttpUtils2<String> utils=new OkHttpUtils2<>();
        utils.url(I.SERVER_ROOT)
                .addParam(I.KEY_REQUEST,I.REQUEST_FIND_NEW_BOUTIQUE_GOODS)
                .addParam(I.NewAndBoutiqueGood.CAT_ID,I.CAT_ID+"")
                .addParam(I.PAGE_ID,pageId+"")
                .addParam(I.PAGE_SIZE,1+"")
                .targetClass(String.class)
                .execute(new OkHttpUtils2.OnCompleteListener<String>() {
                    @Override
                    public void onSuccess(String result) {
                        if(result!=null&&result.length()!=0){
                            Gson gson=new Gson();
                            NewGoodBean[] goodBeen = gson.fromJson(result, NewGoodBean[].class);
                            Log.e(TAG,"goodBean="+goodBeen.toString());
                            mGoodAdapter.setMore(!(goodBeen==null||goodBeen.length==0));
                            if (!mGoodAdapter.isMore()){
                                mGoodAdapter.setFooterText("没有更多加载");
                                return;
                            }
                            ArrayList<NewGoodBean> been = OkHttpUtils2.array2List(goodBeen);
                            switch (action){
                                case ACTION_DOWN_LOAD:
                                    mGoodAdapter.addNewGood(been);
                                    mGoodAdapter.setFooterText("加载更多");
                                    break;
                                case  ACTION_PULL_DOWN:
                                    mGoodAdapter.addNewGood(been);
                                    mSwipeRefreshLayout.setRefreshing(false);
                                    tvRefreshing.setVisibility(View.GONE);
                                    mGoodAdapter.setFooterText("加载更多");
                                    break;
                                case  ACTION_PULL_UP:
                                    mGoodAdapter.addList(been);
                                    break;
                            }
                        }
                    }

                    @Override
                    public void onError(String error) {

                    }
                });
    }

    private void initView(View view) {
        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.srlNewGood);
        mSwipeRefreshLayout.setColorSchemeColors(
                R.color.google_blue,
                R.color.google_green,
                R.color.google_red,
                R.color.google_yellow

        );
        mRecyclerView = (RecyclerView) view.findViewById(R.id.rvNewGood);
        mGoodAdapter=new GoodAdapter(mContext,goodList);
        mRecyclerView.setAdapter(mGoodAdapter);
        gm=new GridLayoutManager(mContext,2);
        gm.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(gm);
        tvRefreshing= (TextView) view.findViewById(R.id.tvRefreshHint);

    }

}
