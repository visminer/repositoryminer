package br.edu.ufba.softvis.visminer.utility;

import java.io.IOException;

import org.eclipse.jgit.errors.IncorrectObjectTypeException;
import org.eclipse.jgit.errors.MissingObjectException;
import org.eclipse.jgit.errors.StopWalkException;
import org.eclipse.jgit.lib.PersonIdent;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevWalk;
import org.eclipse.jgit.revwalk.filter.RevFilter;
import org.gitective.core.filter.commit.PersonFilter;

/**
 * @version 0.9
 * Custom filter implementation of Gitctive API.
 */
public class AuthorEmailFilter extends PersonFilter{

	private String email;
	
	public AuthorEmailFilter(final PersonIdent person){
		super(person);
	}
	
	public AuthorEmailFilter(final String email) {
		super(null, email);
		this.email = email;
	}

	@Override
	public boolean include(RevWalk walker, RevCommit commit) throws StopWalkException, MissingObjectException,
			IncorrectObjectTypeException, IOException {
		return compare(commit.getAuthorIdent()) ? true : include(false);
	}
	
	@Override
	public RevFilter clone(){
		return new AuthorEmailFilter(email);
	}
	
	private boolean compare(final PersonIdent ident){
		
		if(ident == null){
			return false;
		}
		
		if(email != null && !email.equals(ident.getEmailAddress())){
			return false;
		}
		
		return true;
		
	}

}