package com.syztb_idea_gsf.controller;

import com.syztb_idea_gsf.service.ITouBService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@Slf4j
@RestController
@RequestMapping("/tb")
public class TouBController {

    @Resource
    private ITouBService iTouBService;


}
