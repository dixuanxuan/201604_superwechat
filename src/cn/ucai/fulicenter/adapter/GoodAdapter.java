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
import cn.ucai.fulicenter.activity.view.FooterViewHolder;
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
    FooterViewHolder mFooterViewHolder;

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
        ViewHolder holder=null;

        switch (viewType){
            case I.TYPE_FOOTER:
             layout=LayoutInflater.from(mContext).inflate(R.layout.item_footer,parent,false);
                holder=new FooterViewHolder(layout);
                break;
            case  I.TYPE_ITEM:
                layout=LayoutInflater.from(mContext).inflate(R.layout.item_good,parent,false);
                holder=new GoodViewHoder(layout);
                break;
        }
        return  holder;
    }

    public void initItem(ArrayList<NewGoodBean> list) {
        if (mGoodList!=null){
            mGoodList.clear();
        }
        mGoodList.addAll(list);
        soryByAddTime();
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if (holder instanceof FooterViewHolder){
            mFooterViewHolder= (FooterViewHolder) holder;
            mFooterViewHolder.tvFooter.setText(footerText);
        }
        if (holder instanceof  GoodViewHoder){
            mGoodViewHoder= (GoodViewHoder) holder;
            NewGoodBean good = mGoodList.get(position);
        //    mGoodViewHoder.ivGoodThumb.setImageURI(good.getGoodsThumb());
            ImageUtils.setGoodThumb(mContext,mGoodViewHoder.ivGoodThumb,good.getGoodsThumb());
            mGoodViewHoder.tvGoodName.setText(good.getGoodsName());
            mGoodViewHoder.tvGoodPrice.setText(good.getCurrencyPrice());
        }

    }

    @Override
    public int getItemViewType(int position) {
        if (position==getItemCount()-1){
            return  I.TYPE_FOOTER;
        }else {
            return  I.TYPE_ITEM;
        }
    }

    @Override
    public int getItemCount() {

        return mGoodList!=null?mGoodList.size()+1:1;
       //return  mGoodList.size();
    }

    public void addItem(ArrayList<NewGoodBean> list) {
        mGoodList.addAll(list);
        soryByAddTime();
        notifyDataSetChanged();
    }

    class  GoodViewHoder extends ViewHolder{
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

    private  void  soryByAddTime(){
        Collections.sort(mGoodList, new Comparator<NewGoodBean>() {
            @Override
            public int compare(NewGoodBean goodLeft, NewGoodBean goodRight) {
                return (int)(Long.valueOf(goodRight.getAddTime())-Long.valueOf(goodLeft.getAddTime()));
            }
        });
    }

}
