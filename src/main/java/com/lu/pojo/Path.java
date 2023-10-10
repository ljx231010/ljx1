package com.lu.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Path {
    private ArrayList<Reaction> reactionsOfPath = new ArrayList<>(); // 路径上反应对象列表
    private ArrayList<String> reactionNameOfPath = new ArrayList<>(); // 反应名称列表
    private ArrayList<Compound> compoundsOfPath = new ArrayList<>();// 路径上所有化合物列表(只包含主要化合物)

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        int i = 0;
        while (i < reactionsOfPath.size() || i <= compoundsOfPath.size()) {
            if (i < compoundsOfPath.size()) {
                sb.append(compoundsOfPath.get(i).getCId());
                sb.append("->");
            }
            if (i < reactionsOfPath.size()) {
                Reaction reaction = reactionsOfPath.get(i);
                sb.append(reaction.getRId());
                sb.append("(");
                for (String s : reaction.getEcNumber()) {
                    sb.append(s);
                    sb.append(" ");
                }
                sb.append(")");
                sb.append("->");
            }
            i++;
        }
        sb.delete(sb.length()-2,sb.length());
        return sb.toString();
    }
}
