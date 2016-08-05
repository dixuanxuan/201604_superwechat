package cn.ucai.fulicenter.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import cn.ucai.fulicenter.D;
import cn.ucai.fulicenter.I;
import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.activity.CategoryChildActivity;
import cn.ucai.fulicenter.bean.CategoryChildBean;
import cn.ucai.fulicenter.bean.CategoryGroupBean;
import cn.ucai.fulicenter.utils.ImageUtils;

/**
 * Created by Administrator on 2016/8/4 0004.
 */
public class CategoryAdapter extends BaseExpandableListAdapter {
    Context mContext;
    List<CategoryGroupBean> mGroupLst;
    List<ArrayList<CategoryChildBean>> mCategoryChildList;

    public CategoryAdapter(Context mContext, List<CategoryGroupBean> mGroupLst, List<ArrayList<CategoryChildBean>> mCategoryChildList) {
        this.mContext = mContext;
        this.mGroupLst =new ArrayList<CategoryGroupBean>();
        mGroupLst.addAll(mGroupLst);
        this.mCategoryChildList = new ArrayList<ArrayList<CategoryChildBean>>();
        mCategoryChildList.addAll(mCategoryChildList);
    }

    @Override
    public int getGroupCount() {
        return  mGroupLst!=null?mGroupLst.size():0;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return mCategoryChildList.get(groupPosition).size();
    }

    @Override
    public CategoryGroupBean getGroup(int groupPosition) {
        if (mGroupLst!=null)return  mGroupLst.get(groupPosition);
        return null;
    }

    @Override
    public CategoryChildBean getChild(int groupPosition, int childPosition) {
        if (mCategoryChildList.get(groupPosition)!=null
                &&mCategoryChildList.get(groupPosition).get(childPosition)!=null)
            return mCategoryChildList.get(groupPosition).get(childPosition);
        return null;
    }

    @Override
    public long getGroupId(int groupPosition) {
        return 0;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return 0;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        GroupViewHolder holder=null;
        if (convertView==null){
            convertView=View.inflate(mContext,R.layout.item_category_group,null);
            holder=new GroupViewHolder();
            holder.ivGroupThumb = (ImageView) convertView.findViewById(R.id.ivCateGroupThumb);
            holder.tvGroupName = (TextView) convertView.findViewById(R.id.tvGroupName);
            holder.ivIndicator = (ImageView) convertView.findViewById(R.id.ivIndicator);
            convertView.setTag(holder);
        }else {
            holder= (GroupViewHolder) convertView.getTag();
        }
        CategoryGroupBean group = getGroup(groupPosition);
        ImageUtils.setGroupCategoryImage(mContext,holder.ivGroupThumb,group.getImageUrl());
        holder.tvGroupName.setText(group.getName());
        if (isExpanded){
            holder.ivIndicator.setImageResource(R.drawable.expand_off);
        }else {
            holder.ivIndicator.setImageResource(R.drawable.expand_on);
        }
        return convertView;
    }
    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        ChildViewHolder holder=null;
        if (convertView==null){

            convertView=View.inflate(mContext,R.layout.item_catogry_child,null);
            holder=new ChildViewHolder();

            holder.layoutCategoryChild = (RelativeLayout) convertView.findViewById(R.id.layout_catogry_child);
            holder.tvCategoruChildName = (TextView) convertView.findViewById(R.id.tvCatogryChildName);
            holder.ivCateChildThumb = (ImageView) convertView.findViewById(R.id.ivCategoryChildThumb);
            convertView.setTag(holder);
        }else {
            holder= (ChildViewHolder) convertView.getTag();
        }
        final CategoryChildBean child = getChild(groupPosition, childPosition);
        if (child!=null){
            ImageUtils.setGroupCategoryImage(mContext,holder.ivCateChildThumb,child.getImageUrl());
            holder.tvCategoruChildName.setText(child.getName());
            holder.layoutCategoryChild.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mContext.startActivity(new Intent(mContext,CategoryChildActivity.class)
                    .putExtra(I.NewAndBoutiqueGood.CAT_ID,child.getId()));
                }
            });
        }
        return convertView;
    }
    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }

    public void addAll(List<CategoryGroupBean> mGroupList, List<ArrayList<CategoryChildBean>> mChildLsit) {
        this.mGroupLst.addAll(mGroupList);
        this.mCategoryChildList.addAll(mChildLsit);
        notifyDataSetChanged();
    }

    class  GroupViewHolder {
        ImageView ivGroupThumb;
        TextView tvGroupName;
        ImageView ivIndicator;
    }
    class  ChildViewHolder  {
        RelativeLayout layoutCategoryChild;
        ImageView ivCateChildThumb;
        TextView  tvCategoruChildName;
    }
}
