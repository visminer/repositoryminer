package org.repositoryminer.utility;

import org.junit.Test;
import org.repositoryminer.codemetric.indirect.CC;

import static org.junit.Assert.*;

/**
 * Created by gustavoramos00 on 22/03/2017.
 */
public class StringUtilsTest {
    @Test
    public void extractClassOfMethod() throws Exception {
        String methodName = "org.repositoryminer.codemetric.indirect.NumericIndirectCodeMetricTest.classOfMethod(java.lang.String)";
        String className = StringUtils.extractClassOfMethod(methodName);
        assertEquals("org.repositoryminer.codemetric.indirect.NumericIndirectCodeMetricTest", className);
    }

}