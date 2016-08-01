package cn.ucai.fulicenter.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;
import android.widget.TextView;

import cn.ucai.fulicenter.R;

public class FuliCenterMainActivity extends BaseActivity {
RadioButton mrbNewGood;
RadioButton mrbBoutique;
RadioButton mrbCategory;
RadioButton mrbCart;
RadioButton mrbContact;
    TextView tvCartView;
    RadioButton[] mrbTabs;
    int index=0;
    int currentIndedx;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fuli_center_main);
        initView();
    }

    private void initView() {
        mrbNewGood = (RadioButton) findViewById(R.id.rbGoodNews);
        mrbBoutique = (RadioButton) findViewById(R.id.rbBoutique);
        mrbCategory = (RadioButton) findViewById(R.id.rbCategory);
        mrbCart = (RadioButton) findViewById(R.id.rbCart);
        mrbContact = (RadioButton) findViewById(R.id.rbContact);
        tvCartView = (TextView) findViewById(R.id.tvCartHint);
        mrbTabs=new RadioButton[5];
        mrbTabs[0]=mrbNewGood;
        mrbTabs[1]=mrbBoutique;
        mrbTabs[2]=mrbCategory;
        mrbTabs[3]=mrbCart;
        mrbTabs[4]=mrbContact;

    }
    public  void  onCheckedChanged (View view){
        switch (view.getId()){
            case R.id.rbGoodNews:
                index=0;
                break;
            case R.id.rbBoutique:
                index=1;

                break;
            case R.id.rbCategory:
                index=2;
                break;
            case R.id.rbCart:
                index=3;
                break;
            case R.id.tvCartHint:
                index=4;
                break;
        }
        if (index!=currentIndedx){
            setCurrentIndexStatus(index);
            currentIndedx=index;
        }
    }

    private void setCurrentIndexStatus(int index) {
        for (int i=0;i<mrbTabs.length;i++){
            if (index==i){
                mrbTabs[i].setChecked(true);
            }else {
                mrbTabs[i].setChecked(false);
            }
        }
    }


}
