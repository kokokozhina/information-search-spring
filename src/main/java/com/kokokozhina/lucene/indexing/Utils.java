package com.kokokozhina.lucene.indexing;

import com.kokokozhina.crawler.Book;
import org.apache.lucene.document.*;
import org.apache.lucene.index.IndexWriter;

import java.io.IOException;
import java.util.List;

import static org.apache.lucene.document.Field.Store.YES;

public class Utils {

    public static Document getDocumentFromBook(Book book) {
        Document doc = new Document();

        doc.add(new TextField("title", book.getTitle() != null ? book.getTitle() : "null", YES));
        doc.add(new TextField("author", book.getAuthor() != null ? book.getAuthor() : "null", YES));
        doc.add(new TextField("type", book.getType() != null ? book.getType() : "null", YES));
        doc.add(new TextField("summary", book.getSummary() != null ? book.getSummary() : "null", YES));
        doc.add(new IntField("price", book.getPrice() != null ? book.getPrice() : -1, YES));
        doc.add(new LongField("isbn", book.getIsbn() != null ? book.getIsbn() : -1, YES));
        doc.add(new TextField("publisher", book.getPublisher() != null ? book.getPublisher() : "null", YES));
        doc.add(new TextField("publicationDate", book.getPublicationDate() != null ? book.getPublicationDate() : "null", YES));

        return doc;
    }

    public static void addDocuments(IndexWriter indexWriter, List<Book> books) {
        for (Book book : books) {
            Document doc = getDocumentFromBook(book);

            try {
                indexWriter.addDocument(doc);
            } catch (IOException ex) {
                System.err.println("Error adding documents to the index. " + ex.getMessage());
            }
        }
    }
}
