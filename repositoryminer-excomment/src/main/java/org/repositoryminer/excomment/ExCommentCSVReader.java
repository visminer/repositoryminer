package org.repositoryminer.excomment;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.io.FilenameUtils;
import org.repositoryminer.excomment.model.Comment;
import org.repositoryminer.excomment.model.Heuristic;
import org.repositoryminer.excomment.model.Pattern;

public class ExCommentCSVReader {

	private static final String[] COMMENTS_HEADER = { "idcomment", "total_pattern", "total_heuristic", "total_score",
			"comment", "path", "class", "method" };
	private static final String[] PATTERNS_HEADER = { "idcomment", "pattern", "pattern_score", "pattern_class", "theme",
			"tdtype" };
	private static final String[] HEURISTICS_HEADER = { "idcomment", "heuristic_description", "heuristic_status",
			"heuristic_score" };

	// the comment id is used as key
	private Map<Integer, Comment> commentsMap = new HashMap<Integer, Comment>();

	// keeps the relationship between files and comments
	private Map<String, List<Integer>> filesMap = new HashMap<String, List<Integer>>();

	private String repositoryPath;
	private ExCommentConfig config;

	public ExCommentCSVReader(String repositoryPath, ExCommentConfig config) {
		this.repositoryPath = repositoryPath;
		this.config = config;
	}

	public Map<Integer, Comment> getCommentsMap() {
		return commentsMap;
	}

	public Map<String, List<Integer>> getFilesMap() {
		return filesMap;
	}

	public void readCSVs() throws IOException {
		readComments();
		readHeuristics();
		readPatterns();
	}

	private void readComments() throws IOException {
		List<CSVRecord> records = readCSV(COMMENTS_HEADER, config.getCommentsCSV());

		for (CSVRecord record : records) {
			Comment comment = new Comment(Integer.parseInt(record.get(0)),
					Double.parseDouble(record.get(1).replaceAll(",", ".")),
					Double.parseDouble(record.get(2).replaceAll(",", ".")),
					Double.parseDouble(record.get(3).replaceAll(",", ".")), record.get(4), record.get(6), record.get(7));

			String filename = FilenameUtils.normalize(record.get(5), true);

			
			if (!filesMap.containsKey(filename)) {
				filesMap.put(filename, new ArrayList<Integer>());
			}

			commentsMap.put(comment.getId(), comment);
			filesMap.get(filename).add(comment.getId());
		}
	}

	private void readPatterns() throws IOException {
		List<CSVRecord> records = readCSV(PATTERNS_HEADER, config.getPatternsCSV());

		for (CSVRecord record : records) {
			Pattern pattern = new Pattern(record.get(1), Double.parseDouble(record.get(2).replaceAll(",", ".")),
					record.get(3), record.get(4), record.get(5));

			Comment comment = commentsMap.get(Integer.parseInt(record.get(0)));
			if (comment == null) {
				continue;
			}

			comment.getPatterns().add(pattern);
		}
	}

	private void readHeuristics() throws IOException {
		List<CSVRecord> records = readCSV(HEURISTICS_HEADER, config.getHeuristicsCSV());

		for (CSVRecord record : records) {
			Heuristic heuristic = new Heuristic(record.get(1), Integer.parseInt(record.get(2)),
					Double.parseDouble(record.get(3).replaceAll(",", ".")));

			Comment comment = commentsMap.get(Integer.parseInt(record.get(0)));
			if (comment == null) {
				continue;
			}

			comment.getHeuristics().add(heuristic);
		}
	}

	private List<CSVRecord> readCSV(String[] header, String filename) throws IOException {
		FileReader fileReader = new FileReader(filename);

		CSVFormat format = CSVFormat.DEFAULT.withDelimiter(config.getDelimiter()).withHeader(header)
				.withSkipHeaderRecord();

		CSVParser csvParser = new CSVParser(fileReader, format);

		List<CSVRecord> records = csvParser.getRecords();

		fileReader.close();
		csvParser.close();

		return records;
	}

}
