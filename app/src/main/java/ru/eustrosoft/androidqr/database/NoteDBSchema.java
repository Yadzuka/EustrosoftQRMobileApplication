package ru.eustrosoft.androidqr.database;

public class NoteDBSchema {
    public static final class NoteTable {
        public static final String NAME = "notes";

        public static final class Cols {
            public static final String UUID = "uuid";
            public static final String DATE = "date";
            public static final String TIME = "time";
            public static final String TITLE = "title";
            public static final String TEXT = "text";
        }
    }
}
