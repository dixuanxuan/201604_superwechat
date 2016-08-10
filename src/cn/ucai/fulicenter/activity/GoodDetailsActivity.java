package cn.ucai.fulicenter.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v4.app.NavUtils;
import android.util.Log;
import android.view.View;
import android.view.animation.LayoutAnimationController;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;
import cn.ucai.fulicenter.D;
import cn.ucai.fulicenter.DemoHXSDKHelper;
import cn.ucai.fulicenter.FuLiCenterApplication;
import cn.ucai.fulicenter.I;
import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.bean.AlbumBean;
import cn.ucai.fulicenter.bean.CartBean;
import cn.ucai.fulicenter.bean.CollectBean;
import cn.ucai.fulicenter.bean.GoodDetailsBean;
import cn.ucai.fulicenter.bean.MessageBean;
import cn.ucai.fulicenter.bean.NewGoodBean;
import cn.ucai.fulicenter.bean.PropertyBean;
import cn.ucai.fulicenter.bean.UserAvatar;
import cn.ucai.fulicenter.data.OkHttpUtils2;
import cn.ucai.fulicenter.task.DownloadCollectCountTask;
import cn.ucai.fulicenter.task.UpdateCartListTask;
import cn.ucai.fulicenter.utils.Utils;
import cn.ucai.fulicenter.view.DisplayUtils;
import cn.ucai.fulicenter.view.FlowIndicator;
import cn.ucai.fulicenter.view.SlideAutoLoopView;


/**
 * Created by Administrator on 2016/8/3 0003.
 */
public class GoodDetailsActivity extends  BaseActivity {
    private  static  final  String TAG=GoodDetailsActivity.class.getSimpleName();
    ImageView ivShare;
    ImageView  ivCollect;
    ImageView  ivCart;
    TextView    tvCartCount;
//    int i=0;
    TextView tvGoodEnglishName;
    TextView tvGoodName;
    TextView tvGoodPriceCurrent;
    TextView tvGoodPriceShop;


    SlideAutoLoopView mSlideAutoLoopView;
    FlowIndicator mFlowIndicator;
    WebView mWebView;
    int goodId;
    GoodDetailsActivity mContext;
    GoodDetailsBean mGoodDetail;
    boolean isCollect;

