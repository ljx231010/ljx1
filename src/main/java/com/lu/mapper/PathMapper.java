package com.lu.mapper;

import com.lu.pojo.Path;
import com.lu.pojo.PathSpecies;

public interface PathMapper {
    public Path createCompletePath(String path);

    PathSpecies getPathSpeciesByPath(String path);
}
/*
构建自己路径所需

由String创建完整的path对象

化合物信息
    根据id获取化合物信息

反应信息
    根据id获取反应信息
酶信息
    根据id获取酶数据信息\
物种网络
    根据物种id构建物种网络（即边为化合物的图）
 */