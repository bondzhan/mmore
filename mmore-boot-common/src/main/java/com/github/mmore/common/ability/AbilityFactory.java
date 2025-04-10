package com.github.mmore.common.ability;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Dwyane Lee
 * @date 2025/3/5
 */
@Component
public class AbilityFactory {

    private final ConcurrentHashMap<Class<? extends BaseAbility>, Map<String, BaseAbility>> abilityMap = new ConcurrentHashMap<>();

    @Autowired
    public AbilityFactory(Map<String, BaseAbility> abilityBeans) {
        for (BaseAbility ability : abilityBeans.values()) {
            Class<? extends BaseAbility> strategyClass = (Class<? extends BaseAbility>) ability.getClass().getInterfaces()[0];
            abilityMap.computeIfAbsent(strategyClass, k -> new HashMap<>())
                    .put(ability.provider(), ability);
        }
    }

    public <T extends BaseAbility> T findAbility(Class<T> abilityType, String provider) {
        Map<String, BaseAbility> abilityBeans = abilityMap.get(abilityType);
        if (abilityBeans != null) {
            return (T) abilityBeans.get(provider);
        }
        return null;
    }

}
