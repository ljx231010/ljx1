package com.lu.mapper;

import com.lu.pojo.Compound;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface SpeciesNetworkMapper {
    /*
     由物种id构建该物种的代谢网络
     先由id获取所有包含该物种的反应，然后由反应构建网络
     */
    List<List<Integer>> get1(@Param("speciesId") String speciesId);

    List<Compound> createCompoundsList();

    List<String> getCIdList();
}
