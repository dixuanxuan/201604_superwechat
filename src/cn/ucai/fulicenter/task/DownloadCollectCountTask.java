package cn.ucai.fulicenter.task;

import android.content.Context;
import android.content.Intent;
import android.os.Message;
import android.util.Log;

import java.util.List;
import java.util.Map;

import cn.ucai.fulicenter.FuLiCenterApplication;
import cn.ucai.fulicenter.I;
import cn.ucai.fulicenter.bean.MessageBean;
import cn.ucai.fulicenter.bean.Result;
import cn.ucai.fulicenter.bean.UserAvatar;
import cn.ucai.fulicenter.data.OkHttpUtils2;
import cn.ucai.fulicenter.utils.Utils;

/**
 * Created by Administrator on 2016/7/20 0020.
 */
public class DownloadCollectCountTask {
    private  static  final  String TAG=DownloadCollectCountTask.class.getSimpleName();
    String userName;
    Context mContext;

    public DownloadCollectCountTask(Context context, String userName) {
        mContext=context;
        this.userName = userName;
    }
    public  void  excute (){
        final OkHttpUtils2<MessageBean> utils2=new OkHttpUtils2<MessageBean>();
        utils2.setRequestUrl(I.REQUEST_FIND_COLLECT_COUNT)
                .addParam(I.Collect.USER_NAME,userName)
                .targetClass(MessageBean.class)
                .execute(new OkHttpUtils2.OnCompleteListener<MessageBean>() {
                    @Override
                    public void onSuccess(MessageBean msg) {
                        Log.e(TAG,"msg="+msg);
                     //  Result result= Utils.getListResultFromJson(s, UserAvatar.class);
                     //   Log.e(TAG,"result="+result);
                        if (msg!=null){
                            if (msg.isSuccess()){
                                FuLiCenterApplication.getInstance().setCollectCount(Integer.valueOf(msg.getMsg()));
                            }else {
                                FuLiCenterApplication.getInstance().setCollectCount(0);
                            }
                        }
                            mContext.sendStickyBroadcast(new Intent("update_collection"));
                    }
                    @Override
                    public void onError(String error) {
                        Log.e(TAG,"error="+error);
                    }
                });
    }

}
