package com.jhryu.query.ast;

import com.jhryu.query.SearchQueryVisitor;

public interface IndexSearchQuery {
    void accept(SearchQueryVisitor visitor);
}
