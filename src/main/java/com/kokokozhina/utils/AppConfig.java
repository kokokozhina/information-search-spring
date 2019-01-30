package com.kokokozhina.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Stream;

@Configuration
public class AppConfig {

    @Bean(name = "Dictionary")
    public List<String> dictionary(@Value("${dictionary.location}") String path) {
        List<String> dictionary = new ArrayList<>();

        try (Stream<String> lines = Files.lines(Paths.get(path))) {
            lines.forEach(x -> dictionary.add(x.toLowerCase()));
        } catch (IOException e) {
            e.printStackTrace();
        }

        return dictionary;
    }

    public static List<String> splitWordInKGramms(String word, int k) {
        String newWord = "$" + word + "$";
        List<String> wordKGramms = new ArrayList<String>();
        for(int i = 0; i < word.length(); i++) {
            wordKGramms.add(newWord.substring(i, i + 3));
        }

        return wordKGramms;
    }

    @Bean(name = "kGrammIndex")
    @DependsOn("Dictionary")
    public Map<String, List<Integer>> kGrammIndex(@Autowired List<String> dictionary, @Value("${kgramm.value}") Integer k) {
        Map<String, List<Integer>> kGrammIndex = new HashMap<>();

        for(int i = 0; i < dictionary.size(); i++) {
            String word = dictionary.get(i);
            Set<String> splittedWord = new HashSet<>(splitWordInKGramms(word, k));
            for(String kGramm : splittedWord) {
                if (kGrammIndex.containsKey(kGramm)) {
                    List<Integer> listOfIndexesForKGramm = kGrammIndex.get(kGramm);
                    listOfIndexesForKGramm.add(i);
                    kGrammIndex.put(kGramm, listOfIndexesForKGramm);
                } else {
                    List<Integer> listOfIndexesForKGramm = new ArrayList<>();
                    listOfIndexesForKGramm.add(i);
                    kGrammIndex.put(kGramm, listOfIndexesForKGramm);
                }
            }
        }

        return kGrammIndex;
    }

    @Bean(name = "kGrammIndexForOneWord")
    @DependsOn("Dictionary")
    public Map<String, List<String>> kGrammIndexForOneWord(@Autowired List<String> dictionary, @Value("${kgramm.value}") Integer k) {
        Map<String, List<String>> kGrammIndex = new HashMap<>();

        for(int i = 0; i < dictionary.size(); i++) {
            String word = dictionary.get(i);
            List<String> splittedWord = splitWordInKGramms(word, k);
            kGrammIndex.put(word, splittedWord);
        }

        return kGrammIndex;
    }


}
