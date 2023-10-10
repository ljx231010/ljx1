package com.lu.pojo;

import com.lu.javabean.Url;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EnzymeResult {
    private String ecNumber;
    private String sourceOrganismId;//来源物种id
    private String sourceOrganismName;//来源物种id名字
    private String sourceOrganismTaxId;//来源物种taxid
    private String sourceOrganismTaxName;//来源物种taxid的名字
    private double distanceStandardValue;//表转化距离值
    private double kmStandardValue;//标准化km值
    private double score;//最终综合分数
    private List<Url> urls;
}
