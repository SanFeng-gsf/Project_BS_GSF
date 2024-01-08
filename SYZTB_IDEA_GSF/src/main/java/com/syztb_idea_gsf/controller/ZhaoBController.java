package com.syztb_idea_gsf.controller;

import com.syztb_idea_gsf.dto.Result;
import com.syztb_idea_gsf.entity.ZhaoB;
import com.syztb_idea_gsf.entity.ZhaoBDTO;
import com.syztb_idea_gsf.service.IZhaoBService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@Slf4j
@RestController
@RequestMapping("/zb")
public class ZhaoBController {

    @Resource
    private IZhaoBService iZhaoBService;

    /**
     * 根据公司名称查询公司的招标信息
     * @return 该公司所发布的所有招标信息
     */
    @PostMapping ("/selectByName")
    public Result selectByName(@RequestBody ZhaoBDTO zhaoBDTO){
        return iZhaoBService.selectByName(zhaoBDTO.getName());
    }

    /**
     * 分页查询所有公司的招标信息
     * @return 所以的招标信息
     */
    @PostMapping("/selectAll")
    public Result selectAll(@RequestParam(value = "current", defaultValue = "1") Integer current){
        return iZhaoBService.selectAll(current);
    }

    /**
     * 发布招标信息 (新增招标信息)
     */
    @PostMapping("/insert")
    public Result insert(@RequestBody ZhaoB zhaoB){
        return iZhaoBService.insert(zhaoB);
    }

}
