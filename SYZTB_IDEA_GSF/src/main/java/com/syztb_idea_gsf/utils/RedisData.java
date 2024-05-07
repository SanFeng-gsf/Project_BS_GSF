package com.syztb_idea_gsf.utils;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class RedisData {

    private LocalDateTime expireTime;
    private Object data;
}
