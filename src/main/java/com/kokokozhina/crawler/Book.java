package com.kokokozhina.crawler;

import lombok.Getter;
import lombok.Setter;

import java.math.BigInteger;

@Getter
@Setter
public class Book {

    private String title;

    private String author;

    private String type;

    private String summary;

    private Integer price;

    private Long isbn;

    private String publisher;

    private String publicationDate;

}
