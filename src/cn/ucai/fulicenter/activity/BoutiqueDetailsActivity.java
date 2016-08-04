package cn.ucai.fulicenter.activity;

import android.os.Bundle;
import android.util.Log;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import cn.ucai.fulicenter.D;
import cn.ucai.fulicenter.I;
import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.bean.AlbumBean;
import cn.ucai.fulicenter.bean.GoodDetailsBean;
import cn.ucai.fulicenter.data.OkHttpUtils2;
import cn.ucai.fulicenter.view.DisplayUtils;
import cn.ucai.fulicenter.view.FlowIndicator;
import cn.ucai.fulicenter.view.SlideAutoLoopView;


/**
 * Created by Administrator on 2016/8/3 0003.
 */
public class BoutiqueDetailsActivity extends  BaseActivity {
    private  static  final  String TAG=BoutiqueDetailsActivity.class.getSimpleName();
    ImageView ivShare;
    ImageView  ivCollect;
    ImageView  ivCart;
    TextView    tvCartCount;

    TextView tvGoodEnglishName;
    TextView tvGoodName;
    TextView tvGoodPriceCurrent;
    TextView tvGoodPriceShop;

    SlideAutoLoopView mSlideAutoLoopView;
    FlowIndicator mFlowIndicator;
    WebView mWebView;
    int goodId;
    BoutiqueDetailsActivity mContext;
    GoodDetailsBean mGoodDetail;

    @Override
    public void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.activity_good_details);
        mContext = this;
        initView();
        initData();
    }
    private void initData() {
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

    /*    private  void  getGoodDetailsByGoodId(OkHttpUtils2.OnCompleteListener<GoodDetailsBean> listener){
            OkHttpUtils2<GoodDetailsBean> utils=new OkHttpUtils2<GoodDetailsBean>();
            utils.setRequestUrl(I.REQUEST_FIND_GOODS_DETAILS)
                    .addParam(D.GoodDetails.KEY_GOODS,String.valueOf(goodId))
                    .targetClass(GoodDetailsBean.class)
                    .execute(listener);

        }*/
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
}
