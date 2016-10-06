package org.repositoryminer.scm.hostingservice;

import java.util.HashMap;
import java.util.Map;

public enum IssueCommandType {

	CLOSE("close"),
	CLOSES("closes"),
	CLOSED("closed"),
	FIX("fix"),
	FIXES("fixes"),
	FIXED("fixed"),
	RESOLVE("resolve"),
	RESOLVES("resolves"),
	RESOLVED("resolved"),
	CLOSING("closing"),
	FIXING("fixing"),
	RESOLVING("resolving"),
	REOPEN("reopen"),
	REOPENS("reopens"),
	REOPENING("reopening"),
	HOLD("hold"),
	HOLDS("holds"),
	HOLDING("holding"),
	WONTFIX("wontfix"),
	INVALIDATE("invalidate"),
	INVALIDATES("invalidates"),
	INVALIDATED("invalidated"),
	INVALIDATING("invalidating"),
	ADDRESSES("addresses"),
	RE("re"),
	REFERENCES("references"),
	REF("ref"),
	REFS("refs"),
	SEE("see");

	private String word;

	private IssueCommandType(String word){
		this.word = word;
	}

	private String getWord() {
		return this.word;
	}

	public static IssueCommandType parse(String word){
		if(word == null)
			return null;

		for(IssueCommandType ic : IssueCommandType.values()){
			if(ic.getWord().equals(word)){
				return ic;
			}
		}

		return null;
	}

	public static Map<String, IssueCommandType> toMap(){
		Map<String, IssueCommandType> map = new HashMap<String, IssueCommandType>();
		for(IssueCommandType i : IssueCommandType.values()){
			map.put(i.getWord(), i);
		}
		return map;
	}

}