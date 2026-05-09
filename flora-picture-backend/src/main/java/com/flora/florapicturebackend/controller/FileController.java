package com.flora.florapicturebackend.controller;

import com.flora.florapicturebackend.config.CorsConfig;
import com.flora.florapicturebackend.manager.CosManager;
import com.qcloud.cos.model.COSObject;
import com.qcloud.cos.model.COSObjectInputStream;
import com.qcloud.cos.utils.IOUtils;

import lombok.extern.slf4j.Slf4j;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.flora.florapicturebackend.annotation.AuthCheck;
import com.flora.florapicturebackend.common.BaseResponse;
import com.flora.florapicturebackend.common.ResultUtils;
import com.flora.florapicturebackend.constant.UserConstant;
import com.flora.florapicturebackend.exception.BusinessException;
import com.flora.florapicturebackend.exception.ErrorCode;

import java.io.File;
import java.io.IOException;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Slf4j
@RestController
@RequestMapping("/file")
public class FileController {

    @Resource
    private CosManager cosManager;

    /**
     * 测试上传文件
     * 
     */
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    @PostMapping("/test/upload")
    public BaseResponse<String> testUploadFile(@RequestParam("file") MultipartFile multipartFile) {
        String fileName = multipartFile.getOriginalFilename();
        String filepath = String.format("test/%s", fileName);
        File file = null;
        try {
            // 上传文件到临时目录
            file = File.createTempFile(filepath, null);            
            cosManager.putObject(filepath, file);
            return ResultUtils.success(filepath);
        } catch (Exception e) {
            log.error("上传文件失败, filepath: {}", filepath, e);
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "上传失败");
        }finally{
            if(file != null){
                //删除临时文件
                boolean delete = file.delete();
                if(!delete){
                    log.error("删除临时文件失败, filepath: {}", filepath);
                }
            }
        }
    }

    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    @GetMapping("/test/download")
    public void testDownloadFile(String filepath, HttpServletResponse response) {
        COSObjectInputStream cosObjectInputStream = null;
        try {
            COSObject cosObject = cosManager.getObject(filepath);
            cosObjectInputStream = cosObject.getObjectContent();
            byte[] bytes = IOUtils.toByteArray(cosObjectInputStream);
            //设置响应头
            response.setContentType("application/octet-stream;charset=utf-8");
            response.setHeader("Content-Disposition", "attachment; filename=" + filepath);
            //写入响应
            response.getOutputStream().write(bytes);
            response.getOutputStream().flush();
        } catch (Exception e) {
            log.error("下载文件失败, filepath: {}", filepath, e);
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "下载失败");
        }finally{
            if(cosObjectInputStream != null){
                try {
                    cosObjectInputStream.close();
                } catch (IOException e) {
                    log.error("关闭文件流失败, filepath: {}", filepath, e);
                }
            }
        }
    }
    
}
