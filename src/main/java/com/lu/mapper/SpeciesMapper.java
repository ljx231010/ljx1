package com.lu.mapper;

import com.lu.pojo.Species;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface SpeciesMapper {
    /*
    由物种id获取物种对象
     */
    Species getSpeciesById(@Param("id") String id);

    /*
    由taxid获取物种对象
     */
    List<Species> getSpeceisByTaxId(@Param("taxId") String taxId);

    List<String> getAllSpeciesId();

    List<Species> getAllSpecies();

    /**
     *     由物种id获取species表中的名字
     * @param id id
     * @return string
     */
    String getOrganismNameBySpeciesId(@Param("id") String id);

    String getNameByTaxId(@Param("taxId") String taxId);

    List<String> getSpeciesIdByTaxId(@Param("taxId") String taxId);
}
