package com.syztb_idea_gsf.controller;

import com.syztb_idea_gsf.dto.Result;
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
     * 根据项目名称查询招标信息
     */
    @GetMapping("/selectByProjectName")
    public Result selectByProjectName(@RequestParam(value = "current", defaultValue = "1") Integer current, String projectName) {
        return iZhaoBService.selectByProjectName(projectName, current);
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
    @GetMapping("/selectAll")
    public Result selectAll(@RequestParam(value = "current", defaultValue = "1") Integer current){
        return iZhaoBService.selectAll(current);
    }

    /**
     * 分页查询已完成的招标信息
     * @return 所以的招标信息
     */
    @GetMapping("/selectSuccess")
    public Result selectSuccess(@RequestParam(value = "current", defaultValue = "1") Integer current){
        return iZhaoBService.selectSuccess(current);
    }

    /**
     * 分页查询进行中的招标信息
     * @return 所以的招标信息
     */
    @GetMapping("/selectUnSuccess")
    public Result selectUnSuccess(@RequestParam(value = "current", defaultValue = "1") Integer current){
        return iZhaoBService.selectUnSuccess(current);
    }

    /**
     * 分页查询被禁止的招标信息 (违规)
     */
    @GetMapping("/selectWeiGui")
    public Result selectWeiGui(@RequestParam(value = "current", defaultValue = "1") Integer current){
        return iZhaoBService.selectWeiGui(current);
    }

    /**
     * 突发情况暂停招标信息 (禁止功能)  redis
     */
    @GetMapping("/updateClose")
    public Result updateClose(@RequestParam Integer id, int close){
        return iZhaoBService.updateClose(id,close);
    }

    /**
     * 删除招标信息  redis
     */
    @PostMapping("/delete")
    public Result delete(@RequestBody DTO dto){
        return iZhaoBService.delete(dto);
    }

    /**
     * 根据 id 判断招标信息是否结束
     */
    @GetMapping("/isFinish")
    public boolean isFinish(@RequestParam Integer id) {
        return iZhaoBService.isFinish(id);
    }
}
