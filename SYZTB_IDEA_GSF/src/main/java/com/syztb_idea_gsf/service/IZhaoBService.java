package com.syztb_idea_gsf.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.syztb_idea_gsf.dto.Result;
import com.syztb_idea_gsf.entity.DTO;
import com.syztb_idea_gsf.entity.ZhaoB;

public interface IZhaoBService extends IService<ZhaoB> {

    Result selectByName(String name);

    Result selectDetail(DTO dto);

    Result selectAll(Integer current);

    Result selectSuccess(Integer current);

    Result selectUnSuccess(Integer current);

    Result delete(DTO dto);

    boolean isFinish(Integer id);

    Result updateClose(Integer id, int close);

    Result selectByProjectName(String projectName, Integer current);

    Result selectWeiGui(Integer current);
}
