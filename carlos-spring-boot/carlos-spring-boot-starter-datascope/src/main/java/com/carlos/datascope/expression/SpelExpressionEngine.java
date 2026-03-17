package com.carlos.datascope.expression;

import com.carlos.datascope.core.engine.ExecutionContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.expression.Expression;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

import java.util.Map;

/**
 * SpringEL 表达式引擎实现
 * <p>
 * 基于Spring Expression Language的强大表达式引擎
 *
 * @author Carlos
 * @version 2.0
 */
@Slf4j
public class SpelExpressionEngine implements ExpressionEngine {

    private final SpelExpressionParser parser;

    public SpelExpressionEngine() {
        this.parser = new SpelExpressionParser();
    }

    @Override
    public Object compile(String expression) {
        try {
            return parser.parseExpression(expression);
        } catch (Exception e) {
            log.error("Failed to compile SpEL expression: {}", expression, e);
            throw new IllegalArgumentException("Invalid SpEL expression: " + expression, e);
        }
    }

    @Override
    public Object execute(Object compiledExpression, ExecutionContext context) {
        if (!(compiledExpression instanceof Expression)) {
            throw new IllegalArgumentException("Invalid compiled expression type");
        }

        Expression expression = (Expression) compiledExpression;
        StandardEvaluationContext evalContext = createEvaluationContext(context);

        try {
            return expression.getValue(evalContext);
        } catch (Exception e) {
            log.error("Failed to execute SpEL expression: {}", expression.getExpressionString(), e);
            throw new RuntimeException("Expression execution failed", e);
        }
    }

    @Override
    public boolean evaluate(Object compiledExpression, ExecutionContext context) {
        Object result = execute(compiledExpression, context);
        return Boolean.TRUE.equals(result);
    }

    /**
     * 创建评估上下文
     *
     * @param context 执行上下文
     * @return StandardEvaluationContext
     */
    private StandardEvaluationContext createEvaluationContext(ExecutionContext context) {
        StandardEvaluationContext evalContext = new StandardEvaluationContext();

        // 设置变量
        if (context.getVariables() != null) {
            for (Map.Entry<String, Object> entry : context.getVariables().entrySet()) {
                evalContext.setVariable(entry.getKey(), entry.getValue());
            }
        }

        // 设置根对象
        evalContext.setRootObject(context);

        return evalContext;
    }
}
