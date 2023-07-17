package ru.eustrosoft.androidqr.database;

public class ProfileDBSchema {
    public static final class ProfileTable {
        public static final String NAME = "profiles";

        public static final class Cols {
            public static final String UUID = "uuid";
            public static final String DATE = "date";
            public static final String NAME = "name";
            public static final String PASSWORD = "password";
        }
    }
}
