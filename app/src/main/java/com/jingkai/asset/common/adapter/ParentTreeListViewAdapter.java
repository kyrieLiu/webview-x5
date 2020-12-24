package com.jingkai.asset.common.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;

import com.jingkai.asset.common.entity.Node;
import com.jingkai.asset.common.helper.TreeHelper;
import com.jingkai.asset.utils.view.ViewUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * tree适配器
 *
 * @param <T>
 */
public abstract class ParentTreeListViewAdapter<T> extends BaseAdapter {

    protected Context mContext;
    /**
     * 存储所有可见的Node
     */
    protected List<Node> mNodes;
    protected LayoutInflater mInflater;
    /**
     * 存储所有的Node
     */
    protected List<Node> mAllNodes;
    /**
     * 存储已经选中的Node
     */
    List<Node> checkedNodes;

    /**
     * 点击的回调接口
     */
    private OnTreeNodeClickListener onTreeNodeClickListener;

    private Node preSelectNode;

    public interface OnTreeNodeClickListener {
        /**
         * 处理node click事件
         *
         * @param node
         * @param position
         */
        void onClick(Node node, int position);

        /**
         * 处理checkbox选择改变事件
         *
         * @param node
         * @param position
         * @param checkedNodes
         */
        void onCheckChange(Node node, int position, List<Node> checkedNodes);
    }

    public void setOnTreeNodeClickListener(
            OnTreeNodeClickListener onTreeNodeClickListener) {
        this.onTreeNodeClickListener = onTreeNodeClickListener;
    }

    /**
     * @param mTree
     * @param context
     * @param datas
     * @param defaultExpandLevel 默认展开几级树
     */
    public ParentTreeListViewAdapter(ListView mTree, Context context, List<T> datas,
                                     int defaultExpandLevel)
            throws IllegalArgumentException, IllegalAccessException {
        mContext = context;
        checkedNodes = new ArrayList<>();
        mInflater = LayoutInflater.from(context);

        //设置节点点击时，可以展开以及关闭；并且将ItemClick事件继续往外公布
//        mTree.setOnItemClickListener(new OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view,
//                                    int position, long id) {
//                expandOrCollapse(position);
//
//            }
//
//        });
        /**
         * 对所有的Node进行排序
         */
        mAllNodes = TreeHelper
                .getSortedNodes(datas, defaultExpandLevel);
        /**
         * 过滤出可见的Node
         */
        mNodes = TreeHelper.filterVisibleNode(mAllNodes);
        Log.d("tag", "mNode长度" + mNodes.size());

    }

    public void setListData(List<T> datas, int defaultExpandLevel) throws IllegalAccessException {
        /**
         * 对所有的Node进行排序
         */
        mAllNodes = TreeHelper
                .getSortedNodes(datas, defaultExpandLevel);
        /**
         * 过滤出可见的Node
         */
        mNodes = TreeHelper.filterVisibleNode(mAllNodes);
        notifyDataSetChanged();
    }

    /**
     * 相应ListView的点击事件 展开或关闭某节点
     *
     * @param position
     */
    public void expandOrCollapse(int position) {
        Node n = mNodes.get(position);

        if (n != null)// 排除传入参数错误异常
        {
            if (!n.isLeaf() || n.getRoleLevel() != 2) {//该层不是房间
                n.setExpand(!n.isExpand());
                mNodes = TreeHelper.filterVisibleNode(mAllNodes);
                notifyDataSetChanged();// 刷新视图
            } else {
                n.setChecked(true);
                if (preSelectNode != null) {
                    preSelectNode.setChecked(false);
                }
                preSelectNode = n;
                notifyDataSetChanged();
                onTreeNodeClickListener.onClick(n, position);
            }
        }
    }

    /**
     * 点击可展开隐藏的层级
     */
    public void clickParentItem(Node n) {
            n.setExpand(!n.isExpand());
            mNodes = TreeHelper.filterVisibleNode(mAllNodes);
            notifyDataSetChanged();// 刷新视图

    }

    //点击最后可进行操作的层级
    public void clickLeafItem(Node n,int position) {
            n.setChecked(true);
            if (preSelectNode != null) {
                preSelectNode.setChecked(false);
            }
            preSelectNode = n;
            //notifyDataSetChanged();
            onTreeNodeClickListener.onClick(n, position);

    }

    @Override
    public int getCount() {
        return mNodes.size();
    }

    @Override
    public Object getItem(int position) {
        return mNodes.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final Node node = mNodes.get(position);

        convertView = getConvertView(node, position, convertView, parent);
        int screenWidth = ViewUtil.getScreenSize()[0];
        // 设置内边距
        convertView.setPadding(node.getLevel() * (screenWidth / 18), 0, 3, 0);


        return convertView;
    }

    public abstract View getConvertView(Node node, int position,
                                        View convertView, ViewGroup parent);

    /**
     * 更新
     *
     * @param isHide
     */
    public void updateView(boolean isHide) {
        for (Node node : mAllNodes) {
            node.setHideChecked(isHide);
        }

        this.notifyDataSetChanged();
    }

    /**
     * 控制 全选和非全选
     *
     * @param isCheckAll
     */
    public void setSelectAll(boolean isCheckAll) {
        checkedNodes.clear();

        for (Node node : mAllNodes) {
            if (!node.isHaveSelect()) {
                int roleLevel = node.getRoleLevel();
                if (isCheckAll && node.isLeaf() && (roleLevel == 3 || roleLevel == 7)) {
                    checkedNodes.add(node);
                }
                if (node.isRoot()) {
                    TreeHelper.setNodeChecked(node, isCheckAll);

                }
            }

        }

        onTreeNodeClickListener.onCheckChange(null, 0, checkedNodes);
        ParentTreeListViewAdapter.this.notifyDataSetChanged();
    }

    public void setNodeChecked(String id) {
        for (Node n : mAllNodes) {
            if (id.equals(n.getShowID())) {
                checkedNodes.add(n);
                TreeHelper.setNodeChecked(n, true);
                break;
            }

        }
        onTreeNodeClickListener.onCheckChange(null, 0, checkedNodes);
        ParentTreeListViewAdapter.this.notifyDataSetChanged();
    }


}
