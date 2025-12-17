package com.github.mmore.common.ability;

import lombok.Data;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

import java.io.Serializable;

@Data
public class BaseAbilityRequest implements Serializable, AbilityRequest {

    private static final long serialVersionUID = 1L;

    /**
     * 能力提供方
     */
    private String provider;


    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this);
    }
}
