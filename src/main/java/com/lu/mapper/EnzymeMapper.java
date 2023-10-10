package com.lu.mapper;

import com.lu.pojo.Enzyme;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface EnzymeMapper {
    /*
    由ecNumber获取酶对象
     */
    Enzyme getEnzymeByEcNumber(@Param("ecNumber") String ecNumber);

    /*
    由物种id获取包含该物种的所有酶对象
     */
    List<Enzyme> getEnzymesBySpeciesId(@Param("speciesId") String speciesId);

    String getAllOrganism(@Param("ecNumber") String ecNUmber);

    List<String> getEntryId(@Param("ecNumber") String ecNumber, @Param("taxId") String taxId);
}
