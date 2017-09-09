import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.repositoryminer.codemetric.direct.AMW;
import org.repositoryminer.codemetric.direct.ATFD;
import org.repositoryminer.codemetric.direct.CYCLO;
import org.repositoryminer.codemetric.direct.FDP;
import org.repositoryminer.codemetric.direct.IDirectCodeMetric;
import org.repositoryminer.codemetric.direct.LAA;
import org.repositoryminer.codemetric.direct.LOC;
import org.repositoryminer.codemetric.direct.LVAR;
import org.repositoryminer.codemetric.direct.MAXNESTING;
import org.repositoryminer.codemetric.direct.NOA;
import org.repositoryminer.codemetric.direct.NOAM;
import org.repositoryminer.codemetric.direct.NOAV;
import org.repositoryminer.codemetric.direct.NOM;
import org.repositoryminer.codemetric.direct.NOPA;
import org.repositoryminer.codemetric.direct.NProtM;
import org.repositoryminer.codemetric.direct.PAR;
import org.repositoryminer.codemetric.direct.TCC;
import org.repositoryminer.codemetric.direct.WMC;
import org.repositoryminer.codemetric.direct.WOC;
import org.repositoryminer.codesmell.direct.FeatureEnvy;
import org.repositoryminer.codesmell.direct.GodClass;
import org.repositoryminer.codesmell.direct.IDirectCodeSmell;
import org.repositoryminer.mining.RepositoryMiner;
import org.repositoryminer.parser.IParser;
import org.repositoryminer.parser.java.JavaParser;
import org.repositoryminer.persistence.Connection;
import org.repositoryminer.scm.GitSCM;

public class Main {

	public static void main(String[] args) throws IOException {
		Connection conn = Connection.getInstance();
		conn.connect("mongodb://localhost", "rm_final_2");
		
		RepositoryMiner rm = new RepositoryMiner();
		
		List<IDirectCodeMetric> metrics = new ArrayList<IDirectCodeMetric>();
		metrics.add(new AMW());
		metrics.add(new ATFD());
		metrics.add(new CYCLO());
		metrics.add(new FDP());
		metrics.add(new LAA());
		metrics.add(new LOC());
		metrics.add(new LVAR());
		metrics.add(new MAXNESTING());
		metrics.add(new NOA());
		metrics.add(new NOAM());
		metrics.add(new NOAV());
		metrics.add(new NOM());
		metrics.add(new NOPA());
		metrics.add(new NProtM());
		metrics.add(new PAR());
		metrics.add(new TCC());
		metrics.add(new WMC());
		metrics.add(new WOC());
		
		List<IDirectCodeSmell> codesmells = new ArrayList<IDirectCodeSmell>();
		codesmells.add(new GodClass());
		codesmells.add(new FeatureEnvy());
		
		List<IParser> parsers = new ArrayList<IParser>();
		parsers.add(new JavaParser(null));

		List<String> refs = Arrays.asList("refs/heads/master");
		
		rm.setRepositoryPath("/home/felipe/Eclipse-workspace/ProjetoTeste");
		rm.setRepositoryName("junit");
		rm.setRepositoryDescription("teste");
		
		rm.setScm(new GitSCM());
		
		rm.setParsers(parsers);
		rm.setDirectCodeMetrics(metrics);
		rm.setDirectCodeSmells(codesmells);
		rm.setReferences(refs);
		
		rm.mine();
		
		conn.close();;
	}

}
