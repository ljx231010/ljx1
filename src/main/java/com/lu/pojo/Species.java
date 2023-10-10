package com.lu.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/*
物种
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Species {
    private String speciesId;
    private String speciesName;
    private String taxId;
}
