package it.larus.bigdata.dossier.analyzer;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

import opennlp.tools.namefind.NameFinderME;
import opennlp.tools.namefind.TokenNameFinderModel;
import opennlp.tools.tokenize.Tokenizer;
import opennlp.tools.tokenize.TokenizerME;
import opennlp.tools.tokenize.TokenizerModel;
import opennlp.tools.util.Span;

/**
 * Facade for NLP library. This class avoid the serialization problem of direct opennlp tool usage.
 * @author Omar Rampado
 * FIXME handle errors
 * TODO refactor: extract interface
 */
public class NaturalLanguageProcessor implements Serializable {
	private static final long serialVersionUID = 1L;

	private transient Tokenizer tokenizer;
	private transient NameFinderME nameFinder;

	private String tokenFilename;
	private String nameFilename;

	/**
	 * language configuration
	 * @param tokenFilename ES: en-token.bin 
	 * @param nameFilename ES: en-ner-person.bin
	 */
	public NaturalLanguageProcessor(String tokenFilename, String nameFilename) {
		super();
		this.tokenFilename = tokenFilename;
		this.nameFilename = nameFilename;
		try {
			init();
		} catch (IOException e) {
			throw new IllegalArgumentException(e.getMessage());
		}
	}

	/**
	 * Write nothing
	 * @param out
	 * @throws IOException
	 */
	private void writeObject(ObjectOutputStream out) throws IOException {
		out.defaultWriteObject();
	}

	/**
	 * Load configuration 
	 * @param in
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
		in.defaultReadObject();
		init();
	}

	private void init() throws IOException{
		InputStream isToken = new FileInputStream(tokenFilename);

		TokenizerModel modelToken = new TokenizerModel(isToken);
		isToken.close();

		tokenizer = new TokenizerME(modelToken);

		InputStream isNames = new FileInputStream(nameFilename);

		TokenNameFinderModel model = new TokenNameFinderModel(isNames);
		isNames.close();

		nameFinder = new NameFinderME(model);
		
	}
	
	/**
	 * Find a set of names (one or more words) in the given text
	 * @param text
	 * @return
	 */
	public Set<String> extractNames(String text) {
		if(StringUtils.isEmpty(text)){
			return new HashSet<>();
		}
		String[] tokens = tokenizer.tokenize(text);
		Span nameSpans[] = nameFinder.find(tokens);

		Set<String> set = Arrays.stream(nameSpans).map(s -> {
			String[] subarray = (String[]) ArrayUtils.subarray(tokens, s.getStart(), s.getEnd());
			String join = Arrays.stream(subarray).collect(Collectors.joining(" "));
			return join;
		}).collect(Collectors.toSet());

		return set;
	}
}
