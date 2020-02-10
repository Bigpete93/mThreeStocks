package org.mThree.Client.backup.src;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class FrontEnd {
    private static String webpage;

    public static void main(String[] args) throws FileNotFoundException, IOException {

        FileReader fr = new FileReader("seed_weekly");
        int j;
        String temp = "";
        StringBuilder contentBuilder = new StringBuilder();
//                while ((j=fr.read()) != -1)
//                    System.out.print((char) j);


        try (Stream<String> stream = Files.lines( Paths.get("seed_weekly"), StandardCharsets.UTF_8))
        {
            stream.forEach(s -> contentBuilder.append(s).append("\n"));
        }
        temp = contentBuilder.toString();
        temp = temp.replaceAll("\"", "");
        temp = temp.replaceAll("\\{", "");
        temp = temp.replaceAll("\n", "");
        temp = temp.replaceAll("\t", "");
        temp = temp.replaceAll(" ", "");
        String[] list = temp.split("},");
//                System.out.println("List: "+list[0]);
//                System.out.println("List: "+list[1]);
//                System.out.println("List: "+list[2]);
        String[] headersize = list[0].split(",");

        webpage = "";
        webpage += "<HTML>\n" +
                "<style>\n" +
                "  table.blueTable {\n" +
                "  border: 1px solid #1C6EA4;\n" +
                "  background-color: #EEEEEE;\n" +
                "  width: 100%;\n" +
                "  text-align: center;\n" +
                "  border-collapse: collapse;\n" +
                "}\n" +
                "table.blueTable td, table.blueTable th {\n" +
                "  border: 1px solid #AAAAAA;\n" +
                "  padding: 3px 2px;\n" +
                "}\n" +
                "table.blueTable tbody td {\n" +
                "  font-size: 13px;\n" +
                "}\n" +
                "table.blueTable tr:nth-child(even) {\n" +
                "  background: #D0E4F5;\n" +
                "}\n" +
                "table.blueTable thead {\n" +
                "  background: #1C6EA4;\n" +
                "  background: -moz-linear-gradient(top, #5592bb 0%, #327cad 66%, #1C6EA4 100%);\n" +
                "  background: -webkit-linear-gradient(top, #5592bb 0%, #327cad 66%, #1C6EA4 100%);\n" +
                "  background: linear-gradient(to bottom, #5592bb 0%, #327cad 66%, #1C6EA4 100%);\n" +
                "  border-bottom: 2px solid #444444;\n" +
                "}\n" +
                "table.blueTable thead th {\n" +
                "  font-size: 15px;\n" +
                "  font-weight: bold;\n" +
                "  color: #FFFFFF;\n" +
                "  text-align: center;\n" +
                "  border-left: 2px solid #D0E4F5;\n" +
                "}\n" +
                "table.blueTable thead th:first-child {\n" +
                "  border-left: none;\n" +
                "}\n" +
                "\n" +
                "table.blueTable tfoot {\n" +
                "  font-size: 14px;\n" +
                "  font-weight: bold;\n" +
                "  color: #FFFFFF;\n" +
                "  background: #D0E4F5;\n" +
                "  background: -moz-linear-gradient(top, #dcebf7 0%, #d4e6f6 66%, #D0E4F5 100%);\n" +
                "  background: -webkit-linear-gradient(top, #dcebf7 0%, #d4e6f6 66%, #D0E4F5 100%);\n" +
                "  background: linear-gradient(to bottom, #dcebf7 0%, #d4e6f6 66%, #D0E4F5 100%);\n" +
                "  border-top: 2px solid #444444;\n" +
                "}\n" +
                "table.blueTable tfoot td {\n" +
                "  font-size: 14px;\n" +
                "}\n" +
                "table.blueTable tfoot .links {\n" +
                "  text-align: right;\n" +
                "}\n" +
                "table.blueTable tfoot .links a{\n" +
                "  display: inline-block;\n" +
                "  background: #1C6EA4;\n" +
                "  color: #FFFFFF;\n" +
                "  padding: 2px 8px;\n" +
                "  border-radius: 5px;\n" +
                "}\n" +
                "</style>\n" +
                " <HEAD>\n" +
                " 	\n" +
                " </HEAD>\n" +
                " <BODY>\n" +
                "\n" +
                "<form>\n" +
                "	\n" +
                "	<button type=\"submit\" value=\"Submit\">Weekly</button>\n" +
                "    <button type=\"submit\" value=\"Submit\">5 Min</button>\n" +
                "	<button type=\"submit\" value=\"Submit\">15 Min</button>\n" +
                "</form>\n" +
                " 	<table class=\"blueTable\">\n" +
                "<thead>\n" +
                "<tr>\n";
        if (headersize.length ==0){
        }
        else if (headersize.length ==1){
            webpage +="<th>Date</th>\n" ;
        }else if(headersize.length ==2) {
            webpage +="<th>Date</th>\n" ;
            webpage +="<th>Open</th>\n" ;

        }else if(headersize.length ==3) {
            webpage +="<th>Date</th>\n" ;
            webpage +="<th>Open</th>\n" ;
            webpage +="<th>High</th>\n" ;

        }else if(headersize.length ==4) {
            webpage +="<th>Date</th>\n" ;
            webpage +="<th>Open</th>\n" ;
            webpage +="<th>High</th>\n" ;
            webpage +="<th>Low</th>\n" ;
        }
        else if(headersize.length ==5) {
            webpage +="<th>Date</th>\n" ;
            webpage +="<th>Open</th>\n" ;
            webpage +="<th>High</th>\n" ;
            webpage +="<th>Low</th>\n" ;
            webpage +="<th>Close</th>\n" ;
        }
        else if(headersize.length ==6) {
            webpage +="<th>Date</th>\n" ;
            webpage +="<th>Open</th>\n" ;
            webpage +="<th>High</th>\n" ;
            webpage +="<th>Low</th>\n" ;
            webpage +="<th>Close</th>\n" ;
            webpage +="<th>Volumes</th>\n" ;
        }
        if (headersize.length !=0){
            webpage +="</tr>\n";
        }

        webpage +="</thead>\n" +
                "<tfoot>\n" +
                "<tr>\n" +
                "<td colspan=\"6\">\n" +
                "<div class=\"links\"><a href=\"#\">&laquo;</a> <a class=\"active\" href=\"#\">1</a> <a href=\"#\">2</a> <a href=\"#\">3</a> <a href=\"#\">4</a> <a href=\"#\">&raquo;</a></div>\n" +
                "</td>\n" +
                "</tr>\n" +
                "</tfoot>\n" +
                "<tbody>";
        String s =  dummyGetDataByWeek("2020-1-30");
        String[] fields = s.split(",");
        assert fields.length == 5;
        for (int i =0; i <list.length;i++){
            webpage += " <tr>\n";
            String[] splitfields = list[i].split(",");
            for (int g=0; g < splitfields.length;g++){
                splitfields[g]=splitfields[g].replaceAll("\\}", "");
                splitfields[g]=splitfields[g].replaceAll(";", "");
//                         System.out.println("Length "+splitfields.length);
//                         System.out.println("G = "+g);
//                         System.out.println("splitfields: "+splitfields[g]);

                webpage +=        "                <td>" + splitfields[g] + "</td>\n";

            }    webpage +=  "                </tr>";
        }



//                webpage += "<TR>\n";
//                webpage += "<TD align=center>";
//                webpage += fields[0];
//                webpage += "</TD>";
//                webpage += "<TD align=center>";
//                webpage += fields[1];
//                webpage += "</TD>";
//                webpage += "<TD align=center>";
//                webpage += fields[2];
//                webpage += "</TD>";
//                webpage += "<TD align=center>";
//                webpage += fields[3];
//                webpage += "</TD>";
//                webpage += "<TD align=center>";
//                webpage += fields[4];
//                webpage += "</TD>";
//                webpage += "<TD align=center>";
//                webpage += fields[5];
//                webpage += "</TD>";
        webpage += "</TABLE>";
        webpage += "</BODY>";
        webpage += "</HTML>";
        //System.out.println(webpage);
        try {
            File file = new File("index.html");
            file.createNewFile();
            System.out.println("File: " + file);
            FileWriter fileWriter = new FileWriter(file);
            PrintWriter printWriter = new PrintWriter(fileWriter);
            printWriter.print(webpage);
            printWriter.close();
        } catch(Exception e) {
            e.printStackTrace();
        }


        System.exit(0);
    }
    private static String dummyGetDataByWeek(String s) {
        return "100.1234,200.1234,300.1234,400.1234,500, 179845938";
    }
}