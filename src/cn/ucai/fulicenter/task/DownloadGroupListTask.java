package cn.ucai.fulicenter.task;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.util.List;

import cn.ucai.fulicenter.I;
import cn.ucai.fulicenter.SuperWeChatApplication;
import cn.ucai.fulicenter.bean.GroupAvatar;
import cn.ucai.fulicenter.bean.Result;
import cn.ucai.fulicenter.data.OkHttpUtils2;
import cn.ucai.fulicenter.utils.Utils;

/**
 * Created by Administrator on 2016/7/20 0020.
 */
public class DownloadGroupListTask {
    private  static  final  String TAG=DownloadGroupListTask.class.getSimpleName();
    String userName;
    Context mContext;

    public DownloadGroupListTask(Context context, String userName) {
        mContext=context;
        this.userName = userName;
    }
    public  void  excute (){
        final OkHttpUtils2<String> utils2=new OkHttpUtils2<>();
        utils2.setRequestUrl(I.REQUEST_FIND_GROUP_BY_USER_NAME)
                .addParam(I.User.USER_NAME,userName)
                .targetClass(String.class)
                .execute(new OkHttpUtils2.OnCompleteListener<String>() {
                    @Override
                    public void onSuccess(String s) {
                        Log.e(TAG,"s="+s);
                       Result result= Utils.getListResultFromJson(s, GroupAvatar.class);
                        Log.e(TAG,"result="+result);
                        List<GroupAvatar> list= (List<GroupAvatar>) result.getRetData();
                        Log.e(TAG,"list="+list);
                        if (list!=null&&list.size()>0){
                            SuperWeChatApplication.getInstance().setGrouplist(list);
                            for (GroupAvatar g:list){
                                SuperWeChatApplication.getInstance().getGroupMap().put(g.getMGroupHxid(),g);
                            }
                            mContext.sendStickyBroadcast(new Intent("update_group_list"));
                        }
                    }

                    @Override
                    public void onError(String error) {
                        Log.e(TAG,"error="+error);

                    }
                });
    }
}
