package com.github.mmore.common.ability;

/**
 * @author Dwyane Lee
 * @date 2025/3/5
 */
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
