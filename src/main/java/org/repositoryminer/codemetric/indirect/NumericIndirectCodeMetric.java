package org.repositoryminer.codemetric.indirect;

import org.bson.Document;
import org.repositoryminer.ast.AST;
import org.repositoryminer.ast.AbstractClassDeclaration;
import org.repositoryminer.model.MetricData;
import org.repositoryminer.utility.StringUtils;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Classe abstrata para métricas indiretas numéricas. Deve ser utilizada em métricas que possuem
 * como resultado um valor numérico para cada chave.
 *
 * Created by gustavoramos00 on 22/02/2017.
 */
public abstract class NumericIndirectCodeMetric implements IIndirectCodeMetric{

    /**
     * Map com método e sua métrica
     */
    private Map<String, MetricData> mapMetricByMethod = new HashMap<>();

    public Map<String, Document> getResult() {
        Map<String, Document> result = new HashMap<>();

        Map<String, Map<String, MetricData>> metricByClassMethod = getMetricByClass(mapMetricByMethod);

        metricByClassMethod.forEach((className, metricByMethod) -> {
            List<Document> docMethodMetric = metricByMethod.entrySet()
                    .stream()
                    .map(entry -> new Document("method", entry.getKey())
                            .append("value", entry.getValue().getValue())
                            .append("callers", entry.getValue().getCallers()))
                    .collect(Collectors.toList());
            Document classDocument = new Document("metric", getId().toString()).append("methods", docMethodMetric);
            result.put(className, classDocument);
        });
        return result;
    }

    private Map<String, Map<String, MetricData>> getMetricByClass(Map<String, MetricData> metricsByMethod) {
        Map<String, Map<String, MetricData>> mapClassMethodMetricData = new HashMap<>();
        metricsByMethod.forEach((methodName, value) -> {
            String className = StringUtils.extractClassOfMethod(methodName);
            Map<String, MetricData> mapMethods = mapClassMethodMetricData.getOrDefault(className, new HashMap<>());
            mapMethods.put(methodName, value);

            mapClassMethodMetricData.put(className, mapMethods);
        });
        return mapClassMethodMetricData;
    }

    @Override
    public void calculate(AbstractClassDeclaration type, AST ast) {
        Map<String, MetricData> resultMap = calculate(type);
        resultMap.forEach((key, value) -> {
            MetricData metricData = mapMetricByMethod.getOrDefault(key, new MetricData());
            metricData.incrementValue(value.getValue());
            metricData.addAllCallers(value.getCallers());
            mapMetricByMethod.put(key, metricData);
        });
    }

    protected abstract Map<String, MetricData> calculate(AbstractClassDeclaration type);

    public Map<String, MetricData> getMapMetricByMethod() {
        return mapMetricByMethod;
    }
}

