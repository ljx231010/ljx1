package com.lu.service;

import com.lu.pojo.KeyValue;
import com.lu.pojo.Message;
import com.lu.pojo.Species;
import com.lu.pojo.SpeciesNetwork1;
import org.apache.ibatis.annotations.Param;

import java.io.IOException;
import java.util.List;

public interface SpeciesNetworkService {
    List<KeyValue> calculateScoreOfSpecies(String path,Double inOut,Double subAndProduct,Double compete) throws IOException;

    SpeciesNetwork1 getArrayFromTxt(String speciesId) throws IOException;

    List<Species> getAllSpecies();

    /**
     * 根据物种id获取物种对象
     * @param id 物种
     * @return 物种对象Species
     */
    Species getSpeciesById(@Param("id") String id);

    List<Message> getDeadEndMetaboliteInOneSpecies(String path, String speciesId) throws IOException;
}
