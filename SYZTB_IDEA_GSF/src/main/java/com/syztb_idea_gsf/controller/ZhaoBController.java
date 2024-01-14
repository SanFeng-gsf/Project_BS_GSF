package com.syztb_idea_gsf.controller;

import com.syztb_idea_gsf.dto.Result;
import com.syztb_idea_gsf.entity.ZhaoB;
import com.syztb_idea_gsf.entity.DTO;
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
    public Result selectByName(@RequestBody DTO dto){
        return iZhaoBService.selectByName(dto.getName());
    }

    /**
     * 根据对应公司的项目名称查看招标信息详情 (含有已投标的投标信息)
     * 需要 公司名称、项目名称
     */
    @PostMapping("/selectDetail")
    public Result selectDetail(@RequestBody DTO dto){
        return iZhaoBService.selectDetail(dto);
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
     * 数据库更新会清除相关缓存 (注意刷新)
     */
    @PostMapping("/insert")
    public Result insert(@RequestBody ZhaoB zhaoB){
        return iZhaoBService.insert(zhaoB);
    }

    /**
     * 突发情况暂停招标信息 (禁止功能)  redis
     */
    @PostMapping("/close")
    public Result close(@RequestBody DTO dto){
        return iZhaoBService.close(dto);
    }

    /**
     * 启动暂停的招标信息  redis
     */
    @PostMapping("/open")
    public Result open(@RequestBody DTO dto){
        return iZhaoBService.open(dto);
    }

    /**
     * 删除招标信息  redis
     */
    @PostMapping("/delete")
    public Result delete(@RequestBody DTO dto){
        return iZhaoBService.delete(dto);
    }

}
