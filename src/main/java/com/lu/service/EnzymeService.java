package com.lu.service;

import com.lu.javabean.Url;
import com.lu.pojo.*;

import java.util.List;
import java.util.Map;

public interface EnzymeService {
    /*
   创建系统发育树
    */
    public SpeciesTreeNode<SpeciesNode> createSpeciesTree();

    /*
    由taxId获取对应kegg的物种id
     */
    public List<String> mapping(String taxId);

    /*
     获取一条路径中反应在某个物种中的外源反应列表
    */
    public List<Reaction> getForeignReaction(String path, String speciesId);

    public List<DistanceEnzyme> recom1(String ecNumber, String host);

    /*
    只按系统发育距离
     */
    public Map<String, List<DistanceEnzyme>> enzymeAboutDIS(String path, String host);

    List<DistanceEnzyme> enzymeAboutDISOneReaction(String reactionId, String host);

    List<DynamicsEnzyme> enzymeAboutKmOneReaction(String reactionId);

    /**
     * 为单个外援反应推荐外源酶
     *
     * @param reactionId 外源反应id
     * @param host       当前宿主
     * @return
     */
    public List<EnzymeResult> calculateBothOneReaction(String reactionId, String host,double disWeight,double KMWeight);

    /*
   由推荐的酶获取详细酶信息页面的网址
    */
    public List<Url> recomSpecificInfoOfEnzyme(String ecNumber, String organism);

    /*
    为路径酶结果添加结果信息的网址
     */
    void addUrlsAboutDistanceEnzyme(List<DistanceEnzyme> collect);

    /*
    为结果添加结果信息的网址
     */
    void addUrlsAboutEnzymeResult(List<EnzymeResult> collect);

    void addUrlsAboutDynamicsEnzyme(List<DynamicsEnzyme> list);

    /*
    获取path在speicesId中的反应对应的SpeciesReaction列表
     */
    List<SpeciesReaction> allReaction(String path, String speciesId);

    /*
   由物种id获取物种对象
    */
    Species getSpeciesById(String id);

    /*
        为要展示的DistanceEnzyme赋值来源物种的名字
         */
    void toNamesAboutDistanceEnzyme(List<DistanceEnzyme> resultList);

    /*
    为要展示的EnzymeResult赋值来源物种的名字
     */
    void toNamesAboutEnzymeResult(List<EnzymeResult> resultList);


}
