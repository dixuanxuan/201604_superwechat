package cn.ucai.fulicenter.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import cn.ucai.fulicenter.D;
import cn.ucai.fulicenter.FuLiCenterApplication;
import cn.ucai.fulicenter.I;
import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.activity.GoodDetailsActivity;
import cn.ucai.fulicenter.bean.CollectBean;
import cn.ucai.fulicenter.bean.MessageBean;
import cn.ucai.fulicenter.data.OkHttpUtils2;
import cn.ucai.fulicenter.task.DownloadCollectCountTask;
import cn.ucai.fulicenter.utils.ImageUtils;
import cn.ucai.fulicenter.view.FooterViewHolder;

/**
 * Created by Administrator on 2016/8/1 0001.
 */
public class CollectAdapter extends RecyclerView.Adapter<ViewHolder> {
    final  static  String TAG=CollectAdapter.class.getSimpleName();
    Context mContext;
    List<CollectBean> mGoodList;
    CollectViewHoder mCollectViewHoder;
    boolean isMore;
    String footerText;
    int sortBy;
    FooterViewHolder mFooterViewHolder;

    public boolean isMore() {
        return isMore;
    }

    public void setMore(boolean more) {
        isMore = more;
    }

    public String getFooterText() {
        return footerText;
    }

    public void setSortBy(int sortBy) {
        this.sortBy = sortBy;
        notifyDataSetChanged();
    }

   /* public void setFooterText(String footerText) {
        this.footerText = footerText;
        notifyDataSetChanged();
    }*/

    public CollectAdapter(Context mContext, List<CollectBean> list) {
        this.mContext = mContext;
        mGoodList=new ArrayList<CollectBean>();
        mGoodList.addAll(list);
        sortBy=I.SORT_BY_ADDTIME_DESC;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View layout;
        ViewHolder holder=null;
        switch (viewType){
            case I.TYPE_FOOTER:
             layout=LayoutInflater.from(mContext).inflate(R.layout.item_footer,parent,false);
                holder=new FooterViewHolder(layout);
                break;
            case  I.TYPE_ITEM:
                layout=LayoutInflater.from(mContext).inflate(R.layout.item_collect,parent,false);
                holder=new CollectViewHoder(layout);
                break;
        }
        return  holder;
    }

    public void initItem(ArrayList<CollectBean> list) {
        if (mGoodList!=null){
            mGoodList.clear();
        }
        mGoodList.addAll(list);
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if (holder instanceof FooterViewHolder){
            mFooterViewHolder= (FooterViewHolder) holder;
            mFooterViewHolder.tvFooter.setText(footerText);
        }
        if (holder instanceof CollectViewHoder){
            mCollectViewHoder = (CollectViewHoder) holder;
            final CollectBean collect = mGoodList.get(position);
        //    mCartViewHolder.ivBoutiqueThumb.setImageURI(good.getGoodsThumb());
            ImageUtils.setGoodThumb(mContext, mCollectViewHoder.ivGoodThumb,collect.getGoodsThumb());
            mCollectViewHoder.tvGoodName.setText(collect.getGoodsName());
            Picasso.with(mContext).load(R.drawable.delete).placeholder(R.drawable.delete).error(R.drawable.delete).into(mCollectViewHoder.ivDelete);
            mCollectViewHoder.ivDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    OkHttpUtils2<MessageBean> utils2=new OkHttpUtils2<MessageBean>();
                    utils2.setRequestUrl(I.REQUEST_DELETE_COLLECT)
                            .addParam(I.Collect.USER_NAME, FuLiCenterApplication.getInstance().getUserName())
                            .addParam(I.Collect.GOODS_ID,collect.getGoodsId()+"")
                            .targetClass(MessageBean.class)
                            .execute(new OkHttpUtils2.OnCompleteListener<MessageBean>() {
                                @Override
                                public void onSuccess(MessageBean result) {
                                    Log.e(TAG,"result="+result);
                                    if (result!=null&&result.isSuccess()){
                                        new DownloadCollectCountTask(mContext,FuLiCenterApplication.getInstance().getUserName()).excute();
                                        mGoodList.remove(collect);
                                        notifyDataSetChanged();
                                    }else {
                                        Log.e(TAG,"fail");
                                    }
                                    Toast.makeText(mContext,result.getMsg(),Toast.LENGTH_SHORT).show();

                                }

                                @Override
                                public void onError(String error) {
                                    Log.e(TAG,"error="+error);
                                }
                            });

                }
            });
            mCollectViewHoder.layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mContext.startActivity(new Intent(mContext,GoodDetailsActivity.class)
                    .putExtra(D.GoodDetails.KEY_GOODS,collect.getGoodsId()));
                }
            });
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (position==getItemCount()-1){
            return  I.TYPE_FOOTER;
        }else {
            return  I.TYPE_ITEM;
        }
    }

    @Override
    public int getItemCount() {

        return mGoodList!=null?mGoodList.size()+1:1;
       //return  mCartList.size();
    }

    public void addItem(ArrayList<CollectBean> list) {
        if (mGoodList!=null){
            mGoodList.addAll(list);
        }
        notifyDataSetChanged();
    }

    class CollectViewHoder extends ViewHolder{
    LinearLayout layout;
        ImageView ivGoodThumb;
        ImageView ivDelete;
        TextView tvGoodName;

        public CollectViewHoder(View itemView) {
            super(itemView);
            layout = (LinearLayout) itemView.findViewById(R.id.layout_good);
            ivGoodThumb = (ImageView) itemView.findViewById(R.id.niv_good_thumb);
            tvGoodName= (TextView) itemView.findViewById(R.id.tv_good_name);
            ivDelete = (ImageView) itemView.findViewById(R.id.iv_collect_delete);

        }
    }

}
