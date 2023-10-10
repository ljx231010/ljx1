package com.lu.pojo;

import com.lu.mapper.ITreeNodeData;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SpeciesTreeNode<T extends ITreeNodeData> implements Serializable {
    private static final long serialVersionUID = 1L;
    private T nodeData;
    private SpeciesTreeNode<T> parent;//父节点对象
    private List<SpeciesTreeNode<T>> children = new ArrayList<SpeciesTreeNode<T>>();//子节点对象

    /**
     * @param childNode 子节点
     * @description 添加子节点
     */
    public void addChildNode(SpeciesTreeNode<T> childNode) {
        childNode.setParent(this);
        children.add(childNode);
    }

    /**
     * 移出所有子节点
     */
    public void clear() {
        children.clear();
    }

    /**
     * * @param nodeName : 节点名称
     * * @return : 如果找到，返回对应树节点，否则返回null
     * * @methodName : searchSubNode
     * * @description : 在当前节点及下级节点中查找指定节点名称的节点
     */
    public SpeciesTreeNode<T> searchSubNodeByName(String nodeName) {
        SpeciesTreeNode<T> node = null;

        //检查当前节点
        if (nodeData.getSpeciesName().equals(nodeName)) {
            node = this;
            return node;
        }

        //遍历子节点
        for (SpeciesTreeNode<T> item : children) {
            node = item.searchSubNodeByName(nodeName);
            if (node != null) {
                //如果节点非空，表示查找到了
                break;
            }
        }
        return node;
    }

    /**
     * @param nodeName : 节点名称
     * @return : 如果找到，返回对应树节点，否则返回null
     * @methodName : searchSuperNode
     * @description : 在当前节点及上级节点中查找指定节点名称的节点
     */
    public SpeciesTreeNode<T> searchSuperNodeByName(String nodeName) {
        SpeciesTreeNode<T> node = null;
        //检查当前节点
        if (nodeData.getSpeciesName().equals(nodeName)) {
            node = this;
            return node;
        }

        //查找父节点
        if (parent != null) {
            node = parent.searchSuperNodeByName(nodeName);
        }

        return node;
    }

    /**
     * @param speciesId : 物种id
     * @return : 如果找到，返回对应树节点，否则返回null
     * @methodName : searchSubNode
     * @description : 在当前节点及下级节点中查找指定节点名称的节点
     */
    public SpeciesTreeNode<T> searchSubNodeBySpeciesId(String speciesId) {
        SpeciesTreeNode<T> node = null;

        //检查当前节点
        if (nodeData.getSpeciesName().startsWith(speciesId) && nodeData.getSpeciesName().split("  ")[0].equals(speciesId)) {
            node = this;
            return node;
        }

        //遍历子节点
        for (SpeciesTreeNode<T> item : children) {
            node = item.searchSubNodeBySpeciesId(speciesId);
            if (node != null) {
                //如果节点非空，表示查找到了
                break;
            }
        }
        return node;
    }

    /**
     * @param speciesId : 物种id
     * @return : 如果找到，返回对应树节点，否则返回null
     * @methodName : searchSuperNode
     * @description : 在当前节点及上级节点中查找指定节点名称的节点
     */
    public SpeciesTreeNode<T> searchSuperNodeBySpeciesId(String speciesId) {
        SpeciesTreeNode<T> node = null;

        //检查当前节点
        if (nodeData.getSpeciesName().equals(speciesId)) {
            node = this;
            return node;
        }

        //查找父节点
        if (parent != null) {
            node = parent.searchSuperNodeBySpeciesId(speciesId);
        }

        return node;
    }

    //    public void searchParentOfTwoNodes(SpeciesTreeNode<T> node1, SpeciesTreeNode<T> node2) {
//        SpeciesTreeNode<T> temp;
//        while (node1 != null) {
//            ndoe1 = node1.getParent();
//        }
//    }
    /*
    获取两个节点最近的公共父节点
     */
    public SpeciesTreeNode<T> getLastCommonAncestor(SpeciesTreeNode<T> node1, SpeciesTreeNode<T> node2) {
        SpeciesTreeNode<T> temp;
        while (node1 != null) {
            node1 = node1.parent;
            temp = node2;
            while (temp != null) {
                if (node1 == temp.parent)
                    return node1;
                temp = temp.parent;
            }
        }
        return null;
    }

    /*
    计算一父子节点之间的距离
    使用层次遍历
     */

    /**
     * 计算父子节点之间的距离
     *
     * @param parentNode 父节点
     * @param childNdoe  子节点
     * @return 距离，没有路径返回-1
     */
    public int getDistanceBetweenNodes(SpeciesTreeNode<T> parentNode, SpeciesTreeNode<T> childNdoe) {
        int distance = -1;
        SpeciesTreeNode<T> temp;
//        SpeciesTreeNode<T> last = null;//记录每一层的最后一个节点，用以计算距离
        int last = 1;//这一层最后一一个节点在层次遍历中的顺序数，用以计算距离
        int count = 0;//计数
        Queue<SpeciesTreeNode<T>> queue = new LinkedList<SpeciesTreeNode<T>>();
        queue.offer(parentNode);
        while (!queue.isEmpty()) {
            //访问节点
            temp = queue.poll();
            count++;
            if (temp == childNdoe)
                return ++distance;
            //子节点入队
            for (SpeciesTreeNode<T> node : temp.getChildren()) {
                queue.offer(node);
            }
            //处理该层的最右节点
            if (count == last) {
                distance++;
//            last = temp.getChildren().get(temp.children.size() - 1);
                last = count + queue.size();
            }
        }
        return -1;
    }

    /*
    计算分类距离
     */
    public int calculateClassificationDistance(SpeciesTreeNode<T> node1, SpeciesTreeNode<T> node2) {
        SpeciesTreeNode<T> parentNode = getLastCommonAncestor(node1, node2);
        if (parentNode == null) {
            System.out.println("两个节点不存在父节点");
            return 99999;
        }
        int distance1 = getDistanceBetweenNodes(parentNode, node1);
        int distance2 = getDistanceBetweenNodes(parentNode, node2);
        return distance1 + distance2;
    }

    @Override
    public String toString() {
        return "SpeciesTreeNode{" +
                "nodeData=" + nodeData +
                '}';
    }

}