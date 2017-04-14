package org.repositoryminer.codemetric.indirect;

import org.repositoryminer.ast.*;
import org.repositoryminer.codemetric.CodeMetricId;
import org.repositoryminer.model.MetricData;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * CC - Changing Class
 *
 * The number of classes in which the methods that call the measured method are defined in [Mar02a]
 *
 * Created by gustavoramos00 on 22/02/2017.
 */
public class CC extends NumericIndirectCodeMetric {

    @Override
    protected Map<String, MetricData> calculate(AbstractClassDeclaration type) {
        Map<String, MetricData> resultMap = new HashMap<>();
        if (ClassArchetype.CLASS_OR_INTERFACE.equals(type.getArchetype())) {
            type.getMethods().stream()
                    .flatMap(method -> method.getStatements().stream())
                    .filter(statement -> Statement.NodeType.METHOD_INVOCATION.equals(statement.getNodeType()))
                    .map(Statement::getExpression)
                    .distinct()
                    .forEach(methodExpression -> {
                        MetricData metricData = resultMap.getOrDefault(methodExpression, new MetricData());
                        boolean newCaller = metricData.addCaller(type.getName());
                        if (newCaller){
                            metricData.incrementValue();
                        }
                        resultMap.put(methodExpression, metricData);
                    });
        }
        return resultMap;
    }

    @Override
    public CodeMetricId getId() {
        return CodeMetricId.CC;
    }
}
