mmore-boot-file（OSS 文件能力）

定位
- 提供阿里云 OSS 客户端与简易上传/下载接口，支持字节与 URL 上传、生成签名下载地址。

自动装配
- `MmoreFileAutoConf`（`oss.enabled=true` 生效）
  - Bean：`OSS`、`OssFileClient`
  - 控制器：`OssController`（`/upload`）

配置项（application.yml）
```yaml
oss:
  enabled: true
  endpoint: https://oss-xxx.aliyuncs.com
  accessKeyId: xxx
  secretAccessKey: xxx
  bucket: your-bucket
  internetFileUrl: https://your-cdn-domain/
  maxSize: 10   # MB
```

使用示例
```java
@Resource OssFileClient oss;
String url = oss.uploadFile(bytes, "foo.png");
URL signed = oss.downFile(url);
```

注意
- `maxSize` 为 MB；`internetFileUrl` 建议为公网前缀，最终返回为该前缀 + objectKey。

