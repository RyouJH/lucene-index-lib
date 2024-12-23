package com.jhryu.index.impl;

import com.jhryu.index.Record;
import org.junit.Test;

import java.io.File;
import java.util.LinkedHashMap;
import java.util.Map;

public class LuceneIndexWriterTest {

    @Test
    public void write_test() throws Exception {
        File indexDir = new File("target/index");
        if (!indexDir.exists())
            indexDir.mkdirs();
        LuceneIndexWriter writer = new LuceneIndexWriter(indexDir);
        try {
            int BATCH_SIZE = 100000;
            for (int i = 0; i < BATCH_SIZE * 10; i++) {
                TestData testData = new TestData(i + "");
                testData.addField("agent_id", i % 3333 == 0 ? "GSI_02" : "GSI_01");
                testData.addField("channel_id", "CHANNEL-" + (i % 10));
                writer.upsert(testData);
                if (i > 0 && i % BATCH_SIZE == 0)
                    writer.commit();
            }
        } finally {
            writer.close();
        }
    }

    private static class TestData implements Record {
        private String key;
        private Map<String, Field> fieldMap = new LinkedHashMap<>();

        public TestData(String key) {
            this.key = key;
        }

        @Override
        public long getTimestamp() {
            return System.currentTimeMillis();
        }

        @Override
        public String getKey() {
            return key;
        }

        @Override
        public Map<String, Field> getIndexFields() {
            return fieldMap;
        }

        public void addField(String name, String value) {
            fieldMap.put(name, Field.of(value));
        }
    }

}