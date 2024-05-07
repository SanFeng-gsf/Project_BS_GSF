package com.syztb_idea_gsf.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.syztb_idea_gsf.dto.Result;
import com.syztb_idea_gsf.entity.ZhaoBUn;

public interface IZhaoBUnService extends IService<ZhaoBUn> {
    Result selectAll(Integer current);

    Result updateCheck(Integer id, String pass);

    Result selectByProjectName(String projectName);

    boolean insertZhaoBUn(ZhaoBUn zhaoBUn);

}
