package com.github.mmore.common.ability;

public interface AbilityInvoker {

    /**
     * 查找
     * @param provider
     * @param abilityClass
     * @param <Ability>
     * @return
     */
    <Ability extends BaseAbility> Ability findAbility(String provider, Class<Ability> abilityClass);

}
