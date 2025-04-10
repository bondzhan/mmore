### 异步导入导出
### 1. **创建任务枚举**
项目youforest-boot-async-excel-service
```java
model.com.github.mmore.async.TaskCodeEnum
// IMPORT为导入，EXPORT为导出
    QUOTE_IMPORT("quote_import", IMPORT, "导入报价单"),
    QUOTE_EXPORT("quote_export", EXPORT,"导出报价单"),
```

### 2. **引入依赖包**
```xml
<dependency>
	<groupId>com.yunshu</groupId>
	<artifactId>youforest-boot-async-excel-service</artifactId>
	<version>1.0-SNAPSHOT</version>
</dependency>
```


### 3.  **创建excel映射实体**
1. 导入继承AsyncTaskParseImportDto
2. 导出继承AsyncTaskParseExportDto
```java
@Data
public class BatchInfoExportVO extends AsyncTaskParseExportDto {

    /**
     * 仓库名称
     */
    @ExcelProperty(value = "仓库名称")
    @AsyncTaskColumn(index = 0, width = 30) // 设置列宽最大不能超过255
    private String warehouseName;
}
```
### 4.  **方法实现**

```java
// 导入，异常信息设置errorMsg
// 导出，导出数据设置到asyncTaskDtos，asyncTaskData.setAsyncTaskDtos(datas);
    @AsyncListener(taskCode = TaskCodeEnum.SELLER_BATCH_LOCK, dataType = BatchLockVO.class)
    public void batchLock(AsyncTaskData<BatchLockVO> asyncTaskData) {
		
	}
```
### 5.  **前端接口**
```java
controller.com.github.mmore.async.AsyncTaskController
// 导入
controller.com.github.mmore.async.AsyncTaskController#asyncImport
// 导出
controller.com.github.mmore.async.AsyncTaskController#exportTask
// 导入导出结果和下载结果
controller.com.github.mmore.async.AsyncTaskController#selectPage
```
