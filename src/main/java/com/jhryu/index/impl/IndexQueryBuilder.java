package com.jhryu.index.impl;

import com.jhryu.query.SearchQueryVisitor;
import com.jhryu.query.ast.*;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.BooleanClause.Occur;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.BooleanQuery.Builder;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.WildcardQuery;

public class IndexQueryBuilder implements SearchQueryVisitor {
    private Query query;

    public IndexQueryBuilder() {
    }

    @Override
    public void visit(FieldMatchQuery query) {
        this.query = new TermQuery(new Term(query.field, query.value));

    }


    @Override
    public void visit(FieldNotEqualQuery query) {
        this.query = new BooleanQuery.Builder()
                .add(new WildcardQuery(new Term(query.field, "*")), Occur.MUST)
                .add(new TermQuery(new Term(query.field, query.value)), Occur.MUST_NOT).build();
    }

    @Override
    public void visit(BoolAndQuery query) {
        Builder builder = new Builder();
        for (IndexSearchQuery child : query.queries) {
            child.accept(this);
            builder.add(this.query, Occur.MUST);
        }
        this.query = builder.build();
    }

    @Override
    public void visit(BoolOrQuery query) {
        Builder builder = new Builder();
        for (IndexSearchQuery child : query.queries) {
            child.accept(this);
            builder.add(this.query, Occur.SHOULD);
        }
        this.query = builder.build();
    }


    public Query build() {
        return query;
    }
}
