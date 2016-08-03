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
//        setListener();
        return  view;
    }

    private void initData() {
        findViewGoodList(new OkHttpUtils2.OnCompleteListener<String>() {
            @Override
            public void onSuccess(String result) {
                Log.e(TAG,"result="+result);
                if (result!=null){
                    Gson gson=new Gson();
                    BoutiqueBean[] boutiqueBeen=gson.fromJson(result,BoutiqueBean[].class);
                    ArrayList<BoutiqueBean> boutiqueBeanArrayList = Utils.array2List(boutiqueBeen);
                    mBoutiqueAdapter.initItem(boutiqueBeanArrayList);
                }
            }

            @Override
            public void onError(String error) {
            }
        });


    }
    private  void findViewGoodList(OkHttpUtils2.OnCompleteListener<String> listener){
        final  OkHttpUtils2<String> utils=new OkHttpUtils2<>();
        utils.setRequestUrl(I.REQUEST_FIND_BOUTIQUES)
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
       // tvRefreshing= (TextView) view.findViewById(R.id.tvRefreshHint);

    }


}
