package com.syztb_idea_gsf.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.syztb_idea_gsf.entity.JuBaoWhy;

import java.util.ArrayList;

public interface IJuBaoWhyService extends IService<JuBaoWhy> {
    void setJuBaoWhy(String why);
    void setJuBaoWhy(ArrayList<String> type, String why);

    ArrayList<String> getWhy(String why);
}
