package br.edu.ufba.softvis.visminer.test.commit;

import static org.junit.Assert.*;

import org.junit.BeforeClass;
import org.junit.Test;

import java.util.List;

import br.edu.ufba.softvis.visminer.main.VisMiner;
import br.edu.ufba.softvis.visminer.model.business.Commit;
import br.edu.ufba.softvis.visminer.model.business.Repository;
import br.edu.ufba.softvis.visminer.test.VisminerTest;

public class CommitTest {
	
	private static VisminerTest visminerTest;
	private static VisMiner visminer;
	private static Repository repository;
	private static List<Commit> commits ;
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		
		visminerTest = VisminerTest.getInstance();
		visminer = visminerTest.getVisminer();
		repository = visminerTest.getRepository(); 
		commits = repository.getProject().getCommits();
	}
	
	@Test
    public void testDate() {
        
        assertEquals("2014-04-09 11:55:44", commits.get(0).getDate());
        assertEquals("2014-04-09 12:03:13", commits.get(1).getDate());
        assertEquals("2014-04-09 13:16:11", commits.get(2).getDate());
        assertEquals("2014-04-09 17:29:47", commits.get(3).getDate());
    }
    
    @Test
    public void testTextCommits()
    {        
        assertEquals("Initial commit", commits.get(0).getMessage());
        assertEquals("initial", commits.get(1).getMessage());
        assertEquals("erro no mapeamento resolvido.", commits.get(2).getMessage());
        assertEquals("Foram adicionados:calculo das métricas NOC e NOP.analíse das versões. ", commits.get(3).getMessage());
    }
    
    @Test
    public void testCountCommit()
    {
        int total = commits.size();
        assertEquals(103, total);
    }
	
}
