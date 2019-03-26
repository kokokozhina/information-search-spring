//import au.com.bytecode.opencsv.CSVReader;
//import javafx.util.Pair;
//import org.apache.lucene.analysis.Analyzer;
//import org.apache.lucene.analysis.standard.StandardAnalyzer;
//import org.apache.lucene.document.Document;
//import org.apache.lucene.document.Field;
//import org.apache.lucene.document.IntField;
//import org.apache.lucene.document.TextField;
//import org.apache.lucene.index.DirectoryReader;
//import org.apache.lucene.index.IndexReader;
//import org.apache.lucene.index.IndexWriter;
//import org.apache.lucene.index.IndexWriterConfig;
//import org.apache.lucene.queryparser.classic.ParseException;
//import org.apache.lucene.queryparser.classic.QueryParser;
//import org.apache.lucene.search.*;
//import org.apache.lucene.store.Directory;
//import org.apache.lucene.store.FSDirectory;
//import org.apache.lucene.util.BytesRef;
//import org.apache.lucene.util.IntsRef;
//import org.apache.lucene.util.Version;
//import org.apache.lucene.util.fst.Builder;
//import org.apache.lucene.util.fst.FST;
//import org.apache.lucene.util.fst.PositiveIntOutputs;
//import org.apache.lucene.util.fst.Util;
//
//import java.io.File;
//import java.io.FileReader;
//import java.io.IOException;
//import java.util.ArrayList;
//import java.util.Arrays;
//import java.util.Comparator;
//import java.util.List;
//
//public class Indexer {
//    public static void index(File dataDir) throws IOException {
//        Analyzer analyzer = new StandardAnalyzer(Version.LUCENE_40);
//        //Path indexPath = Files.createTempDirectory("tempIndex");
//        Directory directory = FSDirectory.open(dataDir);
//        IndexWriterConfig config = new IndexWriterConfig(Version.LUCENE_40, analyzer);
//        config.setOpenMode(IndexWriterConfig.OpenMode.CREATE);
//        IndexWriter iwriter = new IndexWriter(directory, config);
//
//        //IndexWriter writer = new IndexWriter(indexDir, new StandardAnalyzer(), true);
//        indexDirectory(iwriter, dataDir);
//
//        iwriter.commit();
//        iwriter.close();
//        iwriter.close();
//    }
//
//    private static void indexDirectory(IndexWriter writer, File dir)
//            throws IOException {
//        File[] files = dir.listFiles();
//        for (int i = 0; i < files.length; i++) {
//            File f = files[i];
//            if (f.isDirectory()) {
//                indexDirectory(writer, f); // recurse
//            } else if (f.getName().endsWith(".csv")) {
//                indexFile(writer, f);
//            }
//        }
////        writer.commit();
////        writer.close();
//    }
//
//    private static void indexDirectory2(File dir)
//            throws IOException {
//        File[] files = dir.listFiles();
//        for (int i = 0; i < files.length; i++) {
//            File f = files[i];
//            if (f.isDirectory()) {
//                indexDirectory2(f); // recurse
//            } else if (f.getName().endsWith(".csv")) {
//                indexFile2(f);
//            }
//        }
////        writer.commit();
////        writer.close();
//    }
//
//    private static void indexFile(IndexWriter writer, File f)
//            throws IOException {
//        System.out.println("Indexing " + f.getName());
//
//        CSVReader reader = new CSVReader(new FileReader(f), ',', '"', 1);
//        //Read CSV line by line and use the string array as you want
//        String[] nextLine;
//
//        while ((nextLine = reader.readNext()) != null) {
//            if (nextLine != null) {
//                Document doc = new Document();
//                doc.add(new IntField("id", Integer.parseInt(nextLine[0]), Field.Store.YES));
//                doc.add(new TextField("title", nextLine[1].toString(), Field.Store.YES));
//                doc.add(new TextField("price", nextLine[2].toString(), Field.Store.YES));
//                doc.add(new TextField("author", nextLine[3].toString(), Field.Store.YES));
//                doc.add(new TextField("genre", nextLine[4].toString(), Field.Store.YES));
//                doc.add(new TextField("symbol", nextLine[5].toString(), Field.Store.YES));
//                doc.add(new IntField("view", Integer.parseInt(nextLine[6]), Field.Store.YES));
//                doc.add(new IntField("like", Integer.parseInt(nextLine[7]), Field.Store.YES));
//                doc.add(new IntField("comment", Integer.parseInt(nextLine[8]), Field.Store.YES));
//                doc.add(new IntField("recenses", Integer.parseInt(nextLine[9]), Field.Store.YES));
//                doc.add(new TextField("description", nextLine[10].toString(), Field.Store.YES));
//
//                //doc.add(Field.Text("contents", new FileReader(f)));
//                //doc.add(Field.Keyword("filename", f.getCanonicalPath()));
//                writer.addDocument(doc);
//            }
//        }
//    }
//    public static void add_doc(IndexWriter iw, Directory dir) throws IOException {
//        Document doc = new Document();
//        doc.add(new IntField("id", 10000, Field.Store.YES));
//        doc.add(new TextField("title", "uno", Field.Store.YES));
//        doc.add(new TextField("price", "uno", Field.Store.YES));
//        doc.add(new TextField("author", "uno", Field.Store.YES));
//        doc.add(new TextField("genre", "uno", Field.Store.YES));
//        doc.add(new TextField("symbol", "uno", Field.Store.YES));
//        doc.add(new IntField("view", 10000000, Field.Store.YES));
//        doc.add(new IntField("like", 10000, Field.Store.YES));
//        doc.add(new IntField("comment", 100000, Field.Store.YES));
//        doc.add(new IntField("recenses", 100000, Field.Store.YES));
//        doc.add(new TextField("description", "uno", Field.Store.YES));
//        iw.addDocument(doc);
//        iw.commit();
//        iw.close();
//    }
//
//    public static void del_doc(IndexWriter iwriter, Query query) throws IOException {
//        iwriter.deleteDocuments(query);
//        iwriter.commit();
//        iwriter.close();
//    }
//
//    public static void search(File indexDir) throws Exception {
//        Directory fsDir = FSDirectory.open(indexDir);
//        IndexReader indexReader = DirectoryReader.open(fsDir);
//
//        IndexWriterConfig config = new IndexWriterConfig(Version.LUCENE_40, new StandardAnalyzer(Version.LUCENE_40));
//        config.setOpenMode(IndexWriterConfig.OpenMode.APPEND);
//        IndexWriter iwriter = new IndexWriter(fsDir, config);
//        //add_doc(iwriter, fsDir);
//
//
//        IndexSearcher is = new IndexSearcher(indexReader);
//
//        //Term t = new Term("author", "Андрей Ватагин");
//        //Query query = new TermQuery(t);
////textsearch
//        QueryParser parser = new QueryParser(Version.LUCENE_40,"title", new StandardAnalyzer(Version.LUCENE_40));
//        //Query query = parser.parse("author:Андрей, price: 100");
//        //del_doc(iwriter, query);
//        //  iwriter.deleteDocuments(query);
////iwriter.commit();
////iwriter.close();
//
//        //Query query = parser.parse("author:Андрей, price:100");
//        //Query query = parser.parse("author:uno, price:uno");
////integer search
//        NumericRangeQuery query = NumericRangeQuery.newIntRange("view", 3000, 4000, true, true);
//
//        TopDocs hits = is.search(query, 10);
//        System.out.println("Number of hits: " + hits.totalHits);
//
//        for (ScoreDoc sd : hits.scoreDocs) {
//            Document d = is.doc(sd.doc);
//            System.out.println(String.format(d.toString()));
//        }
//        indexReader.close();
//    }
//
//
//    static Comparator<Pair<Long, String>> mycomp = new Comparator<Pair<Long, String>>(){
//        @Override
//        public int compare(Pair<Long, String> o1, Pair<Long, String> o2)  {
//            if ((o1.getClass().equals(Pair.class)) && (o2.getClass().equals(Pair.class))) {
//                Pair<Long, String> p1 = (Pair<Long, String>) o1;
//                Pair<Long, String> p2 = (Pair<Long, String>) o2;
//                return comPair(p1, p2);
//
//
//            }
//            throw new AssertionError("Unknown Types");
//        }
//
//        public int comPair(Pair<Long, String> p1, Pair<Long, String> p2) {
//            if (p1.getValue().compareTo(p2.getValue()) == 0) {
//                return p1.getKey().compareTo(p2.getKey());
//            }
//            return p1.getValue().compareTo(p2.getValue());
//        }
//    };
//
//
//    @SuppressWarnings("Since15")
//    private static void indexFile2(File f)
//            throws IOException {
//        System.out.println("Indexing " + f.getName());
//
//        CSVReader reader = new CSVReader(new FileReader(f), ',', '"', 1);
//        //Read CSV line by line and use the string array as you want
//        String[] nextLine;
//        List<Pair<Long,String>> pairs = new ArrayList<Pair<Long, String>>();
//        while ((nextLine = reader.readNext()) != null) {
//            if (nextLine != null) {
//                pairs.add(new Pair<Long, String>((long) Integer.parseInt(nextLine[0]), nextLine[3].toString()));
//            }
//        }
//
//        ArrayList<String> inputValues = new ArrayList<>();
//        ArrayList<Long> outputValues = new ArrayList<>();
//
//        pairs.sort(mycomp);
//        for (Pair p : pairs) {
//            inputValues.add(String.valueOf(p.getValue()));
//            outputValues.add((Long) p.getKey());
//        }
//
//        PositiveIntOutputs outputs = PositiveIntOutputs.getSingleton();
//        Builder<Long> builder = new Builder<Long>(FST.INPUT_TYPE.BYTE1, outputs);
//        BytesRef scratchBytes = new BytesRef();
//        IntsRef scratchInts = new IntsRef();
//        for (Pair p : pairs) {
//            try {
//                scratchBytes.copyChars((CharSequence) p.getValue());
//                builder.add(Util.toIntsRef(scratchBytes, scratchInts), (Long) p.getKey());
//            }
//            catch (Exception exc){
//                continue;
//            }
//        }
//
//        FST<Long> fst = builder.finish();
//
//        Long value = Util.get(fst, new BytesRef("Artem Kamenistyy"));
//        System.out.println(value);
//
////        IntsRef key = Util.getByOutput(fst, 66);
////        System.out.println(Util.toBytesRef(key, scratchBytes).utf8ToString());
//
////
////        BytesRefFSTEnum<Long> iterator = new BytesRefFSTEnum<Long>(fst);
////        while (iterator.next() != null) {
////            BytesRefFSTEnum.InputOutput<Long> mapEntry = iterator.current();
////            System.out.print(mapEntry.input.utf8ToString() + " ");
////            System.out.println(mapEntry.output);
//        // }
//
//    }
//
//
//    public static void fst(File Fdir) throws IOException, ParseException {
//        Analyzer analyzer = new StandardAnalyzer(Version.LUCENE_40);
//        //Path indexPath = Files.createTempDirectory("tempIndex");
//        Directory directory = FSDirectory.open(Fdir);
//
//        //IndexWriter writer = new IndexWriter(indexDir, new StandardAnalyzer(), true);
//        indexDirectory2(Fdir);
//
//    }
//
//
//    public static double Zk = 0.132;
//    public static double getNDCG(ArrayList<ArrayList<Integer>> queries) {
//        double value = 0;
//        for (ArrayList<Integer> query_results: queries) {
//            value += Zk * getNDCGForRequest(query_results);
//        }
//        value /= queries.size();
//        return value;
//    }
//    public static double getNDCGForRequest(ArrayList<Integer> docs) {
//        double value = 0;
//        for (int i = 0; i < docs.size(); i++) {
//            value += (Math.pow(2, docs.get(i)) - 1) / (2 + i);
//        }
//        return value;
//    }
//
//
//    public static void ndsg(File fdir) throws Exception {
//        Directory fsDir = FSDirectory.open(fdir);
//        IndexReader indexReader = DirectoryReader.open(fsDir);
//        IndexSearcher is = new IndexSearcher(indexReader);
//
//        QueryParser parser = new QueryParser(Version.LUCENE_40,"price", new StandardAnalyzer(Version.LUCENE_40));
//        //NumericRangeQuery query = NumericRangeQuery.newIntRange("view", 3000, 4000, true, true);
//        //f_query
//        //Query query = parser.parse("(author:Andrey*) AND (price:{0 TO 12*}) AND NOT (price:Подписка)");
//        //Query query = parser.parse("(description:Игра) AND (genre:Боевая фантастика)");
//
////        TopDocs hits = is.search(query, 25);
////        System.out.println("Number of hits: " + hits.totalHits);
//        ArrayList<ArrayList<Integer>> qr = new ArrayList<ArrayList<Integer>>(){
//        };
//
////        ArrayList<Integer> f_query = new ArrayList<Integer>(Arrays.asList(2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2));
////        ArrayList<Integer> sec_query = new ArrayList<Integer>(Arrays.asList(2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2));
//
//        ArrayList<Integer> f_query = new ArrayList<Integer>(Arrays.asList(1, 2, 2, 2, 1, 1, 2, 2, 2, 2, 0, 0, 0, 0));
//        ArrayList<Integer> sec_query = new ArrayList<Integer>(Arrays.asList(2, 0, 0, 1, 1, 1, 0, 1, 1, 1, 2, 2, 1, 0, 1, 1, 0, 0, 0, 1, 2, 0));
//        qr.add(f_query);
//        qr.add(sec_query);
//
////        for (ScoreDoc sd : hits.scoreDocs) {
////            Document d = is.doc(sd.doc);
//////            System.out.println(String.format(d.toString()));
////            System.out.println(String.format(d.getField("title").toString() + d.getField("author") + d.getField("price")));
////        }
//
//        System.out.println(getNDCG(qr));
//
//        indexReader.close();
//
//    }
//
//    public static void main(String[] args) throws Exception {
//        File dataDir = new File("D:\\rut\\src\\main\\resources\\");
//        //index(dataDir);
//        //search(dataDir);
//        //fst(dataDir);
//        ndsg(dataDir);
//    }
//}