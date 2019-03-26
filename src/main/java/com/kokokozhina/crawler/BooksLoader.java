package com.kokokozhina.crawler;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Value;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class BooksLoader {

    @Value("${books.file.location}")
    private String path;

    @Value("${books.shop.url}")
    private String url;

    public void downloadBooks() throws IOException {

        for(int i = 1; i < 200; i++) {

            List<Book> booksList = new ArrayList<>();

            Document document = Jsoup.connect(url + i).get();

            Elements books = document.select("table.catalog").select("tr");
            for (Element book : books) {
                Book newBook = new Book();

                newBook.setAuthor(parseElement(book.select("p.authors")));

                newBook.setTitle(parseElement(book.select("p.title")));

                newBook.setType(parseElement(book.select("p.tip-books")));

                parseDetails(book.select("p.details").first(), newBook);

                newBook.setSummary(book.select("p").get(3).text());

                Elements price = book.select("span.price");
                if (price.size() > 0) {
                    newBook.setPrice(Integer.parseInt(price.text().replaceAll("\\s+|[А-Я]+|[а-я]+", "")));
                } else {
                    continue;
                }

                booksList.add(newBook);
            }

            writeToFile(booksList);
        }


    }

    private String parseElement(Elements element) {
        String value = "";
        if(element.size() == 1) {
            return element.text();
        }

        for(Element el : element) {
            value += el.text() + ',';
        }

        if(value.length() > 1) {
            value = value.substring(0, value.length() - 1);
        }
        return value;
    }

    private void parseDetails(Element element, Book book) {
        for(Node el : element.childNodes()) {
            String elString = el.toString().replaceAll(":\\s+", "");
            if (elString.contains("ISBN")) {
                book.setIsbn(Long.parseLong(elString.substring(5).replaceAll("-", "")));
            } else if (elString.contains("Издательство")) {
                book.setPublisher(elString.substring(13));
            } else if (elString.contains("Дата выхода")) {
                book.setPublicationDate(elString.substring(12));
            }

        }

    }

    private void writeToFile(List<Book> booksList) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(path, true))); // append mode file writer
        objectMapper.writeValue(out, booksList);
    }


    public void init() throws IOException {
        downloadBooks();
    }
}
