package com.jhryu.index;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public abstract class Record {
    public abstract long getTimestamp();

    public abstract String getKey();

    public abstract Map<String, Field> getIndexFields();

    public Set<String> storeFieldSet() {
        return new HashSet<>();
    }

    public static class Field {
        public FieldType type;
        public String value;

        public String asString() {
            return value;
        }

        public long asLong() {
            if (type == FieldType.NUMBER)
                return Long.parseLong(value);
            else
                throw new IllegalStateException();
        }

        public static Field of(String value) {
            Field field = new Field();
            field.type = FieldType.STRING;
            field.value = value;
            return field;
        }

        public static Field of(Number value) {
            Field field = new Field();
            field.type = FieldType.NUMBER;
            field.value = value + "";
            return field;
        }

        @Override
        public String toString() {
            return "Field{" +
                    "value='" + value + '\'' +
                    '}';
        }
    }

    public static enum FieldType {
        NUMBER, STRING
    }
}
