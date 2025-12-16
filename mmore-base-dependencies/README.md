mmore-base-dependencies（依赖与插件版本管理 BOM）

定位
- 统一管理全工程依赖与构建插件版本，避免各模块重复声明与版本漂移。

包含内容
- 依赖版本：Spring Boot/Cloud、Spring Cloud Alibaba、MyBatis-Plus、JetCache、Knife4j、Jackson、Hutool、Guava、PowerJob、Sa-Token、CosId、fastexcel 等。
- 构建插件：spring-boot-maven-plugin、dockerfile-maven-plugin、git-commit-id-plugin、maven-surefire-plugin 等。

如何使用
- 在子模块 POM 的 `<dependencyManagement>` 导入：
  ```xml
  <dependencyManagement>
    <dependencies>
      <dependency>
        <groupId>com.github</groupId>
        <artifactId>mmore-base-dependencies</artifactId>
        <version>${revision}</version>
        <type>pom</type>
        <scope>import</scope>
      </dependency>
    </dependencies>
  </dependencyManagement>
  ```
- 之后直接使用受管版本的依赖，无需显式写 `<version>`。

注意
- 仅做版本“对齐”，不做依赖的传递引入；使用方仍需在 `<dependencies>` 声明实际需要的依赖。

