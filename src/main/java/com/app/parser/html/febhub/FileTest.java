package com.app.parser.html.febhub;

import com.app.support.file.FlatFileWriter;

public class FileTest {

    public static void main(String[] args) throws Exception {

        FlatFileWriter fileWriter = new FlatFileWriter();
        fileWriter.setFilePath("src/main/java/com/app/output");
        fileWriter.setFileName("text.txt");
        fileWriter.open();
        fileWriter.writeLine("12313115");
        fileWriter.close();


    }
}
