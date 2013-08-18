package de.dima.textmining.conll;

import java.io.File;
import java.io.FileFilter;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import de.dima.textmining.languages.LanguageType;
import de.dima.textmining.parser.MateParser;
import de.dima.textmining.parser.Parser;
import de.dima.textmining.shallow.ShallowParser;
import de.dima.textmining.shallow.postagger.StanfordPOSTagger;
import de.dima.textmining.shallow.tokenizer.MaxEntTokenizer;

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

//			writer.append(sb.toString());
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
	
	public static void main(String[] args) {
		
		if (args.length != 2) {
			System.err.println("Please define inputDir and the data outputDir");
		}
		
		ShallowParser shallowParser = new ShallowParser(new MaxEntTokenizer(),
				new StanfordPOSTagger(LanguageType.ENGLISH));
		
		CoNLLNodeSerializer serializer = new CoNLLNodeSerializer();
		
		Parser parser = new MateParser(LanguageType.ENGLISH);
		
		String inputDir = args[0];
		String outputDir = args[1];

		File[] files = new File(inputDir).listFiles();
		FileFilter filter = new TextFilesFilter();
		
		for (final File f : files) {
			if (!f.isDirectory() && !f.isHidden() && f.exists() && f.canRead()
					&& (filter == null || filter.accept(f))) {
				
				System.out.println("Parsing " + f.getName());
				
				try {
					Scanner scanner = new Scanner(f);
					
					List<CoNLLNode> parses = new ArrayList<CoNLLNode>();
					String line;

					while (scanner.hasNextLine()) {

						line = scanner.nextLine();

						if (line.trim().equals(""))
							continue;
						
						parses.add(parser.parse(shallowParser.parseSentence(line)));

					}
					
					File file2 = new File(new File(outputDir).getPath() + "/" + f.getName().substring(0, f.getName().indexOf('.')) + ".conll");
					
					scanner.close();
					
					System.out.println("Serializing " + f.getName());
					
					serializer.serialize(parses, file2);
								
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				}

			}

		}


	}

}
