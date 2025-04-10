package com.github.mmore.db.properties;

import com.github.mmore.web.properties.AbstractProperties;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "mybatis-plus")
@Getter
@Setter
public class MmoreMybatisPlusProperties extends AbstractProperties {

    public static MmoreMybatisPlusProperties getConfig() {
        return getConfig(MmoreMybatisPlusProperties.class);
    }

    private GlobalConfig globalConfig = new GlobalConfig();
    private Pagination pagination = new Pagination();

    @Getter
    @Setter
    public static class GlobalConfig {
        private DbConfig dbConfig = new DbConfig();
    }

    @Getter
    @Setter
    public static class DbConfig {
        private String idType = "auto"; // ID生成策略，默认为 auto
        private SqlLog sqlLog = new SqlLog();
        private SqlSafe sqlSafe = new SqlSafe();
        private AutoFillField autoFillField = new AutoFillField();
    }

    @Getter
    @Setter
    public static class SqlLog {
        private boolean enabled = true; // 默认启用 SQL 日志
    }

    @Getter
    @Setter
    public static class SqlSafe {
        private boolean enabled = false; // 默认关闭 SQL 安全
    }

    @Getter
    @Setter
    public static class AutoFillField {
        private boolean enabled = true; // 默认开启自动填充字段
        private String createId = "create_id";
        private String createName = "create_by";
        private String createTime = "create_time";
        private String modifyId = "modify_id";
        private String modifyName = "modify_by";
        private String modifyTime = "modify_time";
        private String deleted = "deleted";
    }

    @Getter
    @Setter
    public static class Pagination {
        private boolean enabled = true; // 默认启用分页
        private String dbType = "mysql"; // 数据库类型，默认为 MySQL
    }
}