    @Override
    public void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.activity_good_details);
        mContext = this;
        initView();
        initData();
        setListener();
    }

    private void setListener() {
        MyListener listener=new MyListener();
        ivCollect.setOnClickListener(listener);
        ivShare.setOnClickListener(listener);
        tvCartCount.setOnClickListener(listener);
        setUpdateCartCountListener();


    }


    private void initData() {
//        i=Utils.sumCartCount();
//        tvCartCount.setText(i+"");
        goodId=getIntent().getIntExtra(D.GoodDetails.KEY_GOODS, 0);
        Log.e(TAG,"goodId="+goodId);
        if (goodId>0){
            getGoodDetailsByGoodId(new OkHttpUtils2.OnCompleteListener<String>() {
                @Override
                public void onSuccess(String result) {
                    if (result!=null){

                        Log.e(TAG,"result="+result);
                        Gson gson=new Gson();
                        mGoodDetail=gson.fromJson(result,GoodDetailsBean.class);
                        //          mGoodDetail=newGoodBeen;
                        showGoodDetails();
                    }
                }
                @Override
                public void onError(String error) {
                    Log.e(TAG,"error="+error);
                    Toast.makeText(mContext,"获取商品详情失败",Toast.LENGTH_LONG).show();
                }
            });
        }else {
            finish();
            Toast.makeText(mContext,"获取商品详情失败",Toast.LENGTH_LONG).show();
        }
    }
    private void showGoodDetails() {

        tvGoodEnglishName.setText(mGoodDetail.getGoodsEnglishName());
        tvGoodName.setText(mGoodDetail.getGoodsName());
        tvGoodPriceCurrent.setText(mGoodDetail.getCurrencyPrice());
        tvGoodPriceShop.setText(mGoodDetail.getShopPrice());
        //mWebView.loadUrl(mGoodDetail.getShareUrl());
        mSlideAutoLoopView.startPlayLoop(mFlowIndicator,
                getAlbumImageUrl(),getAlbumImageSize());
        mWebView.loadDataWithBaseURL(null,mGoodDetail.getGoodsBrief(),D.TEXT_HTML,D.UTF_8,null);


    }

    private String[] getAlbumImageUrl() {
        String [] albumImageUrl=new String[]{};
        if (mGoodDetail.getProperties()!=null&&mGoodDetail.getProperties().length>0){
            AlbumBean[] albums=mGoodDetail.getProperties()[0].getAlbums();
            albumImageUrl=new   String[albums.length];
            for (int i=0;i<albumImageUrl.length;i++){
                albumImageUrl[i]=albums[i].getImgUrl();
            }
        }
        return albumImageUrl;
    }

    private int getAlbumImageSize() {
        if (mGoodDetail.getProperties()!=null&&mGoodDetail.getProperties().length>0){
            return  mGoodDetail.getProperties()[0].getAlbums().length;

        }return 0;


    }

    private  void  getGoodDetailsByGoodId(OkHttpUtils2.OnCompleteListener<String> listener){
        OkHttpUtils2<String> utils=new OkHttpUtils2<String>();
        utils.setRequestUrl(I.REQUEST_FIND_GOOD_DETAILS)
                .addParam(D.GoodDetails.KEY_GOODS_ID,String.valueOf(goodId))
                .targetClass(String.class)
                .execute(listener);

    }

    private void initView() {
        DisplayUtils.iniitBack(mContext);
        ivShare = (ImageView) findViewById(R.id.ivgoodshare);
        ivCollect=(ImageView) findViewById(R.id.ivcollect);
        ivCart=(ImageView) findViewById(R.id.ivgoodcart);
        tvCartCount=(TextView) findViewById(R.id.tvcartcount);


        tvGoodEnglishName = (TextView) findViewById(R.id.tvgoodenglish);
        tvGoodName = (TextView) findViewById(R.id.tvgoodname);
        tvGoodPriceCurrent = (TextView) findViewById(R.id.tvgoodpricecurrent);
        tvGoodPriceShop = (TextView) findViewById(R.id.tvgoodpriceshop);

        mSlideAutoLoopView = (SlideAutoLoopView) findViewById(R.id.salv);
        mFlowIndicator = (FlowIndicator) findViewById(R.id.indicator);
        mWebView = (WebView) findViewById(R.id.goodbref);

        WebSettings settings = mWebView.getSettings();
        settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        settings.setBuiltInZoomControls(true);
    }

    @Override
    protected void onResume() {
        super.onResume();
        initCollectStatus();
        initCartStatus();


    }

    private void initCartStatus() {
        int count=Utils.sumCartCount();
        if (count>0){
            tvCartCount.setVisibility(View.VISIBLE);
            tvCartCount.setText(""+count);
        }else {
            tvCartCount.setVisibility(View.GONE);
            tvCartCount.setText("0");
        }

    }

    private void initCollectStatus() {

        String userName = FuLiCenterApplication.getInstance().getUserName();
        if (DemoHXSDKHelper.getInstance().isLogined()){
            OkHttpUtils2<MessageBean> utils2=new OkHttpUtils2<>();
            utils2.setRequestUrl(I.REQUEST_IS_COLLECT)
                    .addParam(I.Collect.USER_NAME,userName)
                    .addParam(I.Collect.GOODS_ID,goodId+"")
                    .targetClass(MessageBean.class)
                    .execute(new OkHttpUtils2.OnCompleteListener<MessageBean>() {
                        @Override
                        public void onSuccess(MessageBean result) {
                            Log.e(TAG,"result="+result);
                            if (result!=null&&result.isSuccess()){
                                isCollect=true;
                            }else {
                                isCollect=false;
                            }
                            updateCollectStatus();

                        }

                        @Override
                        public void onError(String error) {
                            Log.e(TAG,"error="+error);
                        }
                    });
        }

    }
    class  MyListener  implements  View.OnClickListener{

        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.ivcollect:
                    goodColect();
                    break;
                case R.id.ivgoodshare:
                    showShare();
                    break;
                case R.id.ivgoodcart:
                    addCart();
                    break;
            }

        }
    }
    private void addCart(){
        Log.e(TAG,"addCart.....");
        List<CartBean> cartList = FuLiCenterApplication.getInstance().getCartList();
        CartBean cart=new CartBean();
        boolean isExits=false;
       for (CartBean cartBean: cartList){
           if (cartBean.getGoodsId()==goodId){
               cart.setChecked(cartBean.isChecked());
               cart.setCount(cartBean.getCount()+1);
               cart.setGoods(mGoodDetail);
               cart.setUserName(cartBean.getUserName());
               isExits=true;
           }
       }
        Log.e(TAG,"addCart....isExit="+isExits);
        if (!isExits){
            cart=new CartBean();
            cart.setChecked(true);
            cart.setCount(1);
            cart.setGoods(mGoodDetail);
            cart.setUserName(FuLiCenterApplication.getInstance().getUserName());

        }
        new UpdateCartListTask(mContext,cart).execute();
    }
    private void showShare() {
        ShareSDK.initSDK(this);
        OnekeyShare oks = new OnekeyShare();
        //关闭sso授权
        oks.disableSSOWhenAuthorize();

// 分享时Notification的图标和文字  2.5.9以后的版本不调用此方法
        //oks.setNotification(R.drawable.ic_launcher, getString(R.string.app_name));
        // title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间使用
        oks.setTitle(getString(R.string.share));
        // titleUrl是标题的网络链接，仅在人人网和QQ空间使用
        oks.setTitleUrl("http://sharesdk.cn");
        // text是分享文本，所有平台都需要这个字段
        oks.setText("我是分享文本");
        // imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
        //oks.setImagePath("/sdcard/test.jpg");//确保SDcard下面存在此张图片
        // url仅在微信（包括好友和朋友圈）中使用
        oks.setUrl("http://sharesdk.cn");
        // comment是我对这条分享的评论，仅在人人网和QQ空间使用
        oks.setComment("我是测试评论文本");
        // site是分享此内容的网站名称，仅在QQ空间使用
        oks.setSite(getString(R.string.app_name));
        // siteUrl是分享此内容的网站地址，仅在QQ空间使用
        oks.setSiteUrl("http://sharesdk.cn");

// 启动分享GUI
        oks.show(this);
    }
