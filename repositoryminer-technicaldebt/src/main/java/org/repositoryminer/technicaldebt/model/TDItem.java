package org.repositoryminer.technicaldebt.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.bson.Document;
import org.repositoryminer.util.StringUtils;

public class TDItem {

	String filename;
	Map<TDIndicator, Integer> indicators = new HashMap<>();
	Set<TDType> debts = new HashSet<>();

	public TDItem(String filename) {
		this.filename = filename;
	}

	public Document toDocument() {
		setTDDebts();

		List<Document> indicatorsDoc = new ArrayList<Document>();
		for (Entry<TDIndicator, Integer> indicator : indicators.entrySet()) {
			indicatorsDoc.add(new Document("name", indicator.getKey().name())
					.append("occurrences", indicator.getValue()));
		}
		
		// Adding a confirmation flat to TD types and convert them to a document.
		List<Document> typesDoc = new ArrayList<>();
		for (TDType type : debts) {
			typesDoc.add(new Document().append("name", type.name()).append("value", 0));
		}
		
		Document doc = new Document();
		doc.append("filename", filename).
			append("filehash", StringUtils.encodeToCRC32(filename)).
			append("indicators", indicatorsDoc).
			append("debts", typesDoc);
		
		return doc;
	}
	
	public boolean isDebt() {
		return indicators.size() > 0;
	}
	
	public void addOneToIndicator(TDIndicator indicator) {
		addToIndicator(indicator, 1);
	}
	
	public void addToIndicator(TDIndicator indicator, int quantity) {
		if (indicator == null) {
			return;
		}
		
		Integer number = indicators.get(indicator);
		if (number != null) {
			indicators.put(indicator, number + quantity);
		} else {
			indicators.put(indicator, quantity);
		}
	}

	private void setTDDebts() {
		for (TDIndicator indicator : indicators.keySet()) {
			debts.addAll(indicator.getTypes());
		}
	}
	
	public String getFilename() {
		return filename;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}

	public Map<TDIndicator, Integer> getIndicators() {
		return indicators;
	}

	public void setIndicators(Map<TDIndicator, Integer> indicators) {
		this.indicators = indicators;
	}

	public Set<TDType> getDebts() {
		return debts;
	}

	public void setDebts(Set<TDType> debts) {
		this.debts = debts;
	}

}