package com.github.mmore.file;

import com.aliyun.oss.HttpMethod;
import com.aliyun.oss.OSS;
import com.aliyun.oss.model.GeneratePresignedUrlRequest;
import com.aliyun.oss.model.PutObjectRequest;
import com.github.mmore.common.model.SystemErrorType;
import com.github.mmore.common.model.SystemException;
import com.github.mmore.file.config.OssProperties;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.util.Date;
import java.util.UUID;

/**
 * @Author Bond
 * @Date 2024/3/6
 * @Description TODO
 */
@Slf4j
public class OssFileClient {

    @Resource
    private OSS ossClient;

    @Resource
    private OssProperties ossProperties;

    public String uploadFile(byte[] imageBytes) {
        //生成文件名
        String objectKey = ossProperties.getBucket() + File.separator + UUID.randomUUID() + ".jpg";
        // 创建PutObjectRequest请求
        PutObjectRequest putObjectRequest = new PutObjectRequest(ossProperties.getBucket(), objectKey, new ByteArrayInputStream(imageBytes));
        // 上传文件到OSS
        ossClient.putObject(putObjectRequest);
        return ossProperties.getInternetFileUrl() + objectKey;
    }

    public String uploadFile(byte[] imageBytes, String fileName) {
        //生成文件名
        String objectKey = ossProperties.getBucket() + "/" + UUID.randomUUID() + "/" + fileName;
        if (ossProperties.getMaxSizeMb() < imageBytes.length) {
            throw new SystemException(SystemErrorType.SYSTEM_ERROR, "上传文件超过限制!");
        }
        // 创建PutObjectRequest请求
        PutObjectRequest putObjectRequest = new PutObjectRequest(ossProperties.getBucket(), objectKey, new ByteArrayInputStream(imageBytes));
        // 上传文件到OSS
        ossClient.putObject(putObjectRequest);
        return ossProperties.getInternetFileUrl() + objectKey;
    }

    public String uploadUrl(String url) {
        //生成文件名
        String objectKey = ossProperties.getBucket() + "/" + UUID.randomUUID() + ".jpg";
        // 下载文件到本地临时文件
        try {
            File tempFile = downloadFile(url);

            // 上传文件到OSS
            ossClient.putObject(ossProperties.getBucket(), objectKey, tempFile);

            return ossProperties.getInternetFileUrl() + objectKey;
        } catch (Exception e) {
            log.error("图片上传oss失败:{}", url, e);
        }
        return null;
    }

    public URL downFile(String fileName) {
        try{
            fileName = fileName.replace(ossProperties.getInternetFileUrl(),"");
            Date expiration = new Date(System.currentTimeMillis() + 3600 * 1000);
            // 生成签名URL。
            GeneratePresignedUrlRequest request = new GeneratePresignedUrlRequest(ossProperties.getBucket(), fileName, HttpMethod.GET);
            // 设置过期时间。
            request.setExpiration(expiration);
            // 通过HTTP GET请求生成签名URL。
            URL signedUrl = ossClient.generatePresignedUrl(request);
            // 打印签名URL。
            return signedUrl;
        }catch (Exception e){
            throw new SystemException(SystemErrorType.SYSTEM_ERROR, "下载文件异常");
        }
    }

    private static File downloadFile(String fileUrl) throws Exception {
        URL website = new URL(fileUrl);
        try (ReadableByteChannel rbc = Channels.newChannel(website.openStream());
             FileOutputStream fos = new FileOutputStream("tempFile")) {
            fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
        }
        return new File("tempFile");
    }
}
