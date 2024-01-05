package com.syztb_idea_gsf.controller;

import com.syztb_idea_gsf.service.IZhaoBService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@Slf4j
@RestController
@RequestMapping("/zb")
public class ZhaoBController {

    @Resource
    private IZhaoBService iZhaoBService;


}
