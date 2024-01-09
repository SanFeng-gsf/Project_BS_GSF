package com.syztb_idea_gsf.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.syztb_idea_gsf.dto.Result;
import com.syztb_idea_gsf.entity.ZhaoB;

public interface IZhaoBService extends IService<ZhaoB> {

    Result selectByName(String name);

    Result selectByProjectName(String projectName);

    Result selectAll(Integer current);

    Result insert(ZhaoB zhaoB);

}
