package com.lu.service;

import com.lu.pojo.Compound;
import com.lu.pojo.Path;
import com.lu.pojo.Reaction;
import com.lu.pojo.SpeciesNetwork1;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public interface MREService {
    /*
      路径上的反应还是要分内外源的
      考虑所有底物的竞争性
       */
    double calculate1(Path path, SpeciesNetwork1 speciesNetwork) throws IOException;

    /**
     * 计算某个转换前体C的反应的f(r)
     *
     * @param speciesNetwork 物种网络
     * @param rn             转化前体C的所有天然反应的集合
     * @param curReaction    当前反应
     * @param c              前体C
     * @return 竞争分数
     */
    double getfr(SpeciesNetwork1 speciesNetwork, List<Reaction> rn, Reaction curReaction, Compound c);

    List<Reaction> getRN(SpeciesNetwork1 speciesNetwork, String cId);

    Map<String, Double> dataStandard(Map<String, Double> map);
}
