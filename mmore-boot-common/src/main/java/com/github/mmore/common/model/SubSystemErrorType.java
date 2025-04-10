package com.github.mmore.common.model;

/**
 * @author Dwyane Lee
 * @date 2025/3/3
 */
public class SubSystemErrorType implements ApiErrorType {

    private String code;

    private String mesg;

    public SubSystemErrorType() {
        this.code = SystemErrorType.SYSTEM_ERROR.getCode();
        this.mesg = SystemErrorType.SYSTEM_ERROR.getMesg();
    }

    public SubSystemErrorType(String code, String mesg) {
        this.code = code;
        this.mesg = mesg;
    }

    @Override
    public String getCode() {
        return this.code;
    }

    @Override
    public String getMesg() {
        return this.mesg;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setMesg(String mesg) {
        this.mesg = mesg;
    }
}
