package com.github.mmore.lock.util;

import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

/**
 * SpringEL表达式工具类
 */
public class ELUtil {

    private static final ExpressionParser parser = new SpelExpressionParser();

    private static final String COMBINATION = "+";
    private static final String COMBINATION_SPLIT = "\\+";

//    public static String parseExpression(String expression, ProceedingJoinPoint joinPoint) {
//
//        if (!isElExpression(expression)) {
//            return expression;
//        }
//        StandardEvaluationContext context = getContext(joinPoint);
//        return parser.parseExpression(expression).getValue(context, String.class);
//    }

    /**
     * 解析并执行SpringEL表达式
     * @param expression
     * @param joinPoint
     * @return
     */
    public static String parseExpression(String expression, ProceedingJoinPoint joinPoint) {

        StandardEvaluationContext context = getContext(joinPoint);
        if (expression.contains(COMBINATION)) {
            String[] keys = expression.split(COMBINATION_SPLIT);
            StringBuilder sb = new StringBuilder();
            for (String key : keys) {
                key = key.trim();
                if (!isElExpression(key)) {
                    sb.append(key);
                } else {
                    sb.append(parser.parseExpression(key).getValue(context, String.class));
                }
            }
            return sb.toString();
        } else {
            if (!isElExpression(expression)) {
                return expression;
            }
            return parser.parseExpression(expression).getValue(context, String.class);
        }
    }

    private static StandardEvaluationContext getContext(ProceedingJoinPoint joinPoint) {
        StandardEvaluationContext context = new StandardEvaluationContext();
        // 将方法参数绑定到上下文中
        Object[] args = joinPoint.getArgs();
        String[] parameterNames = ((MethodSignature) joinPoint.getSignature()).getParameterNames();
        if (parameterNames != null) {
            for (int i = 0; i < parameterNames.length; i++) {
                context.setVariable(parameterNames[i], args[i]);
            }
        }
        return context;
    }

    /**
     * 判断是否为SpringEL表达式
     * @param key
     * @return
     */
    private static boolean isElExpression(String key) {
        if (StringUtils.isBlank(key)) {
            return false;
        }
        // 正则匹配 EL 表达式格式：以 #{} 或 ${} 开头
        String elPattern = "^\\s*\\$?\\{.*?}\\s*$";
        return key.matches(elPattern);
    }
}