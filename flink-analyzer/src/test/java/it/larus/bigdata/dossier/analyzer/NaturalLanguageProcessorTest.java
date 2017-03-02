package it.larus.bigdata.dossier.analyzer;

import static org.junit.Assert.*;

import java.io.File;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

public class NaturalLanguageProcessorTest {

	private NaturalLanguageProcessor nlp;

	@Before
	public void setUp() throws Exception {

		ClassLoader classLoader = getClass().getClassLoader();
		File fileName = new File(classLoader.getResource("en-ner-person.bin").getFile());
		File fileToken = new File(classLoader.getResource("en-token.bin").getFile());

		nlp = new NaturalLanguageProcessor(fileToken.getAbsolutePath(), fileName.getAbsolutePath());
	}

	@Test
	public void testExtractNamesNull() {
		Set<String> set = nlp.extractNames(null);
		assertNotNull(set);
		assertTrue(set.isEmpty());
	}
	
	@Test
	public void testExtractNamesEmpty() {
		Set<String> set = nlp.extractNames("");
		assertNotNull(set);
		assertTrue(set.isEmpty());
	}
	
	@Test
	public void testExtractNamesWhite() {
		Set<String> set = nlp.extractNames("   ");
		assertNotNull(set);
		assertTrue(set.isEmpty());
	}

	@Test
	public void testExtractNamesNoName() {
		Set<String> set = nlp.extractNames("This phrase doesn't have names inside");
		assertNotNull(set);
		assertTrue(set.isEmpty());
	}

	@Test
	public void testExtractNamesOne() {
		Set<String> set = nlp.extractNames("Do you know who Nelson is?");
		
		assertEquals(1,set.size());
		assertEquals("Nelson", set.iterator().next());
	}
	
	@Test
	public void testExtractNamesComposed() {
		Set<String> set = nlp.extractNames("Do you know who Nelson Mandela is?");
		
		assertEquals(1,set.size());
		assertEquals("Nelson Mandela", set.iterator().next());
	}
	
	@Test
	public void testExtractNamesTwoComposed() {
		Set<String> set = nlp.extractNames("Do you know who Nelson Mandela is? He isn't Barack Obama!");

		assertEquals(2,set.size());
		assertTrue(set.contains("Nelson Mandela"));
		assertTrue(set.contains("Barack Obama"));
	}
}
