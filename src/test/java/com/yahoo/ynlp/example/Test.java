package com.yahoo.ynlp.example;

import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;

import edu.stanford.nlp.dcoref.CorefChain;
import edu.stanford.nlp.dcoref.CorefCoreAnnotations;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;

public class Test {

	public static void main(String[] args) {

		//String text = "Viki is a smart boy. He knows a lot of things.";
		
		/*
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
		*/
		
		String text = ""
				+ "Mr. Goldberg grew up in Minneapolis and graduated from Harvard in 1989 with a degree in history and government. "
				+ "He joined the consulting firm Bain & Company, then moved to Capitol Records as a marketer. "
				+ "In 1993, he and his best friend from high school started Launch Media, a digital music magazine that was initially distributed by CD-ROM. "
				+ "In 2001, Yahoo bought Launch, and Mr. Goldberg became head of Yahoo Music, living in Los Angeles. "
				+ "Around that time, he began dating Ms. Sandberg, then an advertising executive at Google, in the Bay Area. "
				+ "\"I lost the coin flip as to where we were going to live,\" he told Business Insider in a recent interview. "
				+ "In 2007, he quit Yahoo and joined Benchmark Capital as an entrepreneur in residence, where he spent a couple of years before becoming chief executive of SurveyMonkey. ";
		
		Annotation document = new Annotation(text);
		Properties props = new Properties();
		props.put("annotators", "tokenize, ssplit, pos, parse, lemma, ner, dcoref");
		StanfordCoreNLP pipeline = new StanfordCoreNLP(props);
		pipeline.annotate(document);
		
		Map<Integer, CorefChain> graph = document
				.get(CorefCoreAnnotations.CorefChainAnnotation.class);

		/*
		Iterator<Integer> itr = graph.keySet().iterator();
		while (itr.hasNext()) {
			String key = itr.next().toString();
			System.out.println(key + " " + graph.get(key));
		}
		*/
		
		Iterator<Entry<Integer, CorefChain>> itr1 = graph.entrySet().iterator();

		while (itr1.hasNext()) {
			Entry<Integer, CorefChain> entry = itr1.next();
			System.out.println(entry.getKey()+ " " + entry.getValue());
		}

	}
}