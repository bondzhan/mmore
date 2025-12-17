package com.github.mmore.common.model;

import lombok.Getter;

@Getter
public class SubSystemException extends SystemException {

    public SubSystemException(String code, String mesg) {
        super(new SubSystemErrorType(code, mesg));
    }

    public SubSystemException(SubSystemErrorType subSystemErrorType) {
        super(subSystemErrorType);
    }

}
