package com.jingkai.asset.widget.photopicture;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.jingkai.asset.R;
import com.jingkai.asset.utils.view.GlideUtils;

import java.util.List;

public class NoScrollGridAdapter extends BaseAdapter {
    String TAG = "NoScrollGrid";
    /**
     * 上下文
     */
    private Context ctx;
    /**
     * 图片Url集合
     */
    private List<String> imageUrls;
    private int columnNum;

    public NoScrollGridAdapter(Context ctx, List<String> urls, int columnNum) {
        this.ctx = ctx;
        this.imageUrls = urls;
        this.columnNum = columnNum;
    }

    @Override
    public int getCount() {
        return imageUrls == null ? 0 : imageUrls.size();
    }

    @Override
    public Object getItem(int position) {
        return imageUrls.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        convertView = View.inflate(ctx, R.layout.item_gridview, null);
        ImageView imageView = convertView.findViewById(R.id.iv_image);

        GlideUtils.loadImage(ctx,imageView,imageUrls.get(position));
        return convertView;
    }

    class ViewHolder {
        ImageView imageView;
    }
}
