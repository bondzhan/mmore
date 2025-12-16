mmore-boot-async-excel-api（异步导入导出 SPI）

定位
- 定义导入导出任务的持久化 SPI 接口与公共模型，供服务端实现，以实现任务落库、状态更新与分页查询。

接口
- `AsyncTaskPersistenceApi`
  - `/save`：保存任务（WAITING）
  - `/update`：更新任务（PROCESSING/SUCCESS/FAIL 等）
  - `/selectPage`：分页查询任务

模型
- `AsyncTaskDto`：任务信息（类型、状态、导入文件、结果文件、统计等）
- `AsyncTaskQueryDto`：查询条件
- `AsyncTaskStatusEnum`：任务状态枚举

使用
- 由业务侧实现 `AsyncTaskPersistenceApi`，供 service 端调用。

