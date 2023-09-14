package org.sk.pdfreader.data;


import java.io.File;

public class Database {
    private static final Database _instance =new Database();

    public static Database getInstance(){
        return _instance;
    }

    public static final String DB_FOLDER ="makidb";
    private Database(){
        System.out.println("Starting database");
        File database=new File(DB_FOLDER);
        if(!database.exists()) {
            System.out.println("Database folder not found");
            System.out.println("Creating database folder");
            boolean isCreated = database.mkdir();
        }


    }

}
