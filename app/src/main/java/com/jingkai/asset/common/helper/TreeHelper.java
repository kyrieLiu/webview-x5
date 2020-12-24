package com.jingkai.asset.common.helper;


import com.jingkai.asset.R;
import com.jingkai.asset.common.entity.Node;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by liuyin on 2018/9/7 8:56
 *
 * @Describe 初始化节点  根据所有节点获取可见节点
 */
public class TreeHelper {
    public static List<Node> filterVisibleNode(List<Node> allNodes) {
        List<Node> visibleNodes = new ArrayList<Node>();
        for (Node node : allNodes) {
            // 如果为根节点，或者上层目录为展开状态
            if (node.isRoot() || node.isParentExpand()) {
                setNodeProperty(node);
                visibleNodes.add(node);
            }
        }
        return visibleNodes;
    }

    /**
     * 获取排序的所有节点
     *
     * @param datas
     * @param defaultExpandLevel
     * @return
     */
    public static <T> List<Node> getSortedNodes(List<T> datas,
                                                int defaultExpandLevel)
            throws IllegalAccessException, IllegalArgumentException {
        List<Node> sortedNodes = new ArrayList<Node>();
        // 将用户数据转化为List<Node>
        List<Node> nodes = convertData2Nodes(datas);
        // 拿到根节点
        List<Node> rootNodes = getRootNodes(nodes);
        // 排序以及设置Node间关系
        for (Node node : rootNodes) {
            addNode(sortedNodes, node, defaultExpandLevel, 1);
        }
        return sortedNodes;
    }

    /**
     * 把一个节点上的所有的内容都挂上去
     */
    private static void addNode(List<Node> nodes, Node node,
                                int defaultExpandLeval, int currentLevel) {

        nodes.add(node);
        if (defaultExpandLeval >= currentLevel) {
            node.setExpand(true);
        }

        if (node.isLeaf())
            return;
        for (int i = 0; i < node.getChildrenNodes().size(); i++) {
            addNode(nodes, node.getChildrenNodes().get(i), defaultExpandLeval,
                    currentLevel + 1);
        }
    }

    /**
     * 获取所有的根节点
     *
     * @param nodes
     * @return
     */
    public static List<Node> getRootNodes(List<Node> nodes) {
        List<Node> rootNodes = new ArrayList<Node>();
        for (Node node : nodes) {
            if (node.isRoot()) {
                rootNodes.add(node);
            }
        }

        return rootNodes;
    }

    /**
     * 将泛型datas转换为node
     *
     * @param datas
     */
    public static <T> List<Node> convertData2Nodes(List<T> datas)
            throws IllegalAccessException, IllegalArgumentException {
        List<Node> nodes = (List<Node>) datas;

        /**
         * 比较nodes中的所有节点，分别添加children和parent
         */
        for (int i = 0; i < nodes.size(); i++) {
            Node n = nodes.get(i);
            for (int j = i + 1; j < nodes.size(); j++) {
                Node m = nodes.get(j);
                if (n.getShowID() .equals( m.getpId())) {
                    n.getChildrenNodes().add(m);
                    m.setParent(n);
                } else if (n.getpId() .equals( m.getShowID())) {
                    n.setParent(m);
                    m.getChildrenNodes().add(n);
                }
            }
        }

        for (Node n : nodes) {
            setNodeProperty(n);
        }
        return nodes;
    }

    /**
     * 设置打开，关闭icon
     * 设置总数
     *
     * @param node
     */
    public static void setNodeProperty(Node node) {
        //最后一层不加上下箭头
        if (!node.isLeaf()&&node.getRoleLevel()!=2 && node.isExpand()) {
            node.setIcon(R.mipmap.icohide);
        } else if (!node.isLeaf()&&node.getRoleLevel()!=2&& !node.isExpand()) {
            node.setIcon(R.mipmap.icoshow);
        } else {
            node.setIcon(-1);
        }
//        allCount = 0;
//        getChildAllCount(node);
//        node.setAllCount(allCount);

    }

    private static int allCount = 0;

    /**
     * 递归获取总的条目数
     */
    private static void getChildAllCount(Node node) {

        if (!node.isLeaf()) {
            for (Node n : node.getChildrenNodes()) {
                // 所有子节点设置是否选择
                getChildAllCount(n);
            }
        } else {
            int roleLevel=node.getRoleLevel();
            if (roleLevel==2){//是人员
                allCount++;
            }

        }
    }

    /**
     * 递归获取可选的条目数
     */
    private static void getChildCanSelectCount(Node node) {

        if (!node.isLeaf()) {
            for (Node n : node.getChildrenNodes()) {
                // 所有子节点设置是否选择
                getChildCanSelectCount(n);
            }
        } else {
            int roleLevel=node.getRoleLevel();
            if (!node.isHaveSelect()&&(roleLevel==3||roleLevel==7)){//可选择的人员
                allCount++;
            }

        }
    }


    public static void setNodeChecked(Node node, boolean isChecked) {
        // 自己设置是否选择
        node.setChecked(isChecked);
        /**
         * 非叶子节点,子节点处理
         */
        childSelectCount = 0;
        setChildrenNodeChecked(node, isChecked);
        node.setSelectCount(childSelectCount);
        /** 父节点处理 */
        setParentNodeChecked(node);

    }

    private static int childSelectCount = 0;

    /**
     * 非叶子节点,子节点处理
     */

    private static void setChildrenNodeChecked(Node node, boolean isChecked) {
        node.setChecked(isChecked);
        if (!node.isLeaf()) {

            for (int i = 0; i < node.getChildrenNodes().size(); i++) {
                Node n = node.getChildrenNodes().get(i);
//                // 所有子节点设置是否选择

                setChildrenNodeChecked(n, isChecked);
            }
            allCount=0;
            if (isChecked){
                getChildCanSelectCount(node);
            }
            node.setSelectCount(allCount);
        } else {
            int roleLevel=node.getRoleLevel();
            if (!node.isHaveSelect()&&(roleLevel==3||roleLevel==7)) {
                if (node.isChecked()) {
                    childSelectCount++;
                    node.setSelectCount(1);
                }
            }
        }
    }


    /**
     * 设置父节点选择
     *
     * @param node
     */
    private static void setParentNodeChecked(Node node) {

        /** 非根节点 */
        if (!node.isRoot()) {
            Node rootNode = node.getParent();
            int selectCount = 0;

            boolean isAllChecked = true;
            for (Node n : rootNode.getChildrenNodes()) {
                if (!n.isChecked()) {
                    isAllChecked = false;
                    //如果当前节点是非选中状态,但是它的子条目有选中的数据
                    if (n.getSelectCount()>0){
                        selectCount += n.getSelectCount();
                    }
                } else {
                    selectCount += n.getSelectCount();
                }
            }
            rootNode.setSelectCount(selectCount);
            //rootNode.setAllCount(allCount);
            if (isAllChecked) {
                rootNode.setChecked(true);
            } else {
                rootNode.setChecked(false);
            }
            setParentNodeChecked(rootNode);
        }
    }


}
