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
import java.util.List;

import cn.ucai.fulicenter.I;
import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.activity.FuliCenterMainActivity;
import cn.ucai.fulicenter.adapter.BoutiqueAdapter;
import cn.ucai.fulicenter.adapter.GoodAdapter;
import cn.ucai.fulicenter.bean.BoutiqueBean;
import cn.ucai.fulicenter.bean.NewGoodBean;
import cn.ucai.fulicenter.data.OkHttpUtils2;
import cn.ucai.fulicenter.utils.Utils;

/**
 * A simple {@link Fragment} subclass.
 */
public class BoutiqueFragment extends Fragment {
    private  static  final  String TAG=BoutiqueFragment.class.getSimpleName();
    FuliCenterMainActivity mContext;
    List<BoutiqueBean> mBoutiqueList;
    SwipeRefreshLayout mSwipeRefreshLayout;
    RecyclerView mRecyclerView;
    GridLayoutManager gm;
    BoutiqueAdapter  mBoutiqueAdapter;
    int pageId=1;
    TextView tvRefreshing;
    int action=I.ACTION_DOWNLOAD;



    public BoutiqueFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mContext= (FuliCenterMainActivity) getContext();
        View view = View.inflate(mContext,R.layout.fragment_new_good, null);
        mBoutiqueList=new ArrayList<BoutiqueBean>();
        mBoutiqueList=new ArrayList<>();
        initView(view);
        initData();
        setListener();
        return  view;
    }

    private void setListener() {
        setPullDownRefreshListener();
        setPullUpRefreshListener();

    }

    private void setPullUpRefreshListener() {
        mRecyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            int lastItemPositon;
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {

                super.onScrollStateChanged(recyclerView, newState);
                int a=RecyclerView.SCROLL_STATE_DRAGGING;
                int b=RecyclerView.SCROLL_STATE_IDLE;
                int c=RecyclerView.SCROLL_STATE_SETTLING;
                Log.e(TAG,"newState="+newState);
                if (newState==RecyclerView.SCROLL_STATE_IDLE
                        &&lastItemPositon==mBoutiqueAdapter.getItemCount()-1){
                    if (mBoutiqueAdapter.isMore()){
                        action=I.ACTION_PULL_UP;
                        pageId+=I.PAGE_ID_DEFAULT;
                        initData();
                    }
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int f=gm.findFirstVisibleItemPosition();
                int l=gm.findLastVisibleItemPosition();
                Log.e(TAG,"f="+f+",l="+l);
                lastItemPositon=gm.findLastVisibleItemPosition();
            }
        });
    }

    private void setPullDownRefreshListener() {
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                action=I.ACTION_PULL_DOWN;
                tvRefreshing.setVisibility(View.VISIBLE);
                pageId=1;
                initData();
            }
        });

    }

    private void initData() {
        findViewGoodList(new OkHttpUtils2.OnCompleteListener<String>() {
            @Override
            public void onSuccess(String result) {
                Log.e(TAG,"result="+result);
                tvRefreshing.setVisibility(View.GONE);
                mSwipeRefreshLayout.setRefreshing(false);
                mBoutiqueAdapter.setMore(true);
                if (result!=null){
                    Gson gson=new Gson();
                    BoutiqueBean[] boutiqueBeen=gson.fromJson(result,BoutiqueBean[].class);
                    ArrayList<BoutiqueBean> boutiqueBeanArrayList = Utils.array2List(boutiqueBeen);
                    if (action==I.ACTION_DOWNLOAD||action==I.ACTION_PULL_DOWN){
                        mBoutiqueAdapter.initItem(boutiqueBeanArrayList);
                    }else {
                        mBoutiqueAdapter.initItem(boutiqueBeanArrayList);
                    }
                    if (boutiqueBeanArrayList.size()<I.PAGE_SIZE_DEFAULT){
                        mBoutiqueAdapter.setMore(false);
                        mBoutiqueAdapter.setFooterText("没有更多");
                    }
                }else {
                    mBoutiqueAdapter.setMore(false);
                    mBoutiqueAdapter.setFooterText("没有更多");
                }
            }
            @Override
            public void onError(String error) {
                Log.e(TAG,"error="+error);
                tvRefreshing.setVisibility(View.GONE);
                mSwipeRefreshLayout.setRefreshing(false);
            }
        });


    }
    private  void findViewGoodList(OkHttpUtils2.OnCompleteListener<String> listener){
        final  OkHttpUtils2<String> utils=new OkHttpUtils2<>();
        utils.setRequestUrl(I.REQUEST_FIND_BOUTIQUES)
                .addParam(I.NewAndBoutiqueGood.CAT_ID,String.valueOf(I.CAT_ID))
                .addParam(I.PAGE_ID,String.valueOf(pageId))
                .addParam(I.PAGE_SIZE,String.valueOf(I.PAGE_SIZE_DEFAULT))
                .targetClass(String.class)
                .execute(listener);
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
        gm=new GridLayoutManager(mContext,1);
        gm.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(gm);
        mBoutiqueAdapter=new BoutiqueAdapter(mContext,mBoutiqueList);
        mRecyclerView.setAdapter(mBoutiqueAdapter);
        tvRefreshing= (TextView) view.findViewById(R.id.tvRefreshHint);

    }


}
