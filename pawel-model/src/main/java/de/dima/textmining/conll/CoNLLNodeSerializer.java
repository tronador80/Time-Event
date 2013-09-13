package de.dima.textmining.conll;

import java.io.File;
import java.io.FileFilter;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

//exec:
//mvn exec:java -Dexec.mainClass="de.dima.textmining.conll.CoNLLNodeSerializer" -e -Dexec.args="/home/umar/projects/input /home/umar/projects/input/output"

public class CoNLLNodeSerializer {

	public List<CoNLLNode> deSerialize(File deSerializeFile) {

		Scanner reader;
		List<CoNLLNode> parses = new ArrayList<CoNLLNode>();

		try {
			reader = new Scanner(deSerializeFile);

			List<String> sentenceLines = new ArrayList<String>();

			while (reader.hasNextLine()) {

				String line = reader.nextLine();

				if (line.trim().equals("")) {

					parses.add(CoNLLNode.parseCoNLL(sentenceLines));

					sentenceLines = new ArrayList<String>();

				} else {
					sentenceLines.add(line);
				}

			}

			reader.close();

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return parses;
	}

	public void serialize(List<CoNLLNode> parses, File serializeFile) {

		StringBuilder sb = new StringBuilder();

		try {
			FileWriter writer = new FileWriter(serializeFile);

			for (CoNLLNode parse : parses) {
				writer.append(parse.getCompleteSentenceAsCoNLL() + "\n");
			}

			// writer.append(sb.toString());
			writer.flush();
			writer.close();

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	private static class TextFilesFilter implements FileFilter {
		public boolean accept(File pathname) {
			return pathname.getName().toLowerCase().endsWith(".txt");
		}
	}

}
