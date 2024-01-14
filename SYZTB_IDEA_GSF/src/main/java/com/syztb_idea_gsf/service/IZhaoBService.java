package com.syztb_idea_gsf.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.syztb_idea_gsf.dto.Result;
import com.syztb_idea_gsf.entity.DTO;
import com.syztb_idea_gsf.entity.ZhaoB;

public interface IZhaoBService extends IService<ZhaoB> {

    Result selectByName(String name);

    Result selectDetail(DTO dto);

    Result selectAll(Integer current);

    Result insert(ZhaoB zhaoB);

    Result close(DTO dto);

    Result open(DTO dto);

    Result delete(DTO dto);

}
