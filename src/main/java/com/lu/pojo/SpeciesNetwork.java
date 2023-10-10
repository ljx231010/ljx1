package com.lu.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
/*
R-E
 */
public class SpeciesNetwork {
    private String speciesId;
    private List<Compound> compoundsList;
    private List<Reaction> reactionList;
    //    private List<List<Species>> network;
    private int[][] network;
}
