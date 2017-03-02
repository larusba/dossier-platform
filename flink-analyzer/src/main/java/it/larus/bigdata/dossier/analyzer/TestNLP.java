package it.larus.bigdata.dossier.analyzer;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

import org.apache.commons.lang.ArrayUtils;

import opennlp.tools.namefind.NameFinderME;
import opennlp.tools.namefind.TokenNameFinderModel;
import opennlp.tools.tokenize.Tokenizer;
import opennlp.tools.tokenize.TokenizerME;
import opennlp.tools.tokenize.TokenizerModel;
import opennlp.tools.util.Span;

public class TestNLP {

	public static void main(String[] args) throws IOException {
		findName();
	}

	public static void findName() throws IOException {
		InputStream isToken = new FileInputStream(
				"/Users/omar/Documents/progetti/larus/RicercaSviluppo/dossier-platform/flink-analyzer/src/main/resources/en-token.bin");

		TokenizerModel modelToken = new TokenizerModel(isToken);
		isToken.close();

		Tokenizer tokenizer = new TokenizerME(modelToken);

		String sentence[] = tokenizer.tokenize("Hi. How are you? This is Mike.");

		InputStream isNames = new FileInputStream(
				"/Users/omar/Documents/progetti/larus/RicercaSviluppo/dossier-platform/flink-analyzer/src/main/resources/en-ner-person.bin");

		TokenNameFinderModel model = new TokenNameFinderModel(isNames);
		isNames.close();

		NameFinderME nameFinder = new NameFinderME(model);

		Span nameSpans[] = nameFinder.find(sentence);

		for (Span s : nameSpans) {
			Object[] subarray = ArrayUtils.subarray(sentence, s.getStart(), s.getEnd());
			Arrays.stream(subarray).forEach(System.out::println);
		}
		// System.out.println(s.toString());
	}
}
