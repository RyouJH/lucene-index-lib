package com.jhryu.index;

import com.jhryu.index.impl.LuceneIndexSearcher;
import com.jhryu.query.ast.FieldMatchQuery;
import com.jhryu.query.ast.FieldNotEqualQuery;
import org.junit.Test;

import java.io.File;
import java.util.LinkedHashMap;
import java.util.Map;

public class LuceneIndexSearcherTest {
    @Test
    public void test() {
        File indexDir = new File("target/index");
        try (LuceneIndexSearcher searcher = new LuceneIndexSearcher(indexDir)) {
            for (String key : searcher.search(FieldNotEqualQuery.of("agent_id","GSI_02"))) {
                System.out.println(key);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}