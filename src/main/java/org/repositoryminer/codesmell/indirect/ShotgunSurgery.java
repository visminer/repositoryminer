package org.repositoryminer.codesmell.indirect;

import org.bson.Document;
import org.repositoryminer.ast.AST;
import org.repositoryminer.ast.AbstractClassDeclaration;
import org.repositoryminer.ast.ClassArchetype;
import org.repositoryminer.ast.ClassDeclaration;
import org.repositoryminer.codemetric.CodeMetricId;
import org.repositoryminer.codemetric.indirect.CC;
import org.repositoryminer.codemetric.indirect.CM;
import org.repositoryminer.codemetric.indirect.NumericIndirectCodeMetric;
import org.repositoryminer.codesmell.CodeSmellId;
import org.repositoryminer.model.MetricData;
import org.repositoryminer.utility.StringUtils;

import java.util.*;

/**
 * Shotgun Surgery: disharmony in which a method is excessively invoked
 * by many methods located in many classes.
 *
 * Created by gustavoramos00 on 22/02/2017.
 */
public class ShotgunSurgery implements  IIndirectCodeSmell {

    private int cmThreshold = 7; //Short Memory Cap
    private int ccThreshold = 10; //Many Classes

    private NumericIndirectCodeMetric cc = new CC();
    private NumericIndirectCodeMetric cm = new CM();
    private Set<String> shotgunMethods = new HashSet<>();

    @Override
    public void detect(AbstractClassDeclaration type, AST ast) {
        if (!type.getArchetype().equals(ClassArchetype.CLASS_OR_INTERFACE)) {
            return;
        }

        ClassDeclaration cls = (ClassDeclaration) type;

        if (cls.getSuperClass() == null) {
            return;
        }

        cc.calculate(type, ast);
        cm.calculate(type, ast);
    }

    private void detect(){
        cc.getMapMetricByMethod().forEach((key, ccMetricData) -> {
            MetricData cmMetricData = cm.getMapMetricByMethod().getOrDefault(key, new MetricData());
            if (ccMetricData.getValue() >= ccThreshold && cmMetricData.getValue() >= cmThreshold) {
                shotgunMethods.add(key);
            }
        });
    }

    @Override
    public CodeSmellId getId() {
        return CodeSmellId.SHOTGUN_SURGERY;
    }

    @Override
    public Document getThresholds() {
        Document doc = new Document();
        doc.append(CodeMetricId.CM.toString(), cmThreshold);
        doc.append(CodeMetricId.CC.toString(), ccThreshold);
        return new Document("codesmell", getId().toString()).append("thresholds", doc);
    }

    @Override
    public Map<String, Document> getResult() {
        detect();
        Map<String, Document> result = new HashMap<>();
        for (String methodName : shotgunMethods) {
            String className = StringUtils.extractClassOfMethod(methodName);
            Document classDoc = result.getOrDefault(className, new Document("codesmell", CodeSmellId.SHOTGUN_SURGERY.toString()));
            List<Document> methods = (List<Document>) classDoc.getOrDefault("methods", new ArrayList<>());
            Document method = new Document("method", methodName);
            MetricData ccMetricData = cc.getMapMetricByMethod().get(methodName);
            MetricData cmMetricData = cm.getMapMetricByMethod().get(methodName);
            Document ccMetric = new Document("value", ccMetricData.getValue()).append("callers", ccMetricData.getCallers());
            Document cmMetric = new Document("value", cmMetricData.getValue()).append("callers", cmMetricData.getCallers());
            method.append(cc.getId().toString(), ccMetric).append(cm.getId().toString(), cmMetric);
            methods.add(method);
            classDoc.put("methods", methods);
            result.put(className, classDoc);
        }
        return result;
    }
}
