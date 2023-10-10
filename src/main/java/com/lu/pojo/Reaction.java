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
public class Reaction {
    private String rId; //反应id
    private String rName;   //反应名字
    private String definition;  //反应定义      Diphosphate + H2O <=> 2 Orthophosphate
    private String equation;    //反应表达式
    //    private String ecNumber;
    private List<String> ecNumber;
    private List<Enzyme> ecNumber1;
    private String energy;
    private List<String> substrateId;
    //    private String substrateId;
    private List<String> productId;
//    private int ifForeign;//标志当前反映是不是外源反应
//    private String productId;

//    public Reaction(String rId, String rName, String definition, String equation, List<String> ecNumber, String energy, List<String> substrateId, List<String> productId) {
//        this.rId = rId;
//        this.rName = rName;
//        this.definition = definition;
//        this.equation = equation;
//        this.ecNumber = ecNumber;
//        this.energy = energy;
//        this.substrateId = substrateId;
//        this.productId = productId;
//    }

//    public List<Enzyme> getEcNumber1() {
//        return ecNumber1;
//    }
//
//    public void setEcNumber1(List<Enzyme> ecNumber1) {
//        this.ecNumber1 = ecNumber1;
//    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Reaction)) return false;
        Reaction reaction = (Reaction) o;
        return Objects.equals(rId, reaction.rId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(rId, rName, definition, equation, ecNumber, ecNumber1, energy, substrateId, productId);
    }
}

