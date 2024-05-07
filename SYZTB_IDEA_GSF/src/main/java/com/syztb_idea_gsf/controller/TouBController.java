package com.syztb_idea_gsf.controller;

import com.syztb_idea_gsf.dto.Result;
import com.syztb_idea_gsf.entity.TouB;
import com.syztb_idea_gsf.entity.DTO;
import com.syztb_idea_gsf.service.ITouBService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@Slf4j
@RestController
@RequestMapping("/tb")
public class TouBController {

    @Resource
    private ITouBService iTouBService;

    /**
     * 根据自己的公司查询所参与的所有投标信息
     * 注意 ： 是自己的公司 (投标公司)
     */
    @PostMapping("/selectByName")
    public Result selectByName(@RequestBody DTO dto){
        return iTouBService.selectByName(dto.getName());
    }

    /**
     * 根据公司名称、所投公司名称、项目名称查询投标信息详情 (三个一起才可以)
     */
    @PostMapping("/selectDetail")
    public Result selectDetail(@RequestBody DTO dto){
        return iTouBService.selectDetail(dto);
    }

    /**
     * 选择对应的项目进行投标 (新增投标信息)
     * 数据库更新会清除相关缓存 (注意刷新)
     */
    @PostMapping("/insert")
    public Result insert(@RequestBody TouB touB){
        return iTouBService.insert(touB);
    }

    /**
     * 根据公司名称、所投公司名称、项目名称删除投标信息
     * 数据库更新会清除相关缓存 (注意刷新)
     */
    @PostMapping("/delete")
    public Result delete(@RequestBody DTO dto){
        return iTouBService.delete(dto);
    }

    /**
     * 根据招标公司名称(所投)、项目名称查询所有参与投标的公司
     */
    @PostMapping("/selectByN")
    public Result selectByN(@RequestBody DTO dto) {
        return iTouBService.selectByN(dto);
    }

    /**
     * 根据 id 修改为成功投标
     */
    @GetMapping("/setSuccessById")
    public Result setSuccessById(@RequestParam Integer id) {
        return iTouBService.setSuccessById(id);
    }
}
