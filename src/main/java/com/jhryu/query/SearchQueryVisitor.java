package com.jhryu.query;

import com.jhryu.query.ast.BoolAndQuery;
import com.jhryu.query.ast.BoolOrQuery;
import com.jhryu.query.ast.FieldMatchQuery;
import com.jhryu.query.ast.FieldNotEqualQuery;

public interface SearchQueryVisitor {
    void visit(FieldMatchQuery query);

    void visit(BoolAndQuery query);

    void visit(BoolOrQuery query);

    void visit(FieldNotEqualQuery query);
}
