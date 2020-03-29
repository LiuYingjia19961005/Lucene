package com.znsd.Lucene;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.LongPoint;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.FSDirectory;
import org.junit.Before;
import org.junit.Test;
import org.wltea.analyzer.lucene.IKAnalyzer;

import java.io.File;
import java.io.IOException;

public class SearchIndex {
    private IndexReader indexReader;
    private IndexSearcher indexSearcher;
    @Before
    public void init() throws IOException {
        indexReader = DirectoryReader.open(FSDirectory.open(new File("E:\\LuceneSaveIndex").toPath()));
        indexSearcher = new IndexSearcher(indexReader);
    }

    /**
     * 数值范围查询
     * @throws Exception
     */
    @Test
    public void testRangeQuery() throws Exception {
        Query query = LongPoint.newRangeQuery("size", 0l, 10000l);
        printResult(query);
    }

    private void printResult(Query query) throws Exception{
        TopDocs topDocs = indexSearcher.search(query, 10);
        System.out.println("总记录数" + topDocs.totalHits);
        ScoreDoc[] scoreDocs = topDocs.scoreDocs;
        for (ScoreDoc s:scoreDocs) {
            int docId = s.doc;
            Document document = indexSearcher.doc(docId);
            System.out.println(document.get("name"));
            System.out.println(document.get("path"));
            //System.out.println(document.get("Content"));
            System.out.println(document.get("size"));
            System.out.println("-----------------------------------");
        }
        indexReader.close();
    }

    /**
     * QueryParser: 进行查询
     */
    @Test
    public void testQueryParser() throws Exception {
        //创建QueryParser对象, 两参数
        QueryParser queryParser = new QueryParser("name",new IKAnalyzer());
        //cs1:默认搜索域, 参数2: 分析器对象
        Query query = queryParser.parse("Lucene是一个java开发全文检索的工具包");
        //执行查询
        printResult(query);
    }
}























