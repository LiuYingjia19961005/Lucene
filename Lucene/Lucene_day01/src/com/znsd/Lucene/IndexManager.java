package com.znsd.Lucene;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Term;
import org.apache.lucene.store.FSDirectory;
import org.junit.Before;
import org.junit.Test;
import org.wltea.analyzer.lucene.IKAnalyzer;

import java.io.File;
import java.io.IOException;

/**
 * 索引库维护
 */
public class IndexManager {
    /**
     * 添加文档
     * @throws Exception
     */
    private IndexWriter indexWriter;

    @Before
    public void init() throws IOException {
        indexWriter =
                new IndexWriter(FSDirectory.open(new File("E:\\LuceneSaveIndex").toPath()),
                        new IndexWriterConfig(new IKAnalyzer()));
    }

    @Test
    public void addDocument() throws Exception{

        Document document = new Document();

        document.add(new TextField("name","新建文件", Field.Store.YES));
        document.add(new TextField("content","新建文件内容", Field.Store.YES));
        document.add(new TextField("path","E:\\addDocument", Field.Store.YES));

        indexWriter.addDocument(document);
        indexWriter.close();
    }

    /**
     * 删除索引库全部文档
     */
    @Test
    public void delDocument() throws IOException {
        //删除全部文档
        indexWriter.deleteAll();
        indexWriter.close();
    }

    /**
     * 删除name属性中包含apache指定文档
     */
    @Test
    public void delDocumentByQuery() throws IOException {
        indexWriter.deleteDocuments(new Term("name","apache"));
        indexWriter.close();
    }

    /**
     * 修改文档
     *      修改原理: 先修改, 后添加
     */
    @Test
    public void updateDocument() throws IOException {
        Document document = new Document();
        document.add(new TextField("name","更新后的文档",Field.Store.YES));
        document.add(new TextField("name1","更新后的文档",Field.Store.YES));
        document.add(new TextField("name2","更新后的文档",Field.Store.YES));
        document.add(new TextField("name3","更新后的文档",Field.Store.YES));

        //更新操作
        indexWriter.updateDocument(new Term("name","spring"),document);
        indexWriter.close();
    }

    /**
     * Ran
     */
}






























