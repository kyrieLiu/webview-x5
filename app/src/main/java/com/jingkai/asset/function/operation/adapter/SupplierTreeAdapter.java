package com.jingkai.asset.function.operation.adapter;

import android.content.Context;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jingkai.asset.R;
import com.jingkai.asset.common.adapter.ParentTreeListViewAdapter;
import com.jingkai.asset.common.entity.Node;
import com.jingkai.asset.utils.view.ViewUtil;

import java.util.List;

/**
 * Created by liuyin on 2019/5/23 16:22
 * Describe: 供应商列表
 */
public class SupplierTreeAdapter<T> extends ParentTreeListViewAdapter<T> {

    public SupplierTreeAdapter(ListView mTree, Context context,
                               List<T> datas, int defaultExpandLevel)
            throws IllegalArgumentException, IllegalAccessException {
        super(mTree, context, datas, defaultExpandLevel);
    }

    @SuppressWarnings("unchecked")
    @Override
    public View getConvertView(final Node node, final int position, View convertView,
                               ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.item_supplier, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.icon = (ImageView) convertView.findViewById(R.id.id_treenode_icon);
            viewHolder.label = (TextView) convertView.findViewById(R.id.id_treenode_name);
            viewHolder.view10 = (View) convertView.findViewById(R.id.view_item_tree_interval_10);
            viewHolder.view1 = (View) convertView.findViewById(R.id.view_item_tree_interval_1);
            viewHolder.mTvTime = (TextView) convertView.findViewById(R.id.tv_tree_number);
            viewHolder.mLlParent = convertView.findViewById(R.id.ll_tree_company_parent);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        if (node.isRoot()) {//如果顶级层
            viewHolder.view10.setVisibility(View.VISIBLE);
            viewHolder.view1.setVisibility(View.GONE);
            viewHolder.icon.setVisibility(View.VISIBLE);
            viewHolder.icon.setImageResource(R.mipmap.left_title_icon);
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) viewHolder.icon.getLayoutParams();
            params.width = ViewUtil.dp2px(4);
            params.height = ViewUtil.dp2px(12);
            viewHolder.icon.setLayoutParams(params);
            viewHolder.label.setTextSize(TypedValue.COMPLEX_UNIT_SP, 17);
        } else {
            viewHolder.label.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
            viewHolder.view10.setVisibility(View.GONE);
            viewHolder.view1.setVisibility(View.VISIBLE);
            if (node.getIcon() == -1) {
                viewHolder.icon.setVisibility(View.GONE);
            } else {
                viewHolder.icon.setVisibility(View.VISIBLE);
                viewHolder.icon.setImageResource(node.getIcon());
                RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) viewHolder.icon.getLayoutParams();
                params.width = ViewUtil.dp2px(12);
                params.height = ViewUtil.dp2px(12);
                viewHolder.icon.setLayoutParams(params);
            }
        }
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) viewHolder.mLlParent.getLayoutParams();
        if (node.getRoleLevel() == 1) {//如果是职能层级
            convertView.setBackgroundColor(mContext.getResources().getColor(R.color.white));
            viewHolder.mTvTime.setVisibility(View.VISIBLE);
            viewHolder.mTvTime.setText(String.valueOf(node.getAllCount()));
            viewHolder.label.setText(node.getName());
            params.height = ViewUtil.dp2px(50);
        } else {
            convertView.setBackgroundColor(mContext.getResources().getColor(R.color.white));
            viewHolder.mTvTime.setVisibility(View.GONE);
            if (node.getRoleLevel() == 2) {
                convertView.setBackground(mContext.getResources().getDrawable(R.drawable.selector_fill_rectangle_white_press));
                viewHolder.label.setText(node.getName() + "\n" + node.getData());
                params.height = ViewUtil.dp2px(80);
            } else {
                viewHolder.label.setText(node.getName());
                params.height = ViewUtil.dp2px(50);
            }
        }
        viewHolder.mLlParent.setLayoutParams(params);

        viewHolder.mLlParent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!node.isLeaf() || node.getRoleLevel() != 2) {//该层不是房间
                    clickParentItem(node);
                } else {
                    clickLeafItem(node, position);
                }
            }
        });

        return convertView;
    }

    private final class ViewHolder {
        ImageView icon;
        TextView label, mTvTime;
        View view10, view1;
        RelativeLayout mLlParent;
    }

}
