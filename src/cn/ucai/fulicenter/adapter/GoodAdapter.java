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
import java.util.List;
import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.bean.NewGoodBean;

/**
 * Created by Administrator on 2016/8/1 0001.
 */
public class GoodAdapter extends RecyclerView.Adapter<ViewHolder> {
    Context mContext;
    List<NewGoodBean> mGoodList;
    GoodViewHoder mGoodViewHoder;

    public GoodAdapter(Context mContext, List<NewGoodBean> list) {
        this.mContext = mContext;
        mGoodList=new ArrayList<NewGoodBean>();
        mGoodList.addAll(list);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        ViewHolder holder=null;
        holder=new GoodViewHoder( inflater.inflate(R.layout.item_good, parent, false));
        return  holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof  GoodViewHoder){
            mGoodViewHoder= (GoodViewHoder) holder;
            NewGoodBean good = mGoodList.get(position);
        //    mGoodViewHoder.ivGoodThumb.setImageURI(good.getGoodsThumb());
            mGoodViewHoder.tvGoodName.setText(good.getGoodsName());
            mGoodViewHoder.tvGoodPrice.setText(good.getCurrencyPrice());
        }

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
            tvGoodPrice = (TextView) itemView.findViewById(R.id.tv_good_price);
        }
    }
}
