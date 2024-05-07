package com.syztb_idea_gsf.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.syztb_idea_gsf.dto.Result;
import com.syztb_idea_gsf.entity.SysUser;

public interface ISysService extends IService<SysUser> {

    Result login(SysUser sysUser);

    Result insert(SysUser sysUser);

    Result selectByName(String name);

    Result upDate(String name, String password);

    Result selectAll();

    Result deleteById(Long id);

    Result updateAdmin(String name);
}
