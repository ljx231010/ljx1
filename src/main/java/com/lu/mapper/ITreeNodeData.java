package com.lu.mapper;
/**
 * @className	: ITreeNodeData
 * @description	: 树节点数据接口类
 *
 */
public interface ITreeNodeData extends Cloneable{
    //=============节点基本属性访问接口==============================
    //获取节点ID
    int getNodeId();

    //获取物种名称
    String getSpeciesName();
    //  获取物种id
//    String getSpeciesId();
    //获取父节点ID
    int getParentNodeId();

    //=============Cloneable类接口===================================
    //克隆
    public Object clone();
}