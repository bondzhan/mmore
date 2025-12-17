package com.github.mmore.db.model;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;


@Data
@NoArgsConstructor
public class BaseTenantPo extends BasePo implements Serializable {

    @TableField(value = "tenant_id", fill = FieldFill.INSERT)
    private Long tenantId;

}
