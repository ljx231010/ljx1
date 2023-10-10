package com.lu.mapper;

import com.lu.pojo.Reaction;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ReactionMapper {
    //根据反应id获得reaction
    Reaction getReactionById(@Param("id") String id);

    /*
    由酶id获取所对应的反应对象列表
     */
    List<Reaction> getReactionsByEcNumber(@Param("ecNumber") String ecNumber);
    /*
    由酶id获取所对应的反应id列表
     */
    List<String> getRIdByEcNumber(@Param("ecNumber") String ecNumber);
    /*
    由反应id获取对应的自由能信息
     */
    String getEnergyByRId(String rid);

    /*
    统计数据库所有反应数量
     */
    int getReactionCount();
    /*
    由e-r表获取
     */
    List<Reaction> getReactionsByEcNumber1(@Param("ecNumber") String ecNumber);

    String getReactionNameById(@Param("id") String id);
}
