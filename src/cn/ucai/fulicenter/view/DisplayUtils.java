package cn.ucai.fulicenter.view;

import android.app.Activity;
import android.view.View;

import cn.ucai.fulicenter.R;

/**
 * Created by Administrator on 2016/8/3 0003.
 */
public class DisplayUtils  {
    public  static  void  iniitBack(final Activity activity){
        activity.findViewById(R.id.backClickArea).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.finish();
            }
        });
    }
}
