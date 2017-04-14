package org.repositoryminer.codemetric.indirect;

import org.bson.Document;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.repositoryminer.ast.AST;
import org.repositoryminer.ast.AbstractClassDeclaration;
import org.repositoryminer.parser.java.JavaParser;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

/**
 * Created by gustavoramos00 on 07/03/2017.
 */
public class CCTest {

    private CC cc = new CC();
    private static final String UTF8 = "UTF-8";
    private AST ast;

    @Before
    public void setUp() throws Exception {
        JavaParser parser = new JavaParser();
        parser.processSourceFolders(".");
        String filePath = "src/main/java/org/repositoryminer/parser/java/JavaParser.java";
        byte[] data = Files.readAllBytes(Paths.get(filePath));
        String source = new String(data, UTF8);
        ast = parser.generate(filePath, source, UTF8);
    }

    @Test
    public void calculateTest() throws Exception {
        List<AbstractClassDeclaration> types = ast.getDocument().getTypes();
        for (AbstractClassDeclaration type : types) {
            cc.calculate(type, ast);
            Document documentValue1 = new Document("metric", "CC").append("value", 1);
            int uniqueMethodInvocation = 67;
            Assert.assertEquals(uniqueMethodInvocation, cc.getResult().size());
            cc.getResult().forEach((key, document) -> Assert.assertEquals(documentValue1, document));
        }
    }
}