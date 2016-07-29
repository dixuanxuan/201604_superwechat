package cn.ucai.fulicenter.task;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.util.List;
import java.util.Map;

import cn.ucai.fulicenter.I;
import cn.ucai.fulicenter.FuLiCenterServerApplication;
import cn.ucai.fulicenter.bean.Result;
import cn.ucai.fulicenter.bean.UserAvatar;
import cn.ucai.fulicenter.data.OkHttpUtils2;
import cn.ucai.fulicenter.utils.Utils;

/**
 * Created by Administrator on 2016/7/20 0020.
 */
public class DownloadContactListTask {
    private  static  final  String TAG=DownloadContactListTask.class.getSimpleName();
    String userName;
    Context mContext;

    public DownloadContactListTask(Context context,String userName) {
        mContext=context;
        this.userName = userName;
    }
    public  void  excute (){
        final OkHttpUtils2<String> utils2=new OkHttpUtils2<>();
        utils2.setRequestUrl(I.REQUEST_DOWNLOAD_CONTACT_ALL_LIST)
                .addParam(I.Contact.USER_NAME,userName)
                .targetClass(String.class)
                .execute(new OkHttpUtils2.OnCompleteListener<String>() {
                    @Override
                    public void onSuccess(String s) {
                        Log.e(TAG,"s="+s);
                       Result result= Utils.getListResultFromJson(s, UserAvatar.class);
                        Log.e(TAG,"result="+result);
                        List<UserAvatar> list= (List<UserAvatar>) result.getRetData();
                        Log.e(TAG,"list="+list);
                        if (list!=null&&list.size()>0){
                            FuLiCenterServerApplication.getInstance().setUserlist(list);
                            mContext.sendStickyBroadcast(new Intent("update_contact_list"));
                            Map<String ,UserAvatar> userMap= FuLiCenterServerApplication.getInstance().getUserMap();
                            for (UserAvatar u:list){
                                userMap.put(u.getMUserName(),u);
                            }
                        }
                    }

                    @Override
                    public void onError(String error) {
                        Log.e(TAG,"error="+error);

                    }
                });
    }
}
