package br.edu.ufba.softvis.visminer.analyzer.scm;

import java.io.File;
import java.io.IOException;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.ResetCommand.ResetType;
import org.eclipse.jgit.api.errors.GitAPIException;

import br.edu.ufba.softvis.visminer.error.VisMinerAPIException;


/* 
 * Just the methods of JGitRepository that had problems with some repositories were overridden.
 * This class is slowly than JGitRepository but is safer.
 */
public class GitRepository extends JGitRepository implements SCM{

  private Git git;

  @Override
  public void open(String path){

    super.open(path);
    git = getGit();

  }

  @Override
  public void checkout(String hash){

    try{

      if(!git.status().call().isClean())
        git.reset().setMode(ResetType.HARD).call();

      makeCheckout("master", false);
      makeCheckout(hash, true);

    }catch(GitAPIException e){
      super.clean(e);
    }

  }

  @Override
  public void reset(){

    try{

      if(!git.status().call().isClean())
        git.reset().setMode(ResetType.HARD).call();

      makeCheckout("master", false);
      git.branchDelete().setBranchNames(JGitRepository.VM_BRANCH).setForce(true).call();

    }catch(Exception e){
      super.clean(e);
    }

  }

  private void makeCheckout(String hash, boolean create){

    try{

      if(create)
        Runtime.getRuntime().exec("git checkout -f -b "+JGitRepository.VM_BRANCH+" "+ hash, null,
            new File(getAbsolutePath()));
      else
        Runtime.getRuntime().exec("git checkout -f " + hash, null, new File(getAbsolutePath()));

    }catch(IOException e){
      throw new VisMinerAPIException(e.getMessage(), e);
    }

  }

}