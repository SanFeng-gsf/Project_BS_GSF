package com.syztb_idea_gsf.controller;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.RandomUtil;
import com.syztb_idea_gsf.dto.Result;
import com.syztb_idea_gsf.entity.JuBao;
import com.syztb_idea_gsf.service.IFileService;
import com.syztb_idea_gsf.service.IJuBaoService;
import com.syztb_idea_gsf.service.IJuBaoWhyService;
import com.syztb_idea_gsf.utils.PathUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/jb")
public class JuBaoController {
    @Resource
    private IFileService iFileService;
    @Resource
    private IJuBaoService iJuBaoService;
    @Resource
    private IJuBaoWhyService iJuBaoWhyService;

    /**
     * 举报文件上传
     */
    @RequestMapping ("/file")
    public Result JuBaoFile(@RequestParam("file") MultipartFile file) throws IOException {
        String url = iFileService.upload(file);
        return Result.ok(url);
    }

    /**
     * 提交举报信息
     */
    @PostMapping ("/details")
    public Result details(@RequestBody JuBao juBao){
        ArrayList<String> type = juBao.getType();
        String why = RandomUtil.randomString(16);
        if(type.size()==4) {
            iJuBaoWhyService.setJuBaoWhy(why);
        } else {
            iJuBaoWhyService.setJuBaoWhy(type,why);
        }
        return iJuBaoService.setJuBao(juBao,why);
    }

    /**
     * 根据获取所有举报信息
     */
    @GetMapping("/getJb")
    public Result getJb(@RequestParam(value = "myName", required = false) String myName, @RequestParam(value = "projectName", required = false) String projectName) {
        List<JuBao> list;
        if (myName != null) {
            // 根据 公司名称 获取举报信息
            list = iJuBaoService.getByMyName(myName);
        } else if (projectName != null) {
            // 根据 项目名称 获取举报信息
            list = iJuBaoService.getByProjectName(projectName);
        } else {
            // 根据获取所有举报信息
            list = iJuBaoService.getAll();
        }
        List<ArrayList> listUrl = new ArrayList<>();
        ArrayList<String> url = new ArrayList<>();
        for (int i=0;i<list.size();i++) {
            url = iFileService.getUrl(list.get(i).getFileName());
            url.add(list.get(i).getName()); // 末尾添加 举报公司名称
            listUrl.add(i,url);
        }
        Map<String,List> map =new HashMap<>();
        map.put("JuBao",list);
        map.put("url",listUrl);
        return Result.ok("success",map);
    }

    /**
     * 根据被举报公司获取被举报信息 (name) 举报公司 为自己
     */
    @GetMapping("getByName")
    public Result getByName(@RequestParam String name) {
        return iJuBaoService.getByName(name);
    }
    /**
     * 获取举报文件
     */
    @GetMapping ("/getFile")
    public void getFile(@RequestParam String fileUrl, HttpServletResponse response) throws IOException {
        File file = new File(PathUtils.getClassLoadRootPath() + "/" + fileUrl);
        // 设置输出流格式
        ServletOutputStream outputStream = response.getOutputStream();
        response.setHeader("Access-Control-Expose-Headers","Content-Disposition");
        response.addHeader("Content-Disposition","attachment;filename=" + file.getName());
        response.setContentType("application/octet-stream");
        outputStream.write(FileUtil.readBytes(file));
        outputStream.flush();
        outputStream.close();
    }
}
