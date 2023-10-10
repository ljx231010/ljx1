package com.lu.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;

/*
化合物
 */
@Data
@AllArgsConstructor
public class Compound {
    private String cId; //化合物的id
    private String cName;   //化合物的名字
}
