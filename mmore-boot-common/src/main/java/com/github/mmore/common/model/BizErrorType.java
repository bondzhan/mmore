package com.github.mmore.common.model;

import lombok.Getter;

@Getter
public enum BizErrorType implements ApiErrorType {

    /**
     * 未预料异常时均处理为该类型
     */
    COMMON("COMMON", "业务异常"),
    ACCOUNT("ACCOUNT", "账户异常"),
    TRADE("TRADE", "结算异常"),
    SKU("SKU", "SKU异常"),
    ;

    /**
     * 错误类型码
     */
    private final String code;
    /**
     * 错误类型描述信息
     */
    private final String mesg;


    /**
     * 构建函数
     *
     * @param code 错误代码
     * @param mesg 错误提示信息
     */
    BizErrorType(String code, String mesg) {
        this.code = code;
        this.mesg = mesg;
    }
}
