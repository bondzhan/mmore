package com.github.mmore.file.controller;

import com.github.mmore.file.OssFileClient;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Tag(name = "oss文件上传", description = "oss文件上传")
@RestController
@ConditionalOnProperty(prefix = "oss", name = "enabled", havingValue = "true", matchIfMissing = true)
public class OssController {

    @Resource
    private OssFileClient ossFileClient;

    @Operation(summary = "上传")
    @PostMapping("/upload")
    public String upload(@RequestParam("file")  MultipartFile file) throws IOException {
        return ossFileClient.uploadFile(file.getBytes(), file.getOriginalFilename());
    }
}
