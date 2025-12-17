package com.github.mmore.common.ability;

import cn.hutool.core.util.ObjectUtil;
import com.github.mmore.common.model.SystemErrorType;
import com.github.mmore.common.util.AssertUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.annotation.Lazy;

public abstract class BaseAbilityAdapter {

    private final AbilityInvoker abilityInvoker;

    public BaseAbilityAdapter(@Lazy AbilityInvoker abilityInvoker) {
        this.abilityInvoker = abilityInvoker;
    }

    protected <R extends AbilityRequest, Ability extends BaseAbility> Ability findAbility(R request, Class<Ability> abilityClass) {
        AssertUtil.that(ObjectUtil.isNotNull(request), SystemErrorType.ARGUMENT_NOT_VALID, "request null");
        AssertUtil.that(ObjectUtil.isNotNull(abilityClass), SystemErrorType.ARGUMENT_NOT_VALID, "abilityClass null");
        String provider = request.getProvider();
        return findAbility(provider, abilityClass);
    }

    protected <Ability extends BaseAbility> Ability findAbility(String provider, Class<Ability> abilityClass) {
        AssertUtil.that(StringUtils.isNotBlank(provider), SystemErrorType.ARGUMENT_NOT_VALID, "provider null");
        AssertUtil.that(ObjectUtil.isNotNull(abilityClass), SystemErrorType.ARGUMENT_NOT_VALID, "abilityClass null");
        Ability ability = abilityInvoker.findAbility(provider, abilityClass);
        AssertUtil.that(ObjectUtil.isNotNull(abilityClass), SystemErrorType.RESOURCE_NOT_FOUND, "abilityClass null");
        return ability;
    }

}
