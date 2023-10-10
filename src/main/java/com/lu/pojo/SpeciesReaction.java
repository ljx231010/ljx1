package com.lu.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Objects;

/*
反应
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SpeciesReaction {
    private String rId; //反应id
    private String rName;   //反应名字
    private String definition;  //反应定义      Diphosphate + H2O <=> 2 Orthophosphate
    private String equation;    //反应表达式
    private List<String> ecNumber;
    private List<Enzyme> ecNumber1;
    private String energy;
    private List<String> substrateId;
    private List<String> productId;
    private Boolean ifForeign;//标志当前反映是不是外源反应

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SpeciesReaction)) return false;
        SpeciesReaction that = (SpeciesReaction) o;
        return ifForeign == that.ifForeign && Objects.equals(rId, that.rId) && Objects.equals(rName, that.rName) && Objects.equals(definition, that.definition) && Objects.equals(equation, that.equation) && Objects.equals(ecNumber, that.ecNumber) && Objects.equals(ecNumber1, that.ecNumber1) && Objects.equals(energy, that.energy) && Objects.equals(substrateId, that.substrateId) && Objects.equals(productId, that.productId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(rId, rName, definition, equation, ecNumber, ecNumber1, energy, substrateId, productId);
    }
}

