package cn.ucai.fulicenter.activity;

import android.os.Bundle;
import android.view.Display;
import android.widget.EditText;
import android.widget.Spinner;

import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.view.DisplayUtils;

/**
 * Created by Administrator on 2016/8/11 0011.
 */
public class BuyActivity extends BaseActivity {
    BuyActivity mContext;
    EditText edOrderName;
    EditText edOrderPhone;
    EditText edOrderStreet;
    Spinner spOrderProvinc;

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        mContext=this;
        setContentView(R.layout.activity_buy);
        initView();
    }

    private void initView() {
        DisplayUtils.initBackTitle(mContext,"填写收货地址");
        edOrderName = (EditText) findViewById(R.id.ed_order_name);
        edOrderPhone = (EditText) findViewById(R.id.ed_order_phone);
        edOrderStreet = (EditText) findViewById(R.id.ed_order_street);
        spOrderProvinc = (Spinner) findViewById(R.id.sp_order_provinc);
    }
}
