mmore-example-web（示例应用）

定位
- 演示 `mmore-boot-starter` + `mmore-boot-i18n` 的使用，包括错误码与业务字典的多语言效果。

运行
```bash
mvn -pl mmore-example-web spring-boot:run
# 或 IDE 运行 ExampleApplication
```

接口
- `GET /demo/arg-error`：抛出 `SystemException(020000)` → 返回本地化错误信息
- `GET /demo/biz-error`：抛出 `BizException(COMMON)` → 返回本地化错误信息
- `GET /demo/order`：返回包含 `status` 的对象，自动填充 `statusDesc`（多语言）

资源文件
- 错误码：`src/main/resources/error/messages_*.properties`
- 字典：`src/main/resources/dict/messages_*.properties`

验证
```bash
curl -H "Accept-Language: en"    http://localhost:8080/demo/order   # statusDesc=In progress
curl -H "Accept-Language: zh-CN" http://localhost:8080/demo/order   # statusDesc=进行中
```

