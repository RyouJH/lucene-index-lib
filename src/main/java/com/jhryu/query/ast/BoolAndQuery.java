package com.jhryu.query.ast;

import com.jhryu.query.SearchQueryVisitor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BoolAndQuery implements IndexSearchQuery {
    public List<IndexSearchQuery> queries = new ArrayList<>();

    @Override
    public void accept(SearchQueryVisitor visitor) {
        visitor.visit(this);
    }

    public static BoolAndQuery of(IndexSearchQuery... queries) {
        BoolAndQuery andQuery = new BoolAndQuery();
        andQuery.queries.addAll(Arrays.asList(queries));
        return andQuery;
    }
}
