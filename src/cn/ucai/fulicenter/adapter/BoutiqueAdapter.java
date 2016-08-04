package cn.ucai.fulicenter.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import cn.ucai.fulicenter.D;
import cn.ucai.fulicenter.I;
import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.activity.BoutiqueDesc_Activity;
import cn.ucai.fulicenter.activity.GoodDetailsActivity;
import cn.ucai.fulicenter.bean.BoutiqueBean;
import cn.ucai.fulicenter.utils.ImageUtils;
import cn.ucai.fulicenter.view.FooterViewHolder;

/**
 * Created by Administrator on 2016/8/1 0001.
 */
public class BoutiqueAdapter extends RecyclerView.Adapter<ViewHolder> {
    Context mContext;
    List<BoutiqueBean> mBoutiqueList;
    BoutiqueViewHolder mBoutiqueViewHolder;
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

    public BoutiqueAdapter(Context mContext, List<BoutiqueBean> list) {
        this.mContext = mContext;
        mBoutiqueList =new ArrayList<BoutiqueBean>();
        mBoutiqueList.addAll(list);
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
                layout=LayoutInflater.from(mContext).inflate(R.layout.item_boutique,parent,false);
                holder=new BoutiqueViewHolder(layout);
                break;
        }
        return  holder;
    }

    public void initItem(ArrayList<BoutiqueBean> list) {
        if (mBoutiqueList !=null){
            mBoutiqueList.clear();
        }
        mBoutiqueList.addAll(list);
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if (holder instanceof FooterViewHolder){
            mFooterViewHolder= (FooterViewHolder) holder;
            mFooterViewHolder.tvFooter.setText(footerText);

        }
        if (holder instanceof BoutiqueViewHolder){
            mBoutiqueViewHolder = (BoutiqueViewHolder) holder;
            final BoutiqueBean boutique = mBoutiqueList.get(position);
        //    mBoutiqueViewHolder.ivBoutiqueThumb.setImageURI(good.getGoodsThumb());
             ImageUtils.setGoodThumb(mContext,mBoutiqueViewHolder.ivBoutiqueThumb,boutique.getImageurl());
             mBoutiqueViewHolder.tvBoutiqueTitle.setText(boutique.getTitle());
             mBoutiqueViewHolder.tvBoutiqueName.setText(boutique.getName());
             mBoutiqueViewHolder.tvBoutiqueDesceip.setText(boutique.getDescription());
                mBoutiqueViewHolder.layout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mContext.startActivity(new Intent(mContext,BoutiqueDesc_Activity.class)
                                .putExtra(D.Category.KEY_CAT_ID,boutique.getId()
                                ).putExtra(D.Category.KEY_NAME,boutique.getName()));
                    }
                });

        //    mBoutiqueViewHolder.ivBoutiqueThumb.setImageURI(good.getGoodsThumb());
//            ImageUtils.setGoodThumb(mContext,mBoutiqueViewHolder.ivBoutiqueThumb,good.getGoodsThumb());
//            mBoutiqueViewHolder.tvBoutiquetitle.setText(good.getGoodsName());
//            mBoutiqueViewHolder.tvBoutiqueName.setText(good.getCurrencyPrice());
//            mBoutiqueViewHolder.layout.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    mContext.startActivity(new Intent(mContext,GoodDetailsActivity.class)
//                    .putExtra(D.GoodDetails.KEY_GOODS,good.getGoodsId()));
//                }
//            });
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

        return mBoutiqueList !=null? mBoutiqueList.size()+1:1;
       //return  mBoutiqueList.size();
    }

    public void addItem(ArrayList<BoutiqueBean> list) {
        if (mBoutiqueList !=null){
            mBoutiqueList.addAll(list);
        }
        notifyDataSetChanged();
    }

    class BoutiqueViewHolder extends ViewHolder{
        RelativeLayout layout;
        ImageView ivBoutiqueThumb;
        TextView tvBoutiqueTitle;
        TextView tvBoutiqueName;
        TextView tvBoutiqueDesceip;

        public BoutiqueViewHolder(View itemView) {
            super(itemView);
            layout = (RelativeLayout) itemView.findViewById(R.id.layoutboutiqueitem);
            ivBoutiqueThumb = (ImageView) itemView.findViewById(R.id.ivBoutiqueImg);
            tvBoutiqueName = (TextView) itemView.findViewById(R.id.tvBoutiqueName);
            tvBoutiqueTitle = (TextView) itemView.findViewById(R.id.tvBoutiqueTitle);
            tvBoutiqueDesceip = (TextView) itemView.findViewById(R.id.tvBoutiqueDescripe);
        }
    }



}
