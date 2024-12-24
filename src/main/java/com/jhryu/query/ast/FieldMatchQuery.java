package com.jhryu.query.ast;

import com.jhryu.query.SearchQueryVisitor;

public class FieldMatchQuery implements IndexSearchQuery {
    public String field;
    public String value;

    @Override
    public void accept(SearchQueryVisitor visitor) {
        visitor.visit(this);
    }

    public static FieldMatchQuery of(String field, String value) {
        FieldMatchQuery fieldMatchQuery = new FieldMatchQuery();
        fieldMatchQuery.field = field;
        fieldMatchQuery.value = value;
        return fieldMatchQuery;
    }
}