//取消或者注册收藏
    private void goodColect() {
        if (DemoHXSDKHelper.getInstance().isLogined()){
           if (isCollect){
               //取消收藏
               OkHttpUtils2<String  > utils2=new OkHttpUtils2<String>();
               utils2.setRequestUrl(I.REQUEST_DELETE_COLLECT)
                       .addParam(I.Collect.USER_NAME, FuLiCenterApplication.getInstance().getUserName())
                       .addParam(I.Collect.GOODS_ID,goodId+"")
                       .targetClass(String.class)
                       .execute(new OkHttpUtils2.OnCompleteListener<String>() {
                           @Override
                           public void onSuccess(String result) {
                               Gson gson=new Gson();
                               MessageBean messageBean = gson.fromJson(result, MessageBean.class);
                               Log.e(TAG,"result="+result);
                               if (messageBean!=null&&messageBean.isSuccess()){
                                   isCollect=false;
                                   new DownloadCollectCountTask(mContext,FuLiCenterApplication.getInstance().getUserName()).excute();
                                   sendStickyBroadcast(new Intent("update_collec_list"));
                               }else {
                                   Log.e(TAG,"fail");
                               }
                               updateCollectStatus();
                               Toast.makeText(mContext,messageBean.getMsg(),Toast.LENGTH_SHORT).show();

                           }
                           @Override
                           public void onError(String error) {
                               Log.e(TAG,"error="+error);
                           }
                       });
           }else {

               //添加收藏
               OkHttpUtils2<String> utils=new OkHttpUtils2<>();
               utils.setRequestUrl(I.REQUEST_ADD_COLLECT)
                       .addParam(I.Collect.USER_NAME,FuLiCenterApplication.getInstance().getUserName())
                       .addParam(I.Collect.GOODS_ID,mGoodDetail.getGoodsId()+"")
                       .addParam(I.Collect.ADD_TIME,mGoodDetail.getAddTime()+"")
                       .addParam(I.Collect.GOODS_THUMB,I.Collect.GOODS_THUMB)
                       .addParam(I.Collect.USER_NAME,mGoodDetail.getGoodsName())
                       .addParam(I.Collect.GOODS_ENGLISH_NAME,mGoodDetail.getGoodsEnglishName())
                       .addParam(I.Collect.GOODS_IMG,mGoodDetail.getGoodsImg())
                       .targetClass(String.class)
                       .execute(new OkHttpUtils2.OnCompleteListener<String>() {
                           @Override
                           public void onSuccess(String result) {
                               Gson gson=new Gson();
                               MessageBean messageBean = gson.fromJson(result, MessageBean.class);
                               Log.e(TAG,"result="+result);
                               if (messageBean!=null&&messageBean.isSuccess()){
                                   isCollect=true;
                                   new DownloadCollectCountTask(mContext,FuLiCenterApplication.getInstance().getUserName()).excute();
                                   sendStickyBroadcast(new Intent("update_collec_list"));
                               }else {
                                   Log.e(TAG,"fail");
                               }
                               updateCollectStatus();
                               Toast.makeText(mContext,messageBean.getMsg(),Toast.LENGTH_SHORT).show();
                           }
                           @Override
                           public void onError(String error) {
                               Log.e(TAG,"error="+error);
                           }
                       });
           }
        }else {
            startActivity(new Intent(mContext,LoginActivity.class));
        }
    }
    private void  updateCollectStatus (){
        if (isCollect){
            ivCollect.setImageResource(R.drawable.bg_collect_out);
        }else {
            ivCollect.setImageResource(R.drawable.bg_collect_in);
        }
    }
    class updateCartNumReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            updateCartNum();

        }
    }
    updateCartNumReceiver mReceiver;
    private void setUpdateCartCountListener() {
        mReceiver = new updateCartNumReceiver();
        IntentFilter filter = new IntentFilter("update_cart_list");
        registerReceiver(mReceiver, filter);
    }
    private void updateCartNum() {
        int count = Utils.sumCartCount();
        Log.e(TAG, "-----count=" + count);
        if (!DemoHXSDKHelper.getInstance().isLogined() || count == 0) {
            tvCartCount.setText(String.valueOf(0));
            tvCartCount.setVisibility(View.GONE);
        } else {
            tvCartCount.setText(String.valueOf(count));
            tvCartCount.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mReceiver!=null) {
            unregisterReceiver(mReceiver);

        }
    }


}
