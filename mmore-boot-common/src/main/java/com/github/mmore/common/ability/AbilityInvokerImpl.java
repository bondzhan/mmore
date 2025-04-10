package com.github.mmore.common.ability;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

/**
 * @author Dwyane Lee
 * @date 2025/3/5
 */
@Component
public class AbilityInvokerImpl implements AbilityInvoker{

    private final AbilityFactory abilityFactory;

    public AbilityInvokerImpl(@Lazy AbilityFactory abilityFactory) {
        this.abilityFactory = abilityFactory;
    }

    @Override
    public <Ability extends BaseAbility> Ability findAbility(String provider, Class<Ability> abilityClass) {
        return abilityFactory.findAbility(abilityClass,provider);
    }
}
