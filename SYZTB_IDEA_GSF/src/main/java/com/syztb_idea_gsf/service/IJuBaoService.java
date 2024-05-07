package com.syztb_idea_gsf.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.syztb_idea_gsf.dto.Result;
import com.syztb_idea_gsf.entity.JuBao;
import com.syztb_idea_gsf.entity.JuBaoSql;

import java.util.List;


public interface IJuBaoService extends IService<JuBaoSql> {
    Result setJuBao(JuBao juBao, String why);

    List<JuBao> getByMyName(String myName);

    Result getByName(String name);

    List<JuBao> getAll();

    List<JuBao> getByProjectName(String projectName);
}
