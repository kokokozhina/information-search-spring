package com.kokokozhina.lucene.indexing;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.*;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;
import org.springframework.beans.factory.annotation.Value;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class NDCGTask {

    @Value("${index.file.location}")
    private String indexPath;

    private IndexSearcher indexSearcher;

    private double zk = 0.165025;


    public void init() throws IOException {
        Directory dir = FSDirectory.open(new File(indexPath));
        IndexReader indexReader = DirectoryReader.open(dir);
        indexSearcher = new IndexSearcher(indexReader);
    }

    public void getListByQuery(Query query, int n) throws IOException {
        TopDocs hits = indexSearcher.search(query, n);
        System.out.println("Number of hits: " + hits.totalHits);
        for (ScoreDoc sd : hits.scoreDocs)
        {
            Document d = indexSearcher.doc(sd.doc);
            System.out.println(String.format(d.toString()));
        }
    }

    public Query firstQuery() throws ParseException {
        BooleanQuery query = new BooleanQuery();

        QueryParser pa = new QueryParser(Version.LUCENE_40,"author", new StandardAnalyzer(Version.LUCENE_40));
        Query q1 = pa.parse("(author:[А TO О])");

        QueryParser parser = new QueryParser(Version.LUCENE_40,"title", new StandardAnalyzer(Version.LUCENE_40));
        Query q2 = parser.parse("(title:Любовь~)");
        Query q3 = NumericRangeQuery.newIntRange("price", 1, 0, 250, true, true);

        query.add(q1, BooleanClause.Occur.SHOULD);
        query.add(q2, BooleanClause.Occur.SHOULD);
        query.add(q3, BooleanClause.Occur.SHOULD);

        return query;
    }

    public Query secondQuery() throws ParseException {
        BooleanQuery query = new BooleanQuery();

        QueryParser pa = new QueryParser(Version.LUCENE_40,"summary", new StandardAnalyzer(Version.LUCENE_40));
        Query q1 = pa.parse("(summary:Франция~)");

        QueryParser parser = new QueryParser(Version.LUCENE_40,"publisher", new StandardAnalyzer(Version.LUCENE_40));
        Query q2 = parser.parse("(title:Азбука OR Эксмо)");
        Query q3 = NumericRangeQuery.newIntRange("price", 1, 250, 500, true, true);

//        Query q1 = new WildcardQuery(new Term("author", "Борис*"));
//
//        Query q2 = new FuzzyQuery(new Term("title", "Любовница~"));
//        QueryParser pa = new QueryParser(Version.LUCENE_40,"publicationDate", new StandardAnalyzer(Version.LUCENE_40));
//        Query q3 = pa.parse("(publicationDate:[2000 TO 2018])");

        query.add(q1, BooleanClause.Occur.SHOULD);
        query.add(q2, BooleanClause.Occur.SHOULD);
        query.add(q3, BooleanClause.Occur.SHOULD);

        return query;
    }

    public Query thirdQuery() throws ParseException {
        BooleanQuery query = new BooleanQuery();

        QueryParser parser = new QueryParser(Version.LUCENE_40,"author", new StandardAnalyzer(Version.LUCENE_40));
        Query q1 = parser.parse("(author:Борис*)");
        QueryParser pa = new QueryParser(Version.LUCENE_40,"publicationDate", new StandardAnalyzer(Version.LUCENE_40));
        Query q2 = pa.parse("(publicationDate:2012)");
        QueryParser pt = new QueryParser(Version.LUCENE_40,"title", new StandardAnalyzer(Version.LUCENE_40));
        Query q3 = pt.parse("(title:[А TO Л])");

        query.add(q1, BooleanClause.Occur.SHOULD);
        query.add(q2, BooleanClause.Occur.SHOULD);
        query.add(q3, BooleanClause.Occur.SHOULD);


        return query;
    }

    public Query fourthQuery() throws ParseException {
        BooleanQuery query = new BooleanQuery();

        QueryParser parser = new QueryParser(Version.LUCENE_40,"summary", new StandardAnalyzer(Version.LUCENE_40));
        Query q1 = parser.parse("(summary:Русь OR Россия)");
        QueryParser pa = new QueryParser(Version.LUCENE_40,"publicationDate", new StandardAnalyzer(Version.LUCENE_40));
        Query q2 = pa.parse("(publicationDate:[2000 TO 2012])");

        query.add(q1, BooleanClause.Occur.SHOULD);
        query.add(q2, BooleanClause.Occur.SHOULD);

        return query;
    }

    public Query fifthQuery() throws ParseException {
        BooleanQuery query = new BooleanQuery();

        QueryParser parser = new QueryParser(Version.LUCENE_40,"summary", new StandardAnalyzer(Version.LUCENE_40));
        Query q1 = parser.parse("(summary:Ричард~ OR Генрих~ OR Король~)");
        QueryParser pa = new QueryParser(Version.LUCENE_40,"author", new StandardAnalyzer(Version.LUCENE_40));
        Query q2 = pa.parse("(author:Дюма OR Скотт OR Дюфрен)");

        query.add(q1, BooleanClause.Occur.MUST);
        query.add(q2, BooleanClause.Occur.SHOULD);

        return query;
    }

    public Query sixthQuery() throws ParseException {
        BooleanQuery query = new BooleanQuery();

        QueryParser parser = new QueryParser(Version.LUCENE_40,"summary", new StandardAnalyzer(Version.LUCENE_40));
        Query q1 = parser.parse("(summary:Вера)");
        QueryParser pa = new QueryParser(Version.LUCENE_40,"author", new StandardAnalyzer(Version.LUCENE_40));
        Query q2 = pa.parse("(author:Вера)");
        QueryParser p3 = new QueryParser(Version.LUCENE_40,"title", new StandardAnalyzer(Version.LUCENE_40));
        Query q3 = p3.parse("(title:Вера)");


        query.add(q1, BooleanClause.Occur.SHOULD);
        query.add(q2, BooleanClause.Occur.SHOULD);
        query.add(q3, BooleanClause.Occur.SHOULD);

        return query;
    }

    public ArrayList<Integer> readFromConsole() {
        Scanner in = new Scanner(System.in);
        ArrayList<Integer> numbers = new ArrayList<>();
        String[] data = in.nextLine().split(" ");
        for (int i = 0; i < data.length; i++) {
            int num = Integer.parseInt(data[i]);
            if (num < 0 || num > 2) {
                System.exit(1);
            } else {
                numbers.add(num);
            }
        }

        return numbers;
    }


    public double countNdcg(List<List<Integer>> queries) {
        double value = 0;
        for (List<Integer> queryRes: queries) {
            value += zk * getNDCGForRequest(queryRes);
        }
        value /= queries.size();
        return value;
    }

    public double getNDCGForRequest(List<Integer> docs) {
        double value = 0;
        for (int i = 0; i < docs.size(); i++) {
            value += (Math.pow(2, docs.get(i)) - 1) * 1.0 / (2 + i);
        }
        return value;
    }

    public void initNDCGTask() throws IOException, ParseException {
        System.out.println("### NDCG Task start ###");
        init();
        List<List<Integer>> ndcgReview = new ArrayList<>();

        getListByQuery(firstQuery(), 10);
        ndcgReview.add(new ArrayList<>(Arrays.asList(2, 2, 2, 1, 0, 1, 2, 2, 2, 1))); //readFromConsole()

        getListByQuery(secondQuery(), 10);
        ndcgReview.add(new ArrayList<>(Arrays.asList(2, 2, 1, 1, 2, 0, 2, 2, 1, 2)));

        getListByQuery(thirdQuery(), 10);
        ndcgReview.add(new ArrayList<>(Arrays.asList(2, 2, 2, 0, 1, 2, 1, 2, 1, 2)));

        getListByQuery(fourthQuery(), 10);
        ndcgReview.add(new ArrayList<>(Arrays.asList(2, 2, 2, 2, 2, 2, 1, 1, 1, 1)));

        getListByQuery(fifthQuery(), 20);
        ndcgReview.add(new ArrayList<>(Arrays.asList(2, 2, 2, 1, 2, 2, 2, 1, 2, 2,
                2, 0, 0, 2, 2, 2, 2, 2, 0, 2)));

        getListByQuery(sixthQuery(), 20);
        ndcgReview.add(new ArrayList<>(Arrays.asList(2, 2, 2, 2, 2, 2, 2, 2, 2, 2,
                2, 2, 2, 2, 2, 2, 2, 2, 2, 2)));

//        ndcgReview.add(new ArrayList<>(Arrays.asList(2, 2, 2, 2, 2, 2, 2, 0, 2, 2)));
//        ndcgReview.add(new ArrayList<>(Arrays.asList(2, 2, 2, 2, 2, 2, 2, 2, 2, 2)));

        System.out.println("NDCG = " + countNdcg(ndcgReview));
        System.out.println("### NDCG Task end ###");
    }
}
