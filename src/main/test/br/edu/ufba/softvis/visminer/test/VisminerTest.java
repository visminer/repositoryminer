package br.edu.ufba.softvis.visminer.test;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import br.edu.ufba.softvis.visminer.constant.LanguageType;
import br.edu.ufba.softvis.visminer.constant.MetricUid;
import br.edu.ufba.softvis.visminer.constant.SCMType;
import br.edu.ufba.softvis.visminer.main.VisMiner;
import br.edu.ufba.softvis.visminer.model.business.Repository;
import br.edu.ufba.softvis.visminer.retriever.RepositoryRetriever;

public class VisminerTest {

  private static final String PROPS_FILE = "resources/test/test-db.properties";

  private static final String REPOSITORY_DESCRIPTION = "test";
  private static final String REPOSITORY_OWNER = "test";
  private static final String REPOSITORY_NAME = "test";
  private static final SCMType REPOSTIROY_TYPE = SCMType.GIT;
  private final String REPOSITORY_PATH = "C:\\Users\\felipe\\git\\java";
  private final List<MetricUid> METRICS = Arrays.asList(MetricUid.ATFD, MetricUid.CC, MetricUid.NOCAI,
      MetricUid.SLOC, MetricUid.TCC, MetricUid.WMC);
  private final List<LanguageType> LANGS = Arrays.asList(LanguageType.JAVA, LanguageType.NONE);

  private VisMiner visminer;
  private Repository repository;
  private RepositoryRetriever repoRetriever;
  private static VisminerTest vt;

  private VisminerTest() {

    visminer = new VisMiner();
    configParameters();
    repoRetriever = new RepositoryRetriever();

    if (!isRepositoryProcessed()) {
      startRepository();
    }

    repository = repoRetriever.retrieveByPath(REPOSITORY_PATH);
    repository.explore();

  }

  public static VisminerTest getInstance() {

    if (vt == null) {
      vt = new VisminerTest();
    }
    return vt;

  }

  public VisMiner getVisminer() {
    return visminer;
  }

  public Repository getRepository() {
    return repository;
  }

  public RepositoryRetriever getRepositoryRetriever() {
    return repoRetriever;
  }

  private void configParameters() {
    visminer.setDBConfig(PROPS_FILE);
  }

  private void startRepository() {

    Repository repository = new Repository();
    repository.setDescription(REPOSITORY_DESCRIPTION);
    repository.setOwner(REPOSITORY_OWNER);
    repository.setName(REPOSITORY_NAME);
    repository.setPath(REPOSITORY_PATH);
    repository.setType(REPOSTIROY_TYPE);

    try {
      visminer.persistRepository(repository, METRICS,
          LANGS);
    } catch (IOException e) {
      e.printStackTrace();
    }

  }

  private boolean isRepositoryProcessed() {
    return visminer.checkRepository(REPOSITORY_PATH);
  }

}