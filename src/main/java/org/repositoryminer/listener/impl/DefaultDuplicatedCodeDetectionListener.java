package org.repositoryminer.listener.impl;

import java.io.IOException;
import java.math.RoundingMode;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import org.bson.Document;
import org.repositoryminer.codesmell.CodeSmellId;
import org.repositoryminer.listener.IDuplicatedCodeDetectionListener;
import org.repositoryminer.metric.SLOCMetric;
import org.repositoryminer.parser.Parser;

import net.sourceforge.pmd.cpd.Mark;
import net.sourceforge.pmd.cpd.Match;

public class DefaultDuplicatedCodeDetectionListener implements IDuplicatedCodeDetectionListener {

	private List<Document> smellDocs, duplicatedCodeDocs;
	
	public DefaultDuplicatedCodeDetectionListener(List<Document> smellDocs) {
		this.smellDocs = smellDocs;
		duplicatedCodeDocs = new ArrayList<Document>();
	}
	
	@Override
	public void initDuplicationDetection() {
	}

	@Override
	public void updateDuplicationDetection(Match match, Parser parser) {
		Document auxDoc = new Document();
		auxDoc.append("line_count", match.getLineCount());
		auxDoc.append("token_count", match.getTokenCount());
		auxDoc.append("source_code_slice", match.getSourceCodeSlice());
		auxDoc.append("language", parser.getLanguage());
		
		List<Document> filesDoc = new ArrayList<Document>();
		for (Mark mark : match.getMarkSet()) {
			Document fileDoc = new Document();
			fileDoc.append("begin_line", mark.getBeginLine());
			fileDoc.append("end_line", mark.getEndLine());
			fileDoc.append("file_name", mark.getFilename());
			fileDoc.append("percentage", getDuplicatedPercentage(mark.getFilename(), match.getLineCount()));
			filesDoc.add(fileDoc);
		}
		
		auxDoc.append("files", filesDoc);
		duplicatedCodeDocs.add(auxDoc);
	}
	
	private Double getDuplicatedPercentage(String filename, int lineCount) {
		try {
			String source = new String(Files.readAllBytes(Paths.get(filename)));
			SLOCMetric slocMetric = new SLOCMetric();
			
			int sloc = slocMetric.calculate(source);
			double percentage = (double) lineCount / sloc;
			DecimalFormat df = new DecimalFormat("#.####");
			df.setRoundingMode(RoundingMode.CEILING);
			return Double.valueOf(df.format(percentage));
		} catch (IOException e) {
			return 0.0;
		}
	}

	@Override
	public void endOfDuplicationDetection() {
		Document duplicationsDoc = new Document();
		duplicationsDoc.append("name", CodeSmellId.DUPLICATED_CODE).append("occurrences", duplicatedCodeDocs);
		smellDocs.add(duplicationsDoc);
	}

}
