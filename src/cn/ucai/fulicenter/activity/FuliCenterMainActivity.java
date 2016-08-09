package cn.ucai.fulicenter.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.Tag;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.widget.RadioButton;
import android.widget.TextView;

import cn.ucai.fulicenter.DemoHXSDKHelper;
import cn.ucai.fulicenter.FuLiCenterApplication;
import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.fragment.BoutiqueFragment;
import cn.ucai.fulicenter.fragment.CartFragment;
import cn.ucai.fulicenter.fragment.CategoryFragment;
import cn.ucai.fulicenter.fragment.PersonCenterFragment;
import cn.ucai.fulicenter.fragment.NewGoodFragment;
import cn.ucai.fulicenter.utils.Utils;

public class FuliCenterMainActivity extends BaseActivity {
    RadioButton mrbNewGood;
    RadioButton mrbBoutique;
    RadioButton mrbCategory;
    RadioButton mrbCart;
    RadioButton mrbContact;
    TextView tvCartHint;
   private int index;
    private int currentIndex;
    RadioButton[] mrbTabs;
    ViewPager mViewPager;
    Fragment[] mFragment;
    ViewPageAdapter mViewPageAdapter;
    NewGoodFragment mNewGoodFragment;

    updateCartNumReceiver mReceiver;
    private final  String TAG= FuLiCenterApplication.class.getCanonicalName();
    Fragment[] getmFragment;
    public  static  final  int ACTION_LOGIN=100;
    @Override
    protected  void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fuli_center_main);
        initFragment();
        initView();
        setListener();
    }

    private void setListener() {
        setUpdateCartCountListener();
    }

    private void initFragment() {

        mFragment=new Fragment[5];
        mFragment[0]=new NewGoodFragment();
        mFragment[1]=new BoutiqueFragment();
        mFragment[2]=new CategoryFragment();
        mFragment[3]=new CartFragment();
        mFragment[4]=new PersonCenterFragment();

    }

    private void initView() {
        mrbNewGood = (RadioButton) findViewById(R.id.rbGoodNews);
        mrbBoutique = (RadioButton) findViewById(R.id.rbBoutique);
        mrbCategory = (RadioButton) findViewById(R.id.rbCategory);
        mrbCart = (RadioButton) findViewById(R.id.rbCart);
        mrbContact = (RadioButton) findViewById(R.id.rbContact);
        tvCartHint = (TextView) findViewById(R.id.tvCartHint);
        mrbTabs=new RadioButton[5];
        mrbTabs[0]=mrbNewGood;
        mrbTabs[1]=mrbBoutique;
        mrbTabs[2]=mrbCategory;
        mrbTabs[3]=mrbCart;
        mrbTabs[4]=mrbContact;
        //mNewGoodFragment=new NewGoodFragment();

   /*     // 添加显示第一个fragment
      getSupportFragmentManager().
                beginTransaction()
                .add(cn.ucai.fulicenter.R.id.fragment_container, mNewGoodFragment)
              //  .add(cn.ucai.fulicenter.R.id.fragment_container, contactListFragment)
              //  .hide(contactListFragment).
                .show(mNewGoodFragment)
                .commit();*/
        mViewPager= (ViewPager) findViewById(R.id.vpPager);
        mViewPageAdapter=new ViewPageAdapter(getSupportFragmentManager(),mFragment);
        mViewPager.setAdapter(mViewPageAdapter);
    }
class  ViewPageAdapter extends FragmentStatePagerAdapter {
    Fragment[] fragments;
    public ViewPageAdapter(FragmentManager fm,Fragment[] fragments) {
        super(fm);
        this.fragments=fragments;
    }

    @Override
    public Fragment getItem(int i) {
        return fragments[i] ;
    }

    @Override
    public int getCount() {
        return fragments.length;
    }
}

    public  void  onCheckedChange(View view){
        switch (view.getId()){
            case  R.id.rbGoodNews:
                index=0;
                   mViewPager.setCurrentItem(0);
                break;
            case  R.id.rbBoutique:
                index=1;
                mViewPager.setCurrentItem(1);
                break;
            case  R.id.rbCategory:
                index=2;
                mViewPager.setCurrentItem(2);
                break;
            case  R.id.rbCart:
                index=3;
                mViewPager.setCurrentItem(3);
                break;
            case  R.id.rbContact:
                if (DemoHXSDKHelper.getInstance().isLogined()){
                    index=4 ;
                    mViewPager.setCurrentItem(4);
                }else {
                    gotoLogin();
                }
                break;
        }
        Log.e(TAG,"index="+index+",currentIndex"+currentIndex);
        if (index!=currentIndex){
            setRadioButtonStatus(index);
            currentIndex=index;
        }
        //setFragment();
    }
    private void setFragment(){
        if (index!=currentIndex){
            FragmentTransaction trx=getSupportFragmentManager().beginTransaction();
            trx.hide(mFragment[currentIndex]);
            if (!mFragment[index].isAdded()){
                trx.add(R.id.fragment_container,mFragment[index]);
            }
            trx.show(mFragment[index]).commit();
            setRadioButtonStatus(index);
            currentIndex=index;
        }
    }
    private void gotoLogin() {
        startActivityForResult(new Intent(this,LoginActivity.class),ACTION_LOGIN);
//        if(DemoHXSDKHelper.getInstance().isLogined()){
//            setRadioButtonStatus(index);
//        }
    }

    private void setRadioButtonStatus(int index) {
        for (int i=0;i<mrbTabs.length;i++){
            if (index==i){
                mrbTabs[i].setChecked(true);
            }else {
                mrbTabs[i].setChecked(false);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.e(TAG,"requestCode="+requestCode+",resultCode"+resultCode);
        if (requestCode==ACTION_LOGIN){
            if (DemoHXSDKHelper.getInstance().isLogined()){
                setRadioButtonStatus(currentIndex);
                index=4;
            }else {
                setRadioButtonStatus(currentIndex);
            }
//            }else {
//                mViewPager.setCurrentItem(index);
//                Log.e(TAG,"index="+index+",currentIndex"+currentIndex);
//            }
        }
    }

   @Override
    protected void onResume() {
        super.onResume();
         currentIndex=index;
        if (!DemoHXSDKHelper.getInstance().isLogined()&&index==4){
            index=0;
            setRadioButtonStatus(index);
           // setRadioButtonStatus(currentIndex);
        }
         mViewPager.setCurrentItem(index);
        setRadioButtonStatus(index);
            //setFragment();
    }
    private  void setUpdateCartCountListener(){
        mReceiver=new updateCartNumReceiver();
        IntentFilter filter=new IntentFilter("update_cart_list");
        registerReceiver(mReceiver,filter);
    }
    class   updateCartNumReceiver extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {
            updateCartNum();
        }
    }

    private void updateCartNum() {
        int count = Utils.sumCartCount();
        if (!DemoHXSDKHelper.getInstance().isLogined()||count==0){
            tvCartHint.setText(String.valueOf(0));
            tvCartHint.setVisibility(View.GONE);

        }else {
            tvCartHint.setText(String.valueOf(count));
            tvCartHint.setVisibility(View.VISIBLE);

        }
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mReceiver!=null){
            unregisterReceiver(mReceiver);
        }
    }
}
