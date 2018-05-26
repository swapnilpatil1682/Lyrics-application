package com.mycujoo.lyricsapp.controller;

import java.io.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.mycujoo.lyricsapp.UrlFromConf;
import com.mycujoo.lyricsapp.model.Lyrics;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import opennlp.tools.postag.POSModel;
import opennlp.tools.postag.POSSample;
import opennlp.tools.postag.POSTaggerME;
import opennlp.tools.tokenize.WhitespaceTokenizer;

@Controller
public class LyricsController {

    private static final Logger log = LoggerFactory.getLogger(LyricsController.class);

    @GetMapping("/verbs/artist/title")
    @ResponseBody
    public Map<String, List<String>> sayVerbs() throws IOException {

        String urlFromFile = "";
        ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
        try {

            UrlFromConf urlFromConf = mapper.readValue(new ClassPathResource("conf.yaml").getFile(), UrlFromConf.class);

//    UrlFromConf urlFromConf = mapper.readValue(new File("/Users/swapnilpatil/Documents/Swapnil-Personnel/NewsLetter/Original/gs-actuator-service-master/complete/src/main/resources/conf.yaml"), UrlFromConf.class);

            System.out.println(ReflectionToStringBuilder.toString(urlFromConf, ToStringStyle.MULTI_LINE_STYLE));
//      urlFromFile = String.valueOf(Integer.parseInt(urlFromConf.getUrl()));
            urlFromFile = String.valueOf(urlFromConf.getUrl());

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        RestTemplate restTemplate = new RestTemplate();
        Lyrics lyrics = restTemplate.getForObject(urlFromFile, Lyrics.class);
        log.info(lyrics.toString());


        // OPENNLP code to extract lyrics and adjectives
        //Loading Parts of speech-maxent model
        InputStream inputStream = new FileInputStream("en-pos-maxent.bin");
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

                if (boundWords[1].equals("NNP")) {
                    System.out.println("tag is NN and Word is : " + boundWords[0]);
                    advList.add(boundWords[0]);
                }
            }

            if (advList.size() > 0) {
                resultMap.put("Verbs", advList);
            }
        }

        System.out.println(resultMap);
        return resultMap;
    }


    @GetMapping("/adjectives/artist/title")
    @ResponseBody
    public Map<String, List<String>> sayAdjectives() throws IOException {

        String urlFromFile = "";
        ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
        try {

            UrlFromConf urlFromConf = mapper.readValue(new ClassPathResource("conf.yaml").getFile(), UrlFromConf.class);

            //    UrlFromConf urlFromConf = mapper.readValue(new File("/Users/swapnilpatil/Documents/Swapnil-Personnel/NewsLetter/Original/gs-actuator-service-master/complete/src/main/resources/conf.yaml"), UrlFromConf.class);

            System.out.println(ReflectionToStringBuilder.toString(urlFromConf, ToStringStyle.MULTI_LINE_STYLE));
//      urlFromFile = String.valueOf(Integer.parseInt(urlFromConf.getUrl()));
            urlFromFile = String.valueOf(urlFromConf.getUrl());

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

//      System.out.println(" URL given in config file is  :" + urlFromFile);

        RestTemplate restTemplate = new RestTemplate();
        Lyrics lyrics = restTemplate.getForObject(urlFromFile, Lyrics.class);
        log.info(lyrics.toString());


        // OPENNLP code to extract lyrics and adjectives
        //Loading Parts of speech-maxent model
        InputStream inputStream = new FileInputStream("en-pos-maxent.bin");
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







