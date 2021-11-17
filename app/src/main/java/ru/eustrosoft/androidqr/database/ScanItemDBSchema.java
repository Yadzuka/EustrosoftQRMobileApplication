package ru.eustrosoft.androidqr.database;

public class ScanItemDBSchema {
    public static final class ScanItemTable {
        public static final String NAME = "scanning_history";

        public static final class Cols {
            public static final String UUID = "uuid";
            public static final String DATE = "date";
            public static final String TIME = "time";
            public static final String TEXT = "text";
        }
    }
}
