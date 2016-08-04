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
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.TextView;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import cn.ucai.fulicenter.I;
import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.activity.FuliCenterMainActivity;
import cn.ucai.fulicenter.adapter.BoutiqueAdapter;
import cn.ucai.fulicenter.adapter.CategoryAdapter;
import cn.ucai.fulicenter.bean.BoutiqueBean;
import cn.ucai.fulicenter.bean.CategoryChildBean;
import cn.ucai.fulicenter.bean.CategoryGroupBean;
import cn.ucai.fulicenter.data.OkHttpUtils2;
import cn.ucai.fulicenter.utils.Utils;

/**
 * A simple {@link Fragment} subclass.
 */
public class CategoryFragment extends Fragment {
    private  static  final  String TAG=CategoryFragment.class.getSimpleName();
    FuliCenterMainActivity mContext;
    ExpandableListView mExpandableListView;
    List<CategoryGroupBean> mGroupList;
    List<ArrayList<CategoryChildBean>> mChildLsit;
    CategoryAdapter mAdapter;
    int groupCount;
    public CategoryFragment() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mContext= (FuliCenterMainActivity) getContext();
        View view = View.inflate(mContext,R.layout.fragment_category, null);
        mGroupList=new ArrayList<CategoryGroupBean>();
        mChildLsit=new ArrayList<ArrayList<CategoryChildBean>>();
        mAdapter=new CategoryAdapter(mContext,mGroupList,mChildLsit);
        initView(view);
        initData();
        return  view;
    }

    private void initData() {
        findCateGroupList(new OkHttpUtils2.OnCompleteListener<String>() {
            @Override
            public void onSuccess(String result) {
                Log.e(TAG,"result01="+result);
                if (result != null) {
                    Gson gson = new Gson();
                    CategoryGroupBean[] categoryGroupBeen = gson.fromJson(result, CategoryGroupBean[].class);
                    ArrayList<CategoryGroupBean> groupList = Utils.array2List(categoryGroupBeen);
                    if (groupList!=null){
                        Log.e(TAG,"groupList="+groupList.size());
                           int i=0;
                        for (CategoryGroupBean g: groupList){
                            mChildLsit.add(new ArrayList<CategoryChildBean>());
                            findCateChildList(groupList,i);
                            i++;
                        }
                    }
                }
            }
            @Override
            public void onError(String error) {
                Log.e(TAG,"child.error="+error);
            }
        });
    }
    private  void findCateChildList(final ArrayList<CategoryGroupBean> g, final int  index){

        final  OkHttpUtils2<String> utils=new OkHttpUtils2<>();
        utils.setRequestUrl(I.REQUEST_FIND_CATEGORY_CHILDREN)
                .addParam(I.CategoryChild.PARENT_ID,String.valueOf(g.get(index).getId()))
                .addParam(I.PAGE_ID,String.valueOf("0"))
                .addParam(I.PAGE_SIZE,String.valueOf("10"))
                .targetClass(String.class)
                .execute(new OkHttpUtils2.OnCompleteListener<String>() {
                    @Override
                    public void onSuccess(String s) {
                        groupCount++;
                        Log.e(TAG,"result02="+s);
                            Gson gson = new Gson();
                            CategoryChildBean[] categoryChildBeen = gson.fromJson(s, CategoryChildBean[].class);
                            ArrayList<CategoryChildBean> childList = utils.array2List(categoryChildBeen);
                                mChildLsit.set(index,childList);
                        if (groupCount==g.size()){
                            mAdapter.addAll(g,mChildLsit);
                        }

                    }

                    @Override
                    public void onError(String error) {

                    }
                });
    }
    private  void findCateGroupList(OkHttpUtils2.OnCompleteListener<String> listener){
        final  OkHttpUtils2<String> utils=new OkHttpUtils2<>();
        utils.setRequestUrl(I.REQUEST_FIND_CATEGORY_GROUP)
                .targetClass(String.class)
                .execute(listener);
    }
    private void initView(View view) {
        mExpandableListView = (ExpandableListView) view.findViewById(R.id.elvCategory);
        mExpandableListView.setGroupIndicator(null);
        mExpandableListView.setAdapter(mAdapter);

    }
}
