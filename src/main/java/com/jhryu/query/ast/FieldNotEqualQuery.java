package com.jhryu.query.ast;

import com.jhryu.query.SearchQueryVisitor;

public class FieldNotEqualQuery implements IndexSearchQuery {
    public String field;
    public String value;

    @Override
    public void accept(SearchQueryVisitor visitor) {
        visitor.visit(this);
    }

    public static FieldNotEqualQuery of(String field, String value) {
        FieldNotEqualQuery fieldMatchQuery = new FieldNotEqualQuery();
        fieldMatchQuery.field = field;
        fieldMatchQuery.value = value;
        return fieldMatchQuery;
    }
}
