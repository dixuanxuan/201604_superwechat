package cn.ucai.fulicenter.adapter;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import cn.ucai.fulicenter.D;
import cn.ucai.fulicenter.I;
import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.activity.BoutiqueDesc_Activity;
import cn.ucai.fulicenter.bean.CartBean;
import cn.ucai.fulicenter.bean.GoodDetailsBean;
import cn.ucai.fulicenter.task.UpdateCartListTask;
import cn.ucai.fulicenter.utils.ImageUtils;
import cn.ucai.fulicenter.view.FooterViewHolder;

/**
 * Created by Administrator on 2016/8/1 0001.
 */
public class CartAdapter extends RecyclerView.Adapter<ViewHolder> {
    Context mContext;
    List<CartBean> mCartList;
    CartViewHolder mCartViewHolder;
    boolean isMore;


    public boolean isMore() {
        return isMore;
    }

    public void setMore(boolean more) {
        isMore = more;
    }


    public CartAdapter(Context mContext, List<CartBean> list) {
        this.mContext = mContext;
        mCartList =new ArrayList<CartBean>();
        mCartList.addAll(list);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View layout;
        ViewHolder holder=null;
        layout=LayoutInflater.from(mContext).inflate(R.layout.item_cart,parent,false);
        holder=new CartViewHolder(layout);
        return  holder;
    }

    public void initItem(List<CartBean> list) {
        if (mCartList !=null){
            mCartList.clear();
        }
        mCartList.addAll(list);
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        mCartViewHolder = (CartViewHolder) holder;
        final CartBean cart = mCartList.get(position);
        GoodDetailsBean goods=cart.getGoods();
        if (goods==null){
            return;
        }

        mCartViewHolder.tvGoodName.setText(goods.getGoodsName());
        mCartViewHolder.tvCartCount.setText(""+cart.getCount());
        mCartViewHolder.mCheckBox.setChecked(cart.isChecked());
        mCartViewHolder.tvGoodPrice.setText(goods.getCurrencyPrice());
        ImageUtils.setGoodThumb(mContext,mCartViewHolder.ivGoodThumb,goods.getGoodsThumb());
        mCartViewHolder.mCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                cart.setChecked(isChecked);
                new UpdateCartListTask(mContext,cart).execute();
            }
        });

        }






    @Override
    public int getItemCount() {

      //  return mCartList !=null? mCartList.size()+1:1;
        return mCartList !=null? mCartList.size():0;
    }

    public void addItem(List<CartBean> list) {
        if (mCartList !=null){
            mCartList.addAll(list);
        }
        notifyDataSetChanged();
    }

    class CartViewHolder extends ViewHolder{
        CheckBox mCheckBox;
        ImageView ivGoodThumb;
        TextView tvGoodName;
        ImageView ivAddCart;
        TextView tvCartCount;
        ImageView ivReduceCart;
        TextView tvGoodPrice;


        public CartViewHolder(View itemView) {
            super(itemView);
            mCheckBox = (CheckBox) itemView.findViewById(R.id.cbSelect);
            ivGoodThumb = (ImageView) itemView.findViewById(R.id.ivGoodThumb);
            tvGoodName = (TextView) itemView.findViewById(R.id.tvGoodName);
            ivAddCart = (ImageView) itemView.findViewById(R.id.ivAddCart);
            tvCartCount = (TextView) itemView.findViewById(R.id.tvCartCount);
            ivReduceCart = (ImageView) itemView.findViewById(R.id.ivReduceCart);
            tvGoodPrice = (TextView) itemView.findViewById(R.id.tvGoodPrice);
        }
    }



}
