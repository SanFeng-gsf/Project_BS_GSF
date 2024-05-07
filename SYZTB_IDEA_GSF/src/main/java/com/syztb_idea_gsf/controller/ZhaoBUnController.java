package com.syztb_idea_gsf.controller;

import com.syztb_idea_gsf.dto.Result;
import com.syztb_idea_gsf.entity.ZhaoBUn;
import com.syztb_idea_gsf.service.IZhaoBUnService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@Slf4j
@RestController
@RequestMapping("/zbUn")
public class ZhaoBUnController {
    @Resource
    private IZhaoBUnService iZhaoBUnService;

    /**
     * 分页查询招标发布的数据进行审核
     */
    @GetMapping("/selectAll")
    public Result selectAll(@RequestParam(value = "current", defaultValue = "1") Integer current) {
        return iZhaoBUnService.selectAll(current);
    }

    /**
     * 根据项目名称查询招标发布信息
     */
    @GetMapping("/selectByProjectName")
    public Result selectByProjectName(@RequestParam String projectName) {
        return iZhaoBUnService.selectByProjectName(projectName);
    }

    /**
     * 审核招标发布信息
     */
    @PostMapping("/insert")
    public Result insert(@RequestBody ZhaoBUn zhaoBUn){
        boolean result = iZhaoBUnService.insertZhaoBUn(zhaoBUn);
        if (result) {
            return Result.ok("success");
        }
        return Result.fail("fail");
    }

    /**
     * 审核招标发布信息
     */
    @GetMapping("/updateCheck")
    public Result updateCheck(@RequestParam Integer id, String pass) {
        return iZhaoBUnService.updateCheck(id, pass);
    }
}
