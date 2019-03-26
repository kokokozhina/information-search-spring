package com.kokokozhina.lucene.indexing;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kokokozhina.crawler.Book;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.*;
import org.apache.lucene.search.*;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;
import org.springframework.beans.factory.annotation.Value;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

public class DynamicIndexTest {

    @Value("${index.file.location}")
    private String indexPath;

    @Value("${book.test.dynamic.index.location}")
    private String pathToTestBook;

    private IndexWriter indexWriter = null;


    private List<Book> parseFile() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        byte[] booksData = Files.readAllBytes(Paths.get(pathToTestBook));
        List<Book> books = Arrays.asList(mapper.readValue(booksData, Book[].class));
        return books;
    }

    public boolean openIndex() {
        try {
            Directory dir = FSDirectory.open(new File(indexPath));
            Analyzer analyzer = new StandardAnalyzer(Version.LUCENE_40);
            IndexWriterConfig iwc = new IndexWriterConfig(Version.LUCENE_40, analyzer);
            iwc.setOpenMode(IndexWriterConfig.OpenMode.CREATE_OR_APPEND);
            indexWriter = new IndexWriter(dir, iwc);
            return true;
        } catch (Exception e) {
            System.err.println("Error opening the index. " + e.getMessage());
        }
        return false;
    }

    public void finish() {
        try {
            indexWriter.commit();
//            indexWriter.close();
        } catch (IOException ex) {
            System.err.println("We had a problem closing the index: " + ex.getMessage());
        }
    }

    public void addIntoIndex() throws IOException {

        openIndex();
        List<Book> books = parseFile();
        Utils.addDocuments(indexWriter, books);
        finish();

    }

    public void searchIndex() throws IOException {

        Directory dir = FSDirectory.open(new File(indexPath));
        IndexReader indexReader = DirectoryReader.open(dir);
        IndexSearcher indexSearcher = new IndexSearcher(indexReader);

        Term t = new Term("author", "Ольга Кожина");
        Query query = new TermQuery(t);
        TopDocs hits = indexSearcher.search(query, 10);
        System.out.println("Number of hits: " + hits.totalHits);
        for (ScoreDoc sd : hits.scoreDocs) {
            Document d = indexSearcher.doc(sd.doc);
            System.out.println(String.format(d.toString()));
        }

    }

    public void deleteFromIndex() throws IOException {

        indexWriter.deleteDocuments(new Term("author", "Ольга Кожина"));

        indexWriter.commit();
        System.out.println("index contains deleted files: "+ indexWriter.hasDeletions());
        System.out.println("index contains documents: "+ indexWriter.maxDoc());
        System.out.println("index contains deleted documents: "+ indexWriter.numDocs());
    }

    public void investigateDynamicIndex() throws IOException {

        System.out.println("### Investigate dynamic index ###");
        addIntoIndex();
        searchIndex();
        deleteFromIndex();
        searchIndex();
    }


}
