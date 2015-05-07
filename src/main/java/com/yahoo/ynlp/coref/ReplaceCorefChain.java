package com.yahoo.ynlp.coref;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Scanner;

import edu.stanford.nlp.dcoref.CorefChain;
import edu.stanford.nlp.dcoref.CorefChain.CorefMention;
import edu.stanford.nlp.dcoref.CorefCoreAnnotations.CorefChainAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.IndexAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.LemmaAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.NamedEntityTagAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.PartOfSpeechAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.SentencesAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.TextAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.TokensAnnotation;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.semgraph.SemanticGraph;
import edu.stanford.nlp.semgraph.SemanticGraphCoreAnnotations.CollapsedCCProcessedDependenciesAnnotation;
import edu.stanford.nlp.trees.Tree;
import edu.stanford.nlp.trees.TreeCoreAnnotations.TreeAnnotation;
import edu.stanford.nlp.util.CoreMap;

public class ReplaceCorefChain {
	public static void main(String[] args) {
		//demo1();
		//demo2();

		demo(args);
	}

	public static void demo(String[] args) {
		String inputFilePath = args[0];
		String outputFilePath = args[1];

		System.out.println("input: " + inputFilePath);
		System.out.println("output: " + outputFilePath);

		StringBuilder inputStringBuilder = new StringBuilder();

		File inFile = new File(inputFilePath);
		Scanner sc = null;
		try {
			sc = new Scanner(inFile);
			while (sc.hasNextLine()) {
				String line = sc.nextLine();
				inputStringBuilder.append(line);
				inputStringBuilder.append(" ");
			}
			sc.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		String outputString = replaceLongestCorefChain(inputStringBuilder
				.toString());

		File outFile = new File(outputFilePath);
		FileWriter fWriter;
		try {
			fWriter = new FileWriter(outFile);
			PrintWriter pWriter = new PrintWriter(fWriter);
			pWriter.println(outputString);
			pWriter.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void demo1() {
		String text = ""
				+ "Mr. Goldberg grew up in Minneapolis and graduated from Harvard in 1989 with a degree in history and government. "
				+ "He joined the consulting firm Bain & Company, then moved to Capitol Records as a marketer. "
				+ "In 1993, he and his best friend from high school started Launch Media, a digital music magazine that was initially distributed by CD-ROM. "
				+ "In 2001, Yahoo bought Launch, and Mr. Goldberg became head of Yahoo Music, living in Los Angeles. "
				+ "Around that time, he began dating Ms. Sandberg, then an advertising executive at Google, in the Bay Area. "
				+ "\"I lost the coin flip as to where we were going to live,\" he told Business Insider in a recent interview. "
				+ "In 2007, he quit Yahoo and joined Benchmark Capital as an entrepreneur in residence, where he spent a couple of years before becoming chief executive of SurveyMonkey. ";

		System.out.println(replaceLongestCorefChain(text));
	}

	public static void demo2() {
		String text = ""
				+ "Dave Goldberg, chief executive of SurveyMonkey and husband of Facebook Chief Operating Officer Sheryl Sandberg, died suddenly Friday night. "
				+ "Goldberg’s brother posted the news on his Facebook page Saturday morning. "
				+ "Facebook CEO Mark Zuckerberg shared this post on his own page shortly after. "
				+ "\"We are deeply heartbroken to say our CEO and friend, Dave Goldberg passed away last night,\" a SurveyMonkey spokeswoman said. "
				+ "\"We are heartbroken by this news,\" a Facebook spokeswoman said. "
				+ "Goldberg, 47, a former Yahoo executive, joined SurveyMonkey in 2009 and grew the closely held company from 12 employees to more than 450. "
				+ "The company was valued at close to $2 billion as part of a $250 million fundraising round late last year intended to help the provider of online survey questionnaires break into the corporate market. "
				+ "Goldberg was also an outspoken ally of his wife’s efforts to promote equal pay and treatment for women in the workplace. "
				+ "Friends and colleagues took to his Facebook page soon after to share their condolences, and also paid tribute on Twitter. ";

		System.out.println(replaceLongestCorefChain(text));
	}

	public static String replaceLongestCorefChain(String text) {
		Properties props = new Properties();
		props.put("annotators",
				"tokenize, ssplit, pos, parse, lemma, ner, dcoref");
		StanfordCoreNLP pipeline = new StanfordCoreNLP(props);

		Annotation document = new Annotation(text);
		pipeline.annotate(document);

		// these are all the sentences in this document
		// a CoreMap is essentially a Map that uses class objects as keys
		// and has values with custom types
		List<CoreMap> sentences = document.get(SentencesAnnotation.class);

		List<List<String>> outputSentences = new LinkedList<List<String>>();

		for (CoreMap sentence : sentences) {
			// traversing the words in the current sentence
			// a CoreLabel is a CoreMap with additional token-specific
			// methods

			// System.out.println("sentence=" + sentence);

			List<String> outputTokens = new LinkedList<String>();

			for (CoreLabel token : sentence.get(TokensAnnotation.class)) {
				// this is the text of the token
				String word = token.get(TextAnnotation.class);
				// System.out.print("token=" + word);
				outputTokens.add(word);

				// this is the index of the token
				String index = "" + token.get(IndexAnnotation.class);
				// System.out.print("\t| index=" + index);

				// this is the POS tag of the token
				String pos = token.get(PartOfSpeechAnnotation.class);
				// System.out.print("\t| pos=" + pos);

				// this is the parse of the token
				String lemma = token.get(LemmaAnnotation.class);
				// System.out.print("\t| lemma=" + lemma);

				// this is the NER label of the token
				String ne = token.get(NamedEntityTagAnnotation.class);
				// System.out.print("\t| ne=" + ne);

				// System.out.println();
			}

			outputSentences.add(outputTokens);

			// this is the parse tree of the current sentence
			Tree tree = sentence.get(TreeAnnotation.class);
			// Tree parse = sentence.get(BinarizedTreeAnnotation.class);
			// System.out.println("tree=" + tree.toString());

			// this is the Stanford dependency graph of the current sentence
			SemanticGraph dependencies = sentence
					.get(CollapsedCCProcessedDependenciesAnnotation.class);

			// System.out.println("dependencies=" +
			// dependencies.toString());
		}

		// This is the coreference link graph
		// Each chain stores a set of mentions that link to each other,
		// along with a method for getting the most representative mention
		// Both sentence and token offsets start at 1!
		Map<Integer, CorefChain> graph = document
				.get(CorefChainAnnotation.class);

		Iterator<Entry<Integer, CorefChain>> corefChainIter = graph.entrySet()
				.iterator();

		// CorefChain longestCorefChain = null;
		while (corefChainIter.hasNext()) {
			Entry<Integer, CorefChain> entry = corefChainIter.next();
			CorefChain tempCorefChain = entry.getValue();

			if (tempCorefChain.getMentionsInTextualOrder().size() <= 2) {
				continue;
			}

			// StringBuilder strBdr = new StringBuilder();
			// int sentIndex = 0;
			// int tokenIndex = 0;
			// System.out.println("CoRef Entity: " + corefChain.getChainID()
			// + " " + corefChain.getRepresentativeMention());
			Iterator<CorefMention> mentionIter = tempCorefChain
					.getMentionsInTextualOrder().iterator();
			while (mentionIter.hasNext()) {
				CorefMention corefMention = mentionIter.next();

				int sentIndex = corefMention.sentNum - 1;
				int startIndex = corefMention.startIndex - 1;
				int endIndex = corefMention.endIndex - 1;
				
				outputSentences.get(sentIndex).set(startIndex,
						tempCorefChain.getRepresentativeMention().mentionSpan);
				for (int index = startIndex + 1; index < endIndex; index++) {
					outputSentences.get(sentIndex).set(index, "");
				}

				/*
				System.out.println(corefMention.toString() + " | "
						+ corefMention.sentNum + " | "
						+ corefMention.corefClusterID + " | "
						+ corefMention.mentionID + " | "
						+ corefMention.position + " | "
						+ corefMention.mentionSpan + " | "
						+ corefMention.startIndex + " " + corefMention.endIndex
						+ " | " + corefMention.headIndex);
				*/
			}
		}

		StringBuilder strBdr = new StringBuilder();
		Iterator<List<String>> outputSentenceIter = outputSentences.iterator();
		while (outputSentenceIter.hasNext()) {
			Iterator<String> outputTokenIter = outputSentenceIter.next()
					.iterator();
			while (outputTokenIter.hasNext()) {
				String token = outputTokenIter.next();
				strBdr.append(token);
				if (token.length() > 0) {
					strBdr.append(" ");
				}
			}
		}

		text = strBdr.toString();
		return text;
	}

}