package com.lu.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Message {
    String compoundId;
    String reactionId;
    String content;//信息

    @Override
    public String toString() {
        return compoundId + ";" + reactionId + ";" + content;
    }
}
