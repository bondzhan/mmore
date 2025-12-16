package com.github.mmore.example.web.dto;

import com.github.mmore.i18n.annotation.I18nMapping;
import com.github.mmore.i18n.annotation.I18nMappings;
import lombok.Data;

@Data
@I18nMappings({
        @I18nMapping(from = "status", to = "statusDesc", prefix = "status")
})
public class OrderVO {
    private Long id;
    private Integer status; // 1 进行中 2 已完成 3 已关闭
    private String statusDesc;
}

