package com.jhryu.index.impl;

import com.jhryu.index.IndexFields;
import com.jhryu.index.Record;
import com.jhryu.query.ast.IndexSearchQuery;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.*;
import org.apache.lucene.search.BooleanClause.Occur;
import org.apache.lucene.search.*;
import org.apache.lucene.search.SortField.Type;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.NIOFSDirectory;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public class LuceneIndexSearcher implements AutoCloseable, IndexFields {
    private Directory directory;
    private IndexReader reader;

    public LuceneIndexSearcher(File dir) throws Exception {
        Path p = dir.toPath();
        directory = NIOFSDirectory.open(p);
        reader = DirectoryReader.open(directory);
    }
    public List<String> search(IndexSearchQuery query) throws IOException {
        IndexSearcher searcher = new IndexSearcher(reader);
        IndexQueryBuilder queryBulder = new IndexQueryBuilder();
        query.accept(queryBulder);
        Query q = queryBulder.build();
        System.out.println(q);
        SortedNumericSortField sortField = new SortedNumericSortField(_time, Type.LONG, true);
        ScoreDoc[] hits = searcher.search(q, 1000, new Sort(sortField)).scoreDocs;
        List<String> keys = new ArrayList<>();
        for (ScoreDoc doc : hits) {
            Document d = searcher.doc(doc.doc);
            keys.add(d.get("key"));
        }
        return keys;
    }
    public void search(IndexSearchQuery query, Consumer<Record> consumer) throws IOException {
        IndexSearcher searcher = new IndexSearcher(reader);
        IndexQueryBuilder queryBulder = new IndexQueryBuilder();
        query.accept(queryBulder);
        Query q = queryBulder.build();
        searcher.search(q, new StreamingCollector() {
            @Override
            public void collect(int doc) {
                try {
                    Document document = searcher.doc(doc + docBase);
                    Record r = new Record() {
                        @Override
                        public long getTimestamp() {
                            Number time = document.getField(_time).numericValue();
                            return (long) time;
                        }

                        @Override
                        public String getKey() {
                            return document.get(key);
                        }

                        @Override
                        public Map<String, Field> getIndexFields() {
                            Map<String, Field> field = new LinkedHashMap<>();
                            for (IndexableField f : document.getFields()) {
                                field.put(f.name(), Field.of(f.stringValue()));
                            }
                            return field;
                        }
                    };
                    consumer.accept(r);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }

    @Override
    public void close() throws Exception {
        try {
            reader.close();
        } catch (Exception e) {

        }
        try {
            directory.close();
        } catch (Exception e) {

        }
    }

    private static class StreamingCollector extends TotalHitCountCollector {
        protected int docBase;

        @Override
        protected void doSetNextReader(LeafReaderContext context) throws IOException {
            docBase = context.docBase;
            super.doSetNextReader(context);
        }
    }
}
