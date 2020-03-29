package com.znsd.Lucene;

import org.apache.commons.io.FileUtils;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.document.*;
import org.apache.lucene.index.*;
import org.apache.lucene.search.*;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.junit.Test;
import org.wltea.analyzer.lucene.IKAnalyzer;

import java.io.File;
import java.io.IOException;

public class LuceneFirst {
    /**
     * 创建索引库
     * @throws IOException
     */
    @Test
    public void createIndex() throws IOException {
        //Directory directory = new RAMDirectory();   //把索引库保存在内存中

        Directory directory = FSDirectory.open(new File("E:\\LuceneSaveIndex").toPath());   //把索引保存到磁盘中
        IndexWriterConfig config = new IndexWriterConfig(new IKAnalyzer());
        //2.基于Directory 创建IndexWriter对象
        IndexWriter indexWriter = new IndexWriter(directory, config);

        //3.读取磁盘上的文件, 对应每个文件创建一个文档对象
        File dir = new File("I:\\讲义和资料\\5.流行框架\\61.Lucene\\lucene\\02.参考资料\\searchsource");
        File[] files = dir.listFiles();
        for (File f:files) {
            String fName = f.getName(); //文件名
            String fPath = f.getPath(); //文件路径
            String fielContent = FileUtils.readFileToString(f, "utf-8");//字符集
            long fileSize = FileUtils.sizeOf(f);    //文件大小

            //创建Field  参数1: 域的名称; 参数2: 域的内容 参数3: 是否存储
            Field fieldName = new TextField("name", fName, Field.Store.YES);
            //Field fieldPath = new TextField("path", fPath, Field.Store.YES);
            Field fieldPath = new StoredField("path", fPath);
            Field fieldContent = new TextField("content", fielContent, Field.Store.YES);
            //Field fieldSize = new TextField("size", fileSize+"", Field.Store.YES);
            Field fieldSize = new LongPoint("size", fileSize);
            Field fieldSizeStore = new StoredField("size",fileSize);
            //创建文档对象
            Document document = new Document();
            //向文档对象中添加域
            document.add(fieldName);
            document.add(fieldPath);
            document.add(fieldContent);
            //document.add(fieldSize);
            document.add(fieldSize);
            document.add(fieldSizeStore);

            //把文档对象写入索引库
            indexWriter.addDocument(document);
        }
        indexWriter.close();
    }

    /**
     * 查询索引库
     * @throws Exception
     */
    @Test
    public void searchIndex() throws Exception{
        Directory directory = FSDirectory.open(new File("E:\\LuceneSaveIndex").toPath());
        IndexReader indexReader = DirectoryReader.open(directory);
        IndexSearcher indexSearcher = new IndexSearcher(indexReader);
        Query query = new TermQuery(new Term("name","spring"));
        TopDocs topDocs = indexSearcher.search(query,10); //参数1: 查询对象 参数2: 查询结果返回的最大记录数
        System.out.println("总记录数: " + topDocs.totalHits);
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
     * 使用标准分析器
     */
    @Test
    public void testTokenStream1() throws Exception{
        Analyzer analyzer = new StandardAnalyzer();
        TokenStream tokenStream = analyzer.tokenStream("", "常见的结构化数据也就是数据库中的数据。在数据库中搜索很容易实现，通常都是使用sql语句进行查询，而且能很快的得到查询结果。\n" +
                "为什么数据库搜索很容易？\n" +
                "因为数据库中的数据存储是有规律的，有行有列而且数据格式、数据长度都是固定的。");
        CharTermAttribute charTermAttribute = tokenStream.addAttribute(CharTermAttribute.class);
        tokenStream.reset();
        while (tokenStream.incrementToken()){
            System.out.println(charTermAttribute.toString());
        }
        tokenStream.close();
    }

    /**
     * 使用中文分析器
     */
    @Test
    public void testTokenStream2() throws Exception{
        Analyzer analyzer = new IKAnalyzer();
        TokenStream tokenStream = analyzer.tokenStream("", "常见的结构化数据也就是数据库中的数据。在数据库中搜索很容易实现，通常都是使用sql语句进行查询，而且能很快的得到查询结果。\n" +
                "为什么数据库搜索很容易？\n" +
                "因为数据库中的数据存储是有规律的，有行有列而且数据格式、数据长度都是固定的。");
        CharTermAttribute charTermAttribute = tokenStream.addAttribute(CharTermAttribute.class);
        tokenStream.reset();
        while (tokenStream.incrementToken()){
            System.out.println(charTermAttribute.toString());
        }
        tokenStream.close();
    }
}






















