package com.github.mmore.db.model;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class BasePo extends Model<BasePo> implements Serializable {

    @TableField(value = "create_by", fill = FieldFill.INSERT)
    private Long createBy;

    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(value = "modify_by",fill = FieldFill.INSERT_UPDATE)
    private Long modifyBy;

    @TableField(value = "modify_time",fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime modifyTime;
}