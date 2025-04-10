package com.github.mmore.async.model;

public interface TaskCode {

    Byte IMPORT = 0;

    Byte EXPORT = 1;

    String getCode();

    Byte getTaskType();

    String getMessage();
}
