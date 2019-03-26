package com.kokokozhina.lucene.indexing;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kokokozhina.crawler.Book;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.lucene.util.BytesRef;
import org.apache.lucene.util.IntsRef;
import org.apache.lucene.util.fst.Builder;
import org.apache.lucene.util.fst.FST;
import org.apache.lucene.util.fst.PositiveIntOutputs;
import org.apache.lucene.util.fst.Util;
import org.springframework.beans.factory.annotation.Value;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static jdk.nashorn.internal.ir.debug.ObjectSizeCalculator.getObjectSize;

public class FSTUsage {

    @Value("${books.file.location}")
    private String pathToFile;

    private FST<Long> fst;
    private List<Pair<String, Long>> booksTitleAndIsbn;


    private List<Book> parseFile() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        byte[] booksData = Files.readAllBytes(Paths.get(pathToFile));
        List<Book> books = Arrays.asList(mapper.readValue(booksData, Book[].class));
        return books;
    }

    public static <T> Predicate<T> distinctByKey(Function<? super T, ?> keyExtractor) {
        Set<Object> seen = ConcurrentHashMap.newKeySet();
        return t -> seen.add(keyExtractor.apply(t));
    }

    public void fstConstruction() throws IOException {
        List<Book> books = parseFile();
        booksTitleAndIsbn = books.stream()
                .filter(x -> StringUtils.isNotBlank(x.getTitle()) && x.getIsbn() != null)
                .filter(distinctByKey(Book::getTitle))
                .map(x -> new ImmutablePair<>(x.getTitle(), x.getIsbn()))
                .sorted()
                .collect(Collectors.toList());

        PositiveIntOutputs outputs = PositiveIntOutputs.getSingleton();
        Builder<Long> builder = new Builder<>(FST.INPUT_TYPE.BYTE1, outputs);
        BytesRef scratchBytes = new BytesRef();
        IntsRef scratchInts = new IntsRef();
        for (Pair<String, Long> book : booksTitleAndIsbn) {
            scratchBytes.copyChars(book.getKey());
            builder.add(Util.toIntsRef(scratchBytes, scratchInts), book.getValue());
        }
        fst = builder.finish();
    }

    public Long searchByTitle(String title) throws IOException {
        return Util.get(fst, new BytesRef(title));
    }

    public void initFSTUsage() throws IOException {
        System.out.println("## Investigating FST usage ##");

        fstConstruction();
        String title = "Барышни де Лире";
        Long isbn = searchByTitle(title);
        System.out.println(title + " has ISBN = " + isbn);
        System.out.println("Weight of List<Pair<String, Long>> " + getObjectSize(booksTitleAndIsbn) / 1024L + " kb");
        System.out.println("Weight of FST " + getObjectSize(fst) / 1024L + " kb");

        System.out.println("## End of investigation of FST usage ##");
    }

}
