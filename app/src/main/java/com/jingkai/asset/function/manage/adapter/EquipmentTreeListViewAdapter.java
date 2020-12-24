package com.jingkai.asset.function.manage.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.jingkai.asset.R;
import com.jingkai.asset.common.adapter.ParentTreeListViewAdapter;
import com.jingkai.asset.common.entity.Node;

import java.util.List;

public class EquipmentTreeListViewAdapter<T> extends ParentTreeListViewAdapter<T> {

    public EquipmentTreeListViewAdapter(ListView mTree, Context context,
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
            convertView = mInflater.inflate(R.layout.item_vacant_detail, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.icon = (ImageView) convertView.findViewById(R.id.id_treenode_icon);
            viewHolder.labelName = (TextView) convertView.findViewById(R.id.id_treenode_name);
            viewHolder.view10 = (View) convertView.findViewById(R.id.view_item_tree_interval_10);
            viewHolder.view1 = (View) convertView.findViewById(R.id.view_item_tree_interval_1);
            viewHolder.mTvStatus = (TextView) convertView.findViewById(R.id.tv_treenode_statusName);
            viewHolder.mLlParent = (LinearLayout) convertView.findViewById(R.id.ll_tree_parent);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.labelName.setText(node.getName());

        if (node.getIcon() == -1) {
            viewHolder.icon.setVisibility(View.GONE);
        } else {
            viewHolder.icon.setVisibility(View.VISIBLE);
            viewHolder.icon.setImageResource(node.getIcon());
        }

        if (node.isRoot()) {//如果是最外层,显示宽的分割线
            viewHolder.view10.setVisibility(View.VISIBLE);
            viewHolder.view1.setVisibility(View.GONE);
        } else {
            viewHolder.view10.setVisibility(View.GONE);
            viewHolder.view1.setVisibility(View.VISIBLE);

        }

        if (node.isLeaf() && node.getRoleLevel() == 2) {//如果是最后一层,并且是房间层
            convertView.setBackground(mContext.getResources().getDrawable(R.drawable.selector_fill_rectangle_white_press));
            viewHolder.mTvStatus.setVisibility(View.VISIBLE);
        } else {
            convertView.setBackgroundColor(mContext.getResources().getColor(R.color.white));
            viewHolder.mTvStatus.setVisibility(View.GONE);
        }

        viewHolder.mLlParent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!node.isLeaf() || node.getRoleLevel() != 2) {//该层不是房间
                    clickParentItem(node);
                } else {
                    clickLeafItem(node,position);
                }
            }
        });

        return convertView;
    }

    private final class ViewHolder {
        ImageView icon;
        TextView labelName, mTvStatus;
        View view10, view1;
        LinearLayout mLlParent;
    }

}
