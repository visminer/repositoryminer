package br.edu.ufba.softvis.visminer.test.committer;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.BeforeClass;
import org.junit.Test;

import br.edu.ufba.softvis.visminer.main.VisMiner;
import br.edu.ufba.softvis.visminer.model.business.Committer;
import br.edu.ufba.softvis.visminer.model.business.Repository;
import br.edu.ufba.softvis.visminer.test.VisminerTest;

public class CommitterTest {

	
	private static VisminerTest visminerTest;
	private static VisMiner visminer;
	private static Repository repository;
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		
		visminerTest = VisminerTest.getInstance();
		visminer = visminerTest.getVisminer();
		repository = visminerTest.getRepository(); 
		
	}
	
	/*
	 * this test verifies the size of the whole list of committes for the project
	 * */
	@Test
	public void testSizeOfWholeListOfCommitters() {
		List<Committer> committers = repository.getCommitters();
		assertEquals(7, committers.size());
		
		//assertEquals(11, committers.size()); //pag-seguro
		
		assertEquals("visminerproject@gmail.com", committers.get(0).getEmail());
		assertEquals("felipegustavo1000@gmail.com", committers.get(1).getEmail());
		assertEquals("hr.sanches@gmail.com", committers.get(2).getEmail());
		assertEquals("renatoln@yahoo.com.br", committers.get(3).getEmail());
		assertEquals("root@localhost.localdomain", committers.get(4).getEmail());
		assertEquals("luispscarvalho@gmail.com", committers.get(5).getEmail());
		assertEquals("davidpinho08@gmail.com", committers.get(6).getEmail());
		
		/*
		//tests for pagseguro 
		assertEquals("desenvolvedores@pagseguro.com.br", committers.get(0).getEmail());
		assertEquals("cegoncalves@uolinc.com", committers.get(1).getEmail());
		assertEquals("wellington.camargo@s2it.com.br", committers.get(2).getEmail());
		assertEquals("bruno.krebs@tecsinapse.com.br", committers.get(3).getEmail());
		assertEquals("amauri.hamasu@s2it.com.br", committers.get(4).getEmail());
		assertEquals("andre.silva@s2it.com.br", committers.get(5).getEmail());
		assertEquals("gamaral@ubuntu.(none)", committers.get(6).getEmail());
		assertEquals("grecio@uolinc.com", committers.get(7).getEmail());
		assertEquals("cad_cberganton@uolinc.com", committers.get(8).getEmail());
		assertEquals("rribaldo@uolinc.com", committers.get(9).getEmail());
		assertEquals("avicente@uolinc.com", committers.get(10).getEmail());*/
	}
	
	@Test
	public void testFirstAndLastCommitters() {
		/*
		 * obs. to verify those who are the first and last committers I looked in gitk
		 * */
		
		repository.getProject().firstCommit();
		Committer firstCommitter = repository.getProject().getCurrentCommit().getCommitter();
		assertEquals("visminerproject@gmail.com",firstCommitter.getEmail());
		assertEquals("visminer",firstCommitter.getName());
		
		/*
		//pagseguro 
		assertEquals("desenvolvedores@pagseguro.com.br",firstCommitter.getEmail());
		assertEquals("pagseguro",firstCommitter.getName());*/
		
		repository.getProject().lastCommit();
		Committer lastCommitter = repository.getProject().getCurrentCommit().getCommitter();
		assertEquals("renatonovais@gmail.com",lastCommitter.getEmail());
		assertEquals("renatoln",lastCommitter.getName());
		
		/*
		//pagseguro
		assertEquals("cegoncalves@uolinc.com",lastCommitter.getEmail());
		assertEquals("cegoncalves",lastCommitter.getName());*/
		
		
	}
	

}


/*
for (Committer committer : committers) {
 
	System.out.println("Id - Name: " + committer.getId() + " - " + committer.getName());
	System.out.println("Email: " + committer.getEmail());
}
*/
/*
(2) Id - Name: 1 - cegoncalves
Email: cegoncalves@uolinc.com
(5) Id - Name: 2 - amaurihamasus2it
Email: amauri.hamasu@s2it.com.br
(8) Id - Name: 3 - Guilherme Rigo Recio
Email: grecio@uolinc.com
(9) Id - Name: 4 - Carlos Gustavo Berganton
Email: cad_cberganton@uolinc.com
(11) Id - Name: 5 - Andre Abe Vicente
Email: avicente@uolinc.com
(10) Id - Name: 6 - Ribaldo
Email: rribaldo@uolinc.com
(1) Id - Name: 7 - pagseguro
Email: desenvolvedores@pagseguro.com.br
(3) Id - Name: 8 - Wellington Camargo
Email: wellington.camargo@s2it.com.br
(4) Id - Name: 9 - bruno_krebs
Email: bruno.krebs@tecsinapse.com.br
(7) Id - Name: 10 - Personal
Email: gamaral@ubuntu.(none)
(6) Id - Name: 11 - andresilvas2it
Email: andre.silva@s2it.com.br
 * 
 * */
