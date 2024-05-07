package com.syztb_idea_gsf.controller;

import com.syztb_idea_gsf.dto.Result;
import com.syztb_idea_gsf.entity.SysUser;
import com.syztb_idea_gsf.service.ISysService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@Slf4j
@RestController
@RequestMapping("/sys")
public class SystemController {
    @Resource
    private ISysService iSysService;

    /**
     * 登入
     */
    @PostMapping("/login")
    public Result login(@RequestBody SysUser sysUser) {
        return iSysService.login(sysUser);
    }

    /**
     * 注册管理员 验证有没有 Admin
     */
    @PostMapping("/insert")
    public Result insert(@RequestBody SysUser sysUser) {
        return iSysService.insert(sysUser);
    }

    /**
     * 修改管理员密码
     */
    @GetMapping("/update")
    public Result update(@RequestParam String name, @RequestParam String password) {
        return iSysService.upDate(name, password);
    }

    /**
     * 查询个人信息
     */
    @GetMapping("/selectByName")
    public Result selectByName(@RequestParam String name) {
        return iSysService.selectByName(name);
    }

    /**
     * 获取所有管理员信息
     */
    @GetMapping("/selectAll")
    public Result selectAll() {
        return iSysService.selectAll();
    }

    /**
     * 删除管理员
     */
    @GetMapping("/deleteById")
    public Result deleteById(@RequestParam Long id) {
        return iSysService.deleteById(id);
    }

    /**
     * 更新管理员权限
     */
    @GetMapping("/updateAdmin")
    public Result updateAdmin(@RequestParam String name) {
        return iSysService.updateAdmin(name);
    }
}
