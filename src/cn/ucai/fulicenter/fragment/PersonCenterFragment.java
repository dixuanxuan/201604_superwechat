package cn.ucai.fulicenter.fragment;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.easemob.chat.EMChatManager;

import java.util.ArrayList;
import java.util.HashMap;

import cn.ucai.fulicenter.DemoHXSDKHelper;
import cn.ucai.fulicenter.FuLiCenterApplication;
import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.activity.CollectActivity;
import cn.ucai.fulicenter.activity.SettingsActivity;
import cn.ucai.fulicenter.bean.UserAvatar;
import cn.ucai.fulicenter.utils.ImageUtils;
import cn.ucai.fulicenter.utils.UserUtils;

/**
 * A simple {@link Fragment} subclass.
 */
public class PersonCenterFragment extends Fragment {
    final  static  String TAG=PersonCenterFragment.class.getSimpleName();
    Context mContext;
    //资源文件
    private  int[]  pic_path={
            R.drawable.order_list1,
            R.drawable.order_list2,
            R.drawable.order_list3,
            R.drawable.order_list4,
            R.drawable.order_list5};
    ImageView mivUserAvatar;
    TextView mtvUserName;
    TextView mtvCollecCount;
    TextView mtvSettings;
    ImageView mivMessage;
    LinearLayout mLayoutCenterCollect;
    RelativeLayout mLayoutCenterInfo;

    int mCollectCount;

    public PersonCenterFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mContext=getActivity();
        View layout=inflater.inflate(R.layout.fragment_personal_centert, container, false);
        initView(layout);
        setListerner();
        initData();
        return layout;
    }

    private void setListerner() {
        MyClickListener listener=new MyClickListener();
        mtvSettings.setOnClickListener(listener);
        mLayoutCenterCollect.setOnClickListener(listener);
        updateCollectCountListener();

    }
    class  MyClickListener implements  View.OnClickListener{
        @Override
        public void onClick(View v) {
            if (DemoHXSDKHelper.getInstance().isLogined()){
                switch (v.getId()){
                    case R.id.tv_center_settings:
                    case R.id.center_user_info:
                        startActivity(new Intent(mContext, SettingsActivity.class));
                        break;
                    case R.id.layout_center_collect:
                        startActivity(new Intent(mContext, CollectActivity.class));
                        break;
                }
            }else {
                Log.e(TAG,"not Logined ...");
            }
        }
    }

    private void initData() {
//        mCollectCount=FuLiCenterApplication.getInstance().getCollectCount();
//        mtvCollecCount.setText(""+mCollectCount);
        if (DemoHXSDKHelper.getInstance().isLogined()){
            UserAvatar user = FuLiCenterApplication.getInstance().getUser();
            Log.e(TAG,"user="+user);
            UserUtils.setAappCurrentUserAvatar(mContext,mivUserAvatar);
            UserUtils.setCurrentUserNick(mtvUserName);
        }
    }



    private void initView(View layout) {
        mivUserAvatar = (ImageView) layout.findViewById(R.id.iv_user_avatar);
        mtvUserName = (TextView) layout.findViewById(R.id.tv_user_name);
        mtvCollecCount = (TextView) layout.findViewById(R.id.tv_collect_count);
        mLayoutCenterCollect= (LinearLayout) layout.findViewById(R.id.layout_center_collect);
        mLayoutCenterInfo= (RelativeLayout) layout.findViewById(R.id.center_user_info);
        mtvSettings= (TextView) layout.findViewById(R.id.tv_center_settings);
        mivMessage = (ImageView) layout.findViewById(R.id.iv_person_center_msg);
        initOrderList(layout);

    }
    private void initOrderList(View layout) {
        //显示GradiView的界面
        GridView  mOrderLsit = (GridView) layout.findViewById(R.id.center_user_order_lis);
        ArrayList<HashMap<String,Object>> imageList=new ArrayList<>();
        //使用HashMap将图片添加到一个数组中，HashMap<String,Object>类型，  装到map中的数据是图片资源的id，，而不是图片本身
        //如果使用findViewById(R.drawable.iamge)这样把真正的图片取出来，放到map中是无法正常显示的
        HashMap<String,Object> map1=new HashMap<>();
        map1.put("image",R.drawable.order_list1);
        imageList.add(map1);
        HashMap<String,Object> map2=new HashMap<>();
        map2.put("image",R.drawable.order_list2);
        imageList.add(map2);
        HashMap<String,Object> map3=new HashMap<>();
        map3.put("image",R.drawable.order_list3);
        imageList.add(map3);
        HashMap<String,Object> map4=new HashMap<>();
        map4.put("image",R.drawable.order_list4);
        imageList.add(map4);
        HashMap<String,Object> map5=new HashMap<>();
        map5.put("image",R.drawable.order_list5);
        imageList.add(map5);
        SimpleAdapter simpleAdapter=new SimpleAdapter
                (mContext,imageList,R.layout.simple_grid_item,new String[]{"image"},new int[]{R.id.iv_Image});
        mOrderLsit.setAdapter(simpleAdapter);
    }
    class UpdateCollectCount extends BroadcastReceiver{
        @Override
        public void onReceive(Context context, Intent intent) {
            int count = FuLiCenterApplication.getInstance().getCollectCount();
            Log.e(TAG,"count="+count);
            mtvCollecCount.setText(count+"");
        }
    } UpdateCollectCount mReceiver;
    private  void updateCollectCountListener(){
        mReceiver=new UpdateCollectCount();
        IntentFilter filter=new IntentFilter("update_collection");
        mContext.registerReceiver(mReceiver,filter);
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        mContext.unregisterReceiver(mReceiver);

    }
}
