package com.lu.pojo;

import com.lu.javabean.Url;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DynamicsEnzyme {
    private String ecNumber;//酶id:如:4.3.1.23
    private String sourceOrganismName;   //来源物种名称
    private String sourceOrganismTaxId;   //来源分类id
    private double km;  //km值
    private double kmStandardValue;
    private List<Url> urls;
}
