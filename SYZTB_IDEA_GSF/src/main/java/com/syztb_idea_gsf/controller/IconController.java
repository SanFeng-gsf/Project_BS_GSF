package com.syztb_idea_gsf.controller;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.resource.InputStreamResource;
import cn.hutool.json.JSONUtil;
import com.syztb_idea_gsf.dto.Result;
import com.syztb_idea_gsf.service.IUserIconService;
import com.syztb_idea_gsf.utils.PathUtils;
import com.syztb_idea_gsf.utils.SystemConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.*;

@Slf4j
@RestController
@RequestMapping("/userIcon")
public class IconController {
    @Resource
    private IUserIconService iUserIconService;

    /**
     * 保存用户头像
     */
    @RequestMapping("/icon")
    public Result setIcon(@RequestParam("file") MultipartFile file) throws IOException {
        String url = iUserIconService.saveIcon(file);
        return Result.ok(url);
    }


    /**
     * 获取头像
     */
    @GetMapping("/fileUrl")
    public void getIcon(@RequestParam String url, HttpServletResponse response) throws IOException {
        File file = new File(PathUtils.getClassLoadRootPath() + "/" + url);
        // 设置输出流格式
        System.out.println(PathUtils.getClassLoadRootPath() + "/" + url);
        ServletOutputStream outputStream = response.getOutputStream();
        response.addHeader("Content-Disposition","attachment;filename=" + file.getName());
        response.setContentType("application/octet-stream");
        outputStream.write(FileUtil.readBytes(file));
        outputStream.flush();
        outputStream.close();
    }
}
