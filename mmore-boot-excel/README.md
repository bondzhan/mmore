mmore-boot-excel（Excel 读写基础）

定位
- 基于 `fastexcel` 提供通用监听器 `BaseExcelListener<T>`，按行收集解析数据，供导入导出业务复用。

用法示例
```java
BaseExcelListener<MyRow> listener = new BaseExcelListener<>();
FastExcel.read(inputStream, MyRow.class, listener).sheet(0).doRead();
List<MyRow> rows = listener.getDataList();
```

扩展
- 与 `mmore-boot-async-excel` 结合，可实现大批量异步导入导出与结果回传。

