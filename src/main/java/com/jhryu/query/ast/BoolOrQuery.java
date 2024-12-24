package com.jhryu.query.ast;

import com.jhryu.query.SearchQueryVisitor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BoolOrQuery implements IndexSearchQuery {
    public List<IndexSearchQuery> queries = new ArrayList<>();

    @Override
    public void accept(SearchQueryVisitor visitor) {
        visitor.visit(this);
    }

    public static BoolOrQuery of(IndexSearchQuery... queries) {
        BoolOrQuery andQuery = new BoolOrQuery();
        andQuery.queries.addAll(Arrays.asList(queries));
        return andQuery;
    }
}
