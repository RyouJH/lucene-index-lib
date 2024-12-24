package com.jhryu.index.impl;

import com.jhryu.query.ast.*;
import org.apache.lucene.search.Query;
import org.junit.Test;

public class IndexQueryBuilderTest {
    @Test
    public void test() {
        IndexSearchQuery query =
                BoolAndQuery.of(FieldMatchQuery.of("agent_id", "Test"),
                        FieldMatchQuery.of("channel_id","Channel-0"),
                        BoolOrQuery.of(FieldMatchQuery.of("a", "b"), FieldNotEqualQuery.of("b", "c")));

        IndexQueryBuilder builder = new IndexQueryBuilder();
        query.accept(builder);

        Query q = builder.build();
        System.out.println(q);
    }
}