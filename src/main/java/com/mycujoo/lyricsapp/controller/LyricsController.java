package com.mycujoo.lyricsapp.controller;

import java.io.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.mycujoo.lyricsapp.model.Lyrics;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

import opennlp.tools.postag.POSModel;
import opennlp.tools.postag.POSSample;
import opennlp.tools.postag.POSTaggerME;
import opennlp.tools.tokenize.WhitespaceTokenizer;

import java.io.InputStream;

@Controller
public class LyricsController {

    private static final Logger log = LoggerFactory.getLogger(LyricsController.class);

    @Value("${url}")
    String url;

    @GetMapping("/verbs/{artist}/{title}")
    @ResponseBody
    public Map<String, List<String>> sayVerbs(@PathVariable("artist")String artist,
                                              @PathVariable("title")String title) throws IOException {

        log.info(url);
        String urlFromFile = url.replace("{artist}",artist).replace("{title}",title);
        log.info(urlFromFile);
        ObjectMapper mapper = new ObjectMapper(new YAMLFactory());

        RestTemplate restTemplate = new RestTemplate();
        Lyrics verbs = restTemplate.getForObject(urlFromFile, Lyrics.class);
        log.info(verbs.toString());

        // OPENNLP code to extract verbs and adjectives
        //Loading Parts of speech-maxent model

        InputStream inputStream = new FileInputStream("/tmp/en-pos-maxent.bin");
        POSModel model = new POSModel(inputStream);

        //Creating an object of WhitespaceTokenizer class
        WhitespaceTokenizer whitespaceTokenizer = WhitespaceTokenizer.INSTANCE;

        //Tokenizing the sentence
        String sentence = verbs.getLyrics().toString();
        String[] tokens = whitespaceTokenizer.tokenize(sentence);

        //Instantiating POSTaggerME class
        POSTaggerME tagger = new POSTaggerME(model);

        //Generating tags
        String[] tags = tagger.tag(tokens);

        /*
        NN	Noun, singular or mass
        DT	Determiner
        VB	Verb, base form
        VBD	Verb, past tense
        VBZ	Verb, third person singular present
        IN	Preposition or subordinating conjunction
        NNP	Proper noun, singular
        TO	to
        JJ	Adjective */
        //Instantiating the POSSample class
        POSSample sample = new POSSample(tokens, tags);
        Map<String, List<String>> resultMap = new HashMap<String, List<String>>();
        if (sample != null) {
            String output = sample.toString();
            String[] rawWords = output.split(" ");
            List<String> advList = new ArrayList<String>();
            List<String> verbList = new ArrayList<String>();

            for (String rawWord : rawWords) {
                String[] boundWords = rawWord.split("_");
                if (boundWords[1].equals("VB")) {
                    System.out.println("tag is NN and Word is : " + boundWords[0]);

                }
                switch (boundWords[1]) {
                    case "NNP":
                        advList.add(boundWords[0]);
                        break;
                    case "VB":
                    case "VBZ":
                    case "VBD":
                        verbList.add(boundWords[0]);
                        break;
                }
            }


            if (verbList.size() > 0) {
                resultMap.put("verbs", verbList);
            }
        }

        System.out.println(resultMap);
        return resultMap;
    }


    @GetMapping("/adjectives/{artist}/{title}")
    @ResponseBody
    public Map<String, List<String>> sayAdjectives(@PathVariable("artist")String artist,
                                                   @PathVariable("title")String title) throws IOException {

        String urlFromFile = url.replace("{artist}",artist).replace("{title}",title);
        log.info(urlFromFile);
        ObjectMapper mapper = new ObjectMapper(new YAMLFactory());

        RestTemplate restTemplate = new RestTemplate();
        Lyrics lyrics = restTemplate.getForObject(urlFromFile, Lyrics.class);
        log.info(lyrics.toString());

        // OPENNLP code to extract lyrics and adjectives
        //Loading Parts of speech-maxent model
        InputStream inputStream = new FileInputStream("/tmp/en-pos-maxent.bin");
        POSModel model = new POSModel(inputStream);

        //Creating an object of WhitespaceTokenizer class
        WhitespaceTokenizer whitespaceTokenizer = WhitespaceTokenizer.INSTANCE;

        //Tokenizing the sentence
        String sentence = lyrics.getLyrics().toString();
        System.out.println(" Sentence is " + sentence);
        String[] tokens = whitespaceTokenizer.tokenize(sentence);

        //Instantiating POSTaggerME class
        POSTaggerME tagger = new POSTaggerME(model);

        //Generating tags
        String[] tags = tagger.tag(tokens);

        /*
        NN	Noun, singular or mass
        DT	Determiner
        VB	Verb, base form
        VBD	Verb, past tense
        VBZ	Verb, third person singular present
        IN	Preposition or subordinating conjunction
        NNP	Proper noun, singular
        TO	to
        JJ	Adjective */

        System.out.println("tags : " + tags);
        //Instantiating the POSSample class
        POSSample sample = new POSSample(tokens, tags);
        System.out.println(sample.toString());
        Map<String, List<String>> resultMap = new HashMap<String, List<String>>();
        if (sample != null) {
            String output = sample.toString();
            String[] rawWords = output.split(" ");
            List<String> advList = new ArrayList<String>();
            for (String rawWord : rawWords) {
                String[] boundWords = rawWord.split("_");

                if (boundWords[1].equals("JJ")) {
                    System.out.println("tag is JJ and Word is : " + boundWords[0]);
                    advList.add(boundWords[0]);
                }
            }

            if (advList.size() > 0) {
                resultMap.put("Adjectives", advList);
            }
        }

        System.out.println(resultMap);
        return resultMap;
    }
}







