package com.kokokozhina.utils;

import javafx.util.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.util.*;

import static java.lang.Math.min;

@Service
public class KGrammIndex {

    @Autowired
    private List<String> dictionary;

    @Autowired
    @Qualifier("kGrammIndex")
    private Map<String, List<Integer>> kGrammIndex;

    @Autowired
    @Qualifier("kGrammIndexForOneWord")
    private Map<String, List<String>> kGrammIndexForOneWord;

    @Autowired
    public LevenshteinDistance levenshteinDistance;

    @Value("${kgramm.value}")
    public Integer k;

    public Set<Integer> getSetOfWordsWithAtLeastOneSimilarKGramm(String s) {
        List<String> kGramms = AppConfig.splitWordInKGramms(s, k);


        Set<Integer> grammsUnion = new HashSet<>();
        for(String kGramm : kGramms) {
            List<Integer> cur = kGrammIndex.get(kGramm);
            if (cur != null) {
                grammsUnion.addAll(cur);
            }
        }

        return grammsUnion;
    }

    public List<Pair<Integer, String>> kGrammIndex(int numberOfMatches, String s) {
        System.out.println("KGramms started at " + LocalTime.now());

        List<String> kGramms = AppConfig.splitWordInKGramms(s, k);

        int n = s.length();

        Set<Integer> candidates = getSetOfWordsWithAtLeastOneSimilarKGramm(s);

        List<Pair<Double, String>> topMatches = new ArrayList<>();

        for(Integer candidate : candidates) {
            List<String> candidatesGramms = kGrammIndexForOneWord.get(dictionary.get(candidate));

            ArrayList<String> union = new ArrayList<>(kGramms);
            union.addAll(candidatesGramms);

            ArrayList<String> intersection = new ArrayList<>(kGramms);
            intersection.retainAll(candidatesGramms);

            double curDist = intersection.size() * 1.0 / union.size();

            topMatches.add(new Pair<>(curDist, dictionary.get(candidate)));

        }
        topMatches.sort((o1, o2) -> {
            if (o1.getKey() < o2.getKey()) {
                return 1;
            } else if (o1.getKey().equals(o2.getKey())) {
                return 0;
            } else {
                return -1;
            }
        });

        topMatches = topMatches.subList(0, min(topMatches.size(), 500));



        ArrayList<String> onlyValues = new ArrayList<>();
        topMatches.forEach(line -> onlyValues.add(line.getValue()));

        List<Pair<Integer, String>> topMatchesWithLevenshtein =
                levenshteinDistance.getTopMatches(onlyValues, numberOfMatches, s);

        System.out.println("KGramms ended at " + LocalTime.now());

        return topMatchesWithLevenshtein;
    }




}
