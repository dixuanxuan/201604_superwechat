package cn.ucai.fulicenter.task;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import cn.ucai.fulicenter.D;
import cn.ucai.fulicenter.FuLiCenterApplication;
import cn.ucai.fulicenter.I;
import cn.ucai.fulicenter.bean.CartBean;
import cn.ucai.fulicenter.bean.GoodDetailsBean;
import cn.ucai.fulicenter.bean.MessageBean;
import cn.ucai.fulicenter.data.OkHttpUtils2;
import cn.ucai.fulicenter.utils.Utils;

/**
 * Created by sks on 2016/7/21.
 */
public class UpdateCartListTask {
    private static final String TAG = UpdateCartListTask.class.getSimpleName();
    CartBean mCart;
    Context mContext;
    public UpdateCartListTask(Context context, CartBean cart) {
        mContext=context;
        this.mCart = cart;
    }
    public void execute() {
        final List<CartBean> cartList = FuLiCenterApplication.getInstance().getCartList();
        if (cartList.contains(mCart)){
            if (mCart.getCount()>0){
            //更新
                Log.e(TAG,"mCart.getCount="+mCart.getCount());
                updateCart(new OkHttpUtils2.OnCompleteListener<MessageBean>() {
                    @Override
                    public void onSuccess(MessageBean result) {
                        if (result!=null&&result.isSuccess()){
                            cartList.set(cartList.indexOf(mCart),mCart);
                            mContext.sendStickyBroadcast(new Intent("update_cart_list"));
                        }
                    }

                    @Override
                    public void onError(String error) {

                    }
                });
            }else {
                //删除

            }
        }else {
            //添加
        }

    }
    private void updateCart(OkHttpUtils2.OnCompleteListener<MessageBean> listener){
        OkHttpUtils2<MessageBean> utils2=new OkHttpUtils2<>();
        utils2.setRequestUrl(I.REQUEST_UPDATE_CART)
                .addParam(I.Cart.ID,String.valueOf(mCart.getId()))
                .addParam(I.Cart.COUNT,mCart.getCount()+"")
                .addParam(I.Cart.IS_CHECKED,mCart.isChecked()+"")
                .targetClass(MessageBean.class).
                execute(listener);

    }
}