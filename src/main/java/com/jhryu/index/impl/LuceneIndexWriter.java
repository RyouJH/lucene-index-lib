package com.jhryu.index.impl;

import com.jhryu.index.IndexFields;
import com.jhryu.index.Record;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field.Store;
import org.apache.lucene.document.LongPoint;
import org.apache.lucene.document.SortedNumericDocValuesField;
import org.apache.lucene.document.StringField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Term;
import org.apache.lucene.index.TieredMergePolicy;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.NIOFSDirectory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;

public class LuceneIndexWriter implements IndexFields {
    private Logger logger = LoggerFactory.getLogger(LuceneIndexWriter.class);

    public LuceneIndexWriter(File indexDir) throws IOException {
        this.directory = NIOFSDirectory.open(indexDir.toPath());
    }

    private Directory directory;
    private Analyzer analyzer = new StandardAnalyzer();
    private IndexWriterConfig config;
    private IndexWriter writer;


    private synchronized IndexWriter getWriter() throws IOException {
        if (writer == null || !writer.isOpen())
            createWriter();
        return writer;
    }

    private void createWriter() throws IOException {
        config = new IndexWriterConfig(analyzer);
        config.setOpenMode(IndexWriterConfig.OpenMode.CREATE_OR_APPEND);
        config.setRAMBufferSizeMB(320);
        TieredMergePolicy policy = new TieredMergePolicy();
        policy.setMaxMergeAtOnce(40);
        policy.setSegmentsPerTier(40);
        config.setMergePolicy(policy);
        writer = new IndexWriter(directory, config);
        writer.commit();
    }

    public void upsert(Record record) throws Exception {
        Document document = new Document();
        long timestamp = record.getTimestamp();
        document.add(new LongPoint(_time, timestamp));
        document.add(new SortedNumericDocValuesField(_time, timestamp));
        record.getIndexFields().forEach((k, v) -> {
            switch (v.type) {
                case STRING: {
                    StringField sf = new StringField(k, v.asString(), Store.NO);
                    document.add(sf);
                    break;
                }
                case NUMBER: {
                    document.add(new LongPoint(k, v.asLong()));
                    document.add(new SortedNumericDocValuesField(k, v.asLong()));
                    break;
                }
            }
        });
        document.add(new StringField(key, record.getKey(), Store.YES));
        getWriter().updateDocument(new Term(key, record.getKey()), document);
    }

    public void commit() throws IOException {
        getWriter().commit();
        logger.info("commit...");
    }

    public synchronized void close() throws Exception {
        this.commit();
        writer.close();
        directory.close();
    }
}
