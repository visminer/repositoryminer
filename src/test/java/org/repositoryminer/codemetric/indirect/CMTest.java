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
 * Created by gustavoramos00 on 20/02/2017.
 */
public class CMTest {

    private CM cm = new CM();
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
            cm.calculate(type, ast);
            String methodValidateSourceFolder = "org.repositoryminer.parser.java.JavaParser.validateSourceFolder(java.io.File)";
            Document resultMethodValidateSourceFolder = new Document("metric", "CM").append("value", 2);
            Assert.assertEquals(resultMethodValidateSourceFolder, cm.getResult().get(methodValidateSourceFolder));

            String methodSetName = "org.repositoryminer.ast.Document.setName(java.lang.String)";
            Document resultMethodSetName = new Document("metric", "CM").append("value", 1);
            Assert.assertEquals(resultMethodSetName, cm.getResult().get(methodSetName));

            String methodListAdd = "java.util.List<java.lang.String>.add(java.lang.String)";
            Document resultMethodListAdd = new Document("metric", "CM").append("value", 3);
            Assert.assertEquals(resultMethodListAdd, cm.getResult().get(methodListAdd));
        }
    }
}