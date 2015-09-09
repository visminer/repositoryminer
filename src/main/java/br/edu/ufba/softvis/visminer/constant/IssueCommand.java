package br.edu.ufba.softvis.visminer.constant;

import java.util.HashMap;
import java.util.Map;

public enum IssueCommand {

	NONE(0, ""),
    CLOSE(1, "close"),
    CLOSES(2, "closes"),
    CLOSED(3, "closed"),
    FIX(4, "fix"),
    FIXES(5, "fixes"),
    FIXED(6, "fixed"),
    RESOLVE(7, "resolve"),
    RESOLVES(8, "resolves"),
    RESOLVED(9, "resolved"),
	CLOSING(10, "closing"),
	FIXING(11, "fixing"),
	RESOLVING(12, "resolving"),
    REOPEN(13, "reopen"),
    REOPENS(14, "reopens"),
    REOPENING(15, "reopening"),
    HOLD(16, "hold"),
    HOLDS(17, "holds"),
    HOLDING(18, "holding"),
    WONTFIX(19, "wontfix"),
    INVALIDATE(20, "invalidate"),
    INVALIDATES(21, "invalidates"),
    INVALIDATED(22, "invalidated"),
    INVALIDATING(23, "invalidating"),
    ADDRESSES(24, "addresses"),
    RE(25, "re"),
    REFERENCES(26, "references"),
    REF(27, "ref"),
    REFS(28, "refs"),
    SEE(29, "see");

	
	private int id;
	private String word;
	
	private IssueCommand(int id, String word){
		this.id = id;
		this.word = word;
	}

	public int getId(){
		return this.id;
	}
	
	public String getWord(){
		return this.word;
	}
	
	public static IssueCommand parse(String word){
		
		if(word == null)
			return IssueCommand.NONE;
		
		for(IssueCommand ic : IssueCommand.values()){
			if(ic.getWord().equals(word)){
				return ic;
			}
		}
		
		return null;
		
	}
	
	public static IssueCommand parse(int id){

		for(IssueCommand ic : IssueCommand.values()){
			if(ic.getId() == id){
				return ic;
			}
		}

		return null;

	}
	
	public static Map<String, Integer> toMap(){
		
		Map<String, Integer> map = new HashMap<String, Integer>();
		for(IssueCommand i : IssueCommand.values()){
			map.put(i.getWord(), i.getId());
		}
		return map;
		
	}
	
}