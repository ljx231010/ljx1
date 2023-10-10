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
public class SpeciesNetwork1 {
    private String speciesId;
    private List<String> cIdList;
    private List<String> rIdList;
    //    private List<List<Species>> network;
    private int[][] network;
}
