package com.jhryu.index;

import com.jhryu.index.impl.LuceneIndexSearcher;
import org.junit.Test;

import java.io.File;
import java.util.LinkedHashMap;
import java.util.Map;

public class LuceneIndexSearcherTest {
    @Test
    public void test() {
        File indexDir = new File("target/index");
        try (LuceneIndexSearcher searcher = new LuceneIndexSearcher(indexDir)) {
            Map<String, String> q = new LinkedHashMap<>();
            q.put("agent_id","GSI_02");
            for (String key : searcher.search(q)) {
                System.out.println(key);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}