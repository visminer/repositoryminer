package br.edu.ufba.softvis.visminer.test.committer;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.BeforeClass;
import org.junit.Test;

import br.edu.ufba.softvis.visminer.main.VisMiner;
import br.edu.ufba.softvis.visminer.model.business.Commit;
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
		assertEquals("felipegustavo1000@gmail.com",lastCommitter.getEmail());
		assertEquals("felipe",lastCommitter.getName());
		/*
		//pagseguro
		assertEquals("cegoncalves@uolinc.com",lastCommitter.getEmail());
		assertEquals("cegoncalves",lastCommitter.getName());*/
		
		
	}
	
	@Test
	public void testIdbyCommitter(){
		repository.getProject().lastCommit();
		Committer committer = repository.getProject().getCurrentCommit().getCommitter();
		assertEquals(1,committer.getId());
		
		
		repository.getProject().firstCommit();
		Committer committerFirst = repository.getProject().getCurrentCommit().getCommitter();
		assertEquals(1,committerFirst.getId());
		
			
		}
	
	
	
	@Test 
	public void testAmountCommitsbyCommitter(){
		repository.getProject().lastCommit();
		List<Commit> commits = repository.getProject().getCommitsByCommitterName("felipe");
		assertEquals(56,commits.size());
	
	}
		
	@Test
	public void testSetCommitter(){
		repository.getProject().lastCommit();
		Committer committer = new Committer(8,"rafael@gmail.com","Rafael");	
		repository.getProject().getCurrentCommit().setCommitter(committer);
		
		String name = repository.getProject().getCurrentCommit().getCommitter().getName();
		assertEquals("Rafael",name);
		
		String email = repository.getProject().getCurrentCommit().getCommitter().getEmail();
		assertEquals("rafael@gmail.com",email);
		
		int id = repository.getProject().getCurrentCommit().getCommitter().getId();
		assertEquals(8,id);
	
	}
	
	

}



