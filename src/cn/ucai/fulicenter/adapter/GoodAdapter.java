package cn.ucai.fulicenter.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import cn.ucai.fulicenter.D;
import cn.ucai.fulicenter.I;
import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.bean.NewGoodBean;
import cn.ucai.fulicenter.utils.ImageUtils;

/**
 * Created by Administrator on 2016/8/1 0001.
 */
public class GoodAdapter extends RecyclerView.Adapter<ViewHolder> {
    Context mContext;
    List<NewGoodBean> mGoodList;
    GoodViewHoder mGoodViewHoder;
    boolean isMore;
    String footerText;
    final  static  int TYPE_ITEM=0;
    final  static  int TYPE_FOOTER=1;

    public boolean isMore() {
        return isMore;
    }

    public void setMore(boolean more) {
        isMore = more;
    }

    public String getFooterText() {
        return footerText;
    }

    public void setFooterText(String footerText) {
        this.footerText = footerText;
        notifyDataSetChanged();
    }

    public GoodAdapter(Context mContext, List<NewGoodBean> list) {
        this.mContext = mContext;
        mGoodList=new ArrayList<NewGoodBean>();
        mGoodList.addAll(list);
        soryByAddTime();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View layout;
        RecyclerView.ViewHolder holder=null;

        switch (getItemViewType(viewType)){
            case TYPE_FOOTER:
             layout=LayoutInflater.from(mContext).inflate(R.layout.item_footer,null);
                holder=new FooterViewHolder(layout);
                break;
            case  TYPE_ITEM:
                layout=LayoutInflater.from(mContext).inflate(R.layout.item_good,null);
                holder=new GoodViewHoder(layout);
        }
        return  holder;
    }
    class  FooterViewHolder extends RecyclerView.ViewHolder {
        TextView tvFooter;
        public FooterViewHolder(View itemView) {
            super(itemView);
            tvFooter = (TextView) itemView.findViewById(R.id.tvFooter);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof  GoodViewHoder){
            mGoodViewHoder= (GoodViewHoder) holder;
            NewGoodBean good = mGoodList.get(position);
        //    mGoodViewHoder.ivGoodThumb.setImageURI(good.getGoodsThumb());
            ImageUtils.setGoodThumb(mContext,mGoodViewHoder.ivGoodThumb,good.getGoodsThumb());
            mGoodViewHoder.tvGoodName.setText(good.getGoodsName());
            mGoodViewHoder.tvGoodPrice.setText(good.getCurrencyPrice());
        }        if (holder instanceof  FooterViewHolder){
            ((FooterViewHolder)holder).tvFooter.setText(footerText);
            return;
        }


    }

    @Override
    public int getItemViewType(int position) {
        if (position==getItemCount()-1){
            return  TYPE_FOOTER;
        }else {
            return  TYPE_ITEM;
        }
    }
    public void addList(ArrayList<NewGoodBean> List) {
        mGoodList.addAll(List);
        soryByAddTime();
        notifyDataSetChanged();
    }
    @Override
    public int getItemCount() {
        return mGoodList.size();
    }
    class  GoodViewHoder extends RecyclerView.ViewHolder{
    LinearLayout layout;
        ImageView ivGoodThumb;
        TextView tvGoodName;
        TextView tvGoodPrice;

        public GoodViewHoder(View itemView) {
            super(itemView);
            layout = (LinearLayout) itemView.findViewById(R.id.layout_good);
            ivGoodThumb = (ImageView) itemView.findViewById(R.id.niv_good_thumb);
            tvGoodPrice = (TextView) itemView.findViewById(R.id.tv_good_price);
            tvGoodName= (TextView) itemView.findViewById(R.id.tv_good_name);

        }
    }
    public void addNewGood(ArrayList<NewGoodBean>list){
        mGoodList.clear();
        mGoodList.addAll(list);
        soryByAddTime();
        notifyDataSetChanged();
    }
    private  void  soryByAddTime(){
        Collections.sort(mGoodList, new Comparator<NewGoodBean>() {
            @Override
            public int compare(NewGoodBean goodLeft, NewGoodBean goodRight) {
                return (int)(Long.valueOf(goodRight.getAddTime())-Long.valueOf(goodLeft.getAddTime()));
            }
        });
    }

}
