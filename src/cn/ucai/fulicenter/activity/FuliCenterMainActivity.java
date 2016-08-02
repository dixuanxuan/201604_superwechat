package cn.ucai.fulicenter.activity;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.RadioButton;
import android.widget.TextView;

import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.fragment.BoutiqueFragment;
import cn.ucai.fulicenter.fragment.CartFragment;
import cn.ucai.fulicenter.fragment.ContactFragment;
import cn.ucai.fulicenter.fragment.NewGoodFragment;

public class FuliCenterMainActivity extends BaseActivity {
    RadioButton mrbNewGood;
    RadioButton mrbBoutique;
    RadioButton mrbCategory;
    RadioButton mrbCart;
    RadioButton mrbContact;
    TextView tvCartHint;
    int index;
    int currentIndex;
    RadioButton[] mrbTabs;
    ViewPager mViewPager;
    Fragment[] mFragment;
    ViewPageAdapter mViewPageAdapter;
    NewGoodFragment mNewGoodFragment;
    @Override
    protected  void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fuli_center_main);
        initFragment();
        initView();
    }

    private void initFragment() {

        mFragment=new Fragment[5];
        mFragment[0]=new NewGoodFragment();
        mFragment[1]=new BoutiqueFragment();
        mFragment[2]=new CartFragment();
        mFragment[3]=new CartFragment();
        mFragment[4]=new ContactFragment();

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
        mNewGoodFragment=new NewGoodFragment();

        // 添加显示第一个fragment
      /*  getSupportFragmentManager().
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

        return fragments[i];
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
                index=4;
                mViewPager.setCurrentItem(4);
                break;
        }
        if (index!=currentIndex){
            setRadioButtonStatus(index);
            index=currentIndex;
        }
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


}
