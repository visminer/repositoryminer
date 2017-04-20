package org.repositoryminer.codemetric.indirect;

import org.repositoryminer.ast.*;
import org.repositoryminer.codemetric.CodeMetricId;
import org.repositoryminer.model.MetricData;

import java.util.HashMap;
import java.util.Map;

/**
 * CM - Changing Methods
 *
 * Identifies the number of distinct methods that call the measured method [Mar02a]
 *
 * Created by gustavoramos00 on 20/02/2017.
 */
public class CM extends NumericIndirectCodeMetric {

    @Override
    protected Map<String, MetricData> calculate(AbstractClassDeclaration type) {
        Map<String, MetricData> resultMap = new HashMap<>();
        if (ClassArchetype.CLASS_OR_INTERFACE.equals(type.getArchetype())) {
            for (MethodDeclaration method : type.getMethods()) {
                method.getStatements().stream()
                        .filter(statement -> Statement.NodeType.METHOD_INVOCATION.equals(statement.getNodeType()))
                        .map(Statement::getExpression)
                        .distinct() //conta apenas uma chamada por método
                        .forEach(methodExpression -> {
                            MetricData metricData = resultMap.getOrDefault(methodExpression, new MetricData());
                            String callerMethodName = type.getName() + "." + method.getName();
                            boolean newCaller = metricData.addCaller(callerMethodName);
                            if (newCaller){
                                metricData.incrementValue();
                            }
                            resultMap.put(methodExpression, metricData);
                });
            }
        }
        return resultMap;
    }

    @Override
    public CodeMetricId getId() {
        return CodeMetricId.CM;
    }

}
