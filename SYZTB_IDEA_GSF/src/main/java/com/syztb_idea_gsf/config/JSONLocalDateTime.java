package com.syztb_idea_gsf.config;

import cn.hutool.json.JSONConfig;

/**
 *  将日期序列化时保持格式不变
 */
public class JSONLocalDateTime {

    public JSONConfig getJsonConfig() {
        JSONConfig jsonConfig = new JSONConfig();
        jsonConfig.setDateFormat("yyyy-MM-dd HH:mm:ss");
        return jsonConfig;
    }
}
