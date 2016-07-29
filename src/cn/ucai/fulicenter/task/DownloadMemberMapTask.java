package cn.ucai.fulicenter.task;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.ucai.fulicenter.I;
import cn.ucai.fulicenter.FuLiCenterServerApplication;
import cn.ucai.fulicenter.bean.MemberUserAvatar;
import cn.ucai.fulicenter.bean.Result;
import cn.ucai.fulicenter.data.OkHttpUtils2;
import cn.ucai.fulicenter.utils.Utils;

/**
 * Created by Administrator on 2016/7/20 0020.
 */
public class DownloadMemberMapTask {
    private  static  final  String TAG=DownloadMemberMapTask.class.getSimpleName();
    String hxid;
    Context mContext;

    public DownloadMemberMapTask(Context context, String userName) {
        mContext=context;
        this.hxid = userName;
    }
    public  void  excute (){
        final OkHttpUtils2<String> utils2=new OkHttpUtils2<>();
        utils2.setRequestUrl(I.REQUEST_DOWNLOAD_GROUP_MEMBERS_BY_HXID)
                .addParam(I.Member.GROUP_HX_ID, hxid)
                .targetClass(String.class)
                .execute(new OkHttpUtils2.OnCompleteListener<String>() {
                    @Override
                    public void onSuccess(String s) {
                        Log.e(TAG,"s="+s);
                       Result result= Utils.getListResultFromJson(s, MemberUserAvatar.class);
                        Log.e(TAG,"result="+result);
                        List<MemberUserAvatar> list= (List<MemberUserAvatar>) result.getRetData();
                        if (list!=null&&list.size()>0){
                            Log.e(TAG,"list="+list.size()+"");
                            Map<String, HashMap<String, MemberUserAvatar>> memberMap = FuLiCenterServerApplication.getInstance().getMemberMap();
                            if (!memberMap.containsKey(hxid)){
                                memberMap.put(hxid,new HashMap<String, MemberUserAvatar>());
                            }
                            HashMap<String, MemberUserAvatar> hxidMembers = memberMap.get(hxid);
                            for (MemberUserAvatar u:list){
                                hxidMembers.put(u.getMUserName(),u);
                            }
                            mContext.sendStickyBroadcast(new Intent("update_member_list"));
                        }
                    }

                    @Override
                    public void onError(String error) {
                        Log.e(TAG,"error="+error);

                    }
                });
    }
}
