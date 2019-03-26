package com.kokokozhina;

import com.kokokozhina.crawler.BooksLoader;
import com.kokokozhina.utils.KGrammIndex;
import com.kokokozhina.utils.LevenshteinDistance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.List;

@SpringBootApplication
public class InformationSearchSpringApplication {

//    @Autowired
//    public LevenshteinDistance levenshteinDistance;
//
//    @Autowired
//    public List<String> dictionary;

//    @Autowired
//    public BooksLoader booksLoader;

//    @Autowired
//    public KGrammIndex kGrammIndex;

    public static void main(String[] args) {
        SpringApplication.run(InformationSearchSpringApplication.class, args);
    }

//    @PostConstruct
//    public void task1() {
//        String word = "poppplation";
//        String word = "arrcation";
//        String word = "овтaмовиль";

//        levenshteinDistance.getTopMatches(dictionary, 10, word).forEach(System.out::println);
//        kGrammIndex.kGrammIndex(10, word).forEach(System.out::println);
//    }

}


