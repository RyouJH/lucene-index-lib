package com.jhryu.index;

import java.util.Map;

public interface Record {
    long getTimestamp();

    String getKey();

    Map<String, Field> getIndexFields();

    class Field {
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

    enum FieldType {
        NUMBER, STRING
    }
}
