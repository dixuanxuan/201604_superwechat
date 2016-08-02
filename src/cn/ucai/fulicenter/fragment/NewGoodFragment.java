package cn.ucai.fulicenter.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;

import java.util.ArrayList;

import cn.ucai.fulicenter.I;
import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.adapter.GoodAdapter;
import cn.ucai.fulicenter.bean.NewGoodBean;
import cn.ucai.fulicenter.data.OkHttpUtils2;

/**
 * A simple {@link Fragment} subclass.
 */
public class NewGoodFragment extends Fragment {
    RecyclerView mRecyclerView;
    ArrayList<NewGoodBean>goodList;
    GoodAdapter mGoodAdapter;
    GridLayoutManager gm;
    public NewGoodFragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_new_good, container, false);
        initView(view);
        initData();
        return  view;
    }

    private void initData() {
        OkHttpUtils2<String>utils=new OkHttpUtils2<>();
            utils.url(I.SERVER_ROOT)
                    .addParam(I.KEY_REQUEST,I.REQUEST_DOWNLOAD_NEW_GOOD)
                    .addParam(I.CategoryGood.CAT_ID,I.CAT_ID+"")
                    .addParam(I.PAGE_ID,0+"")
                    .addParam(I.PAGE_SIZE,4+"")
                    .targetClass(String.class)
                    .execute(new OkHttpUtils2.OnCompleteListener<String>() {
                        @Override
                        public void onSuccess(String result) {
                            if(result!=null&&result.length()!=0){
                                Gson gson=new Gson();
                                NewGoodBean[] goodBeen = gson.fromJson(result, NewGoodBean[].class);
                                ArrayList<NewGoodBean> been = OkHttpUtils2.array2List(goodBeen);
                                mGoodAdapter=new GoodAdapter(getActivity(),been);
                            }
                        }

                        @Override
                        public void onError(String error) {

                        }
                    });
    }

    private void initView(View view) {
        goodList=new ArrayList<>();
        mRecyclerView = (RecyclerView) view.findViewById(R.id.rvView);
        mGoodAdapter=new GoodAdapter(getActivity(),goodList);
        mRecyclerView.setAdapter(mGoodAdapter);

        gm=new GridLayoutManager(getActivity(),2);
        mRecyclerView.setLayoutManager(gm);
    }

}
