package uk.ac.newcastle.redhat.gavgraph.common.pom.csv;

import com.opencsv.CSVReader;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ReadAndWriteCsv {
    //static String filePath = "src\\main\\resources\\static\\xml\\test0.csv";
    //static String newPath = "src\\main\\resources\\static\\xml\\test00.csv";

    public static void main(String[] args) throws IOException {
        File file1 = new File("D:\\dev\\neo4j_data\\neo4jDatabases\\database-7665b2bb-35ee-4797-8011-6068d7c8edd4\\installation-3.5.14\\import\\gav_data2.csv");
        File file2 = new File("D:\\dev\\neo4j_data\\neo4jDatabases\\database-7665b2bb-35ee-4797-8011-6068d7c8edd4\\installation-3.5.14\\import\\gav_data1.csv");
        List<String[]> read1 = readToListArray(file1);
        List<String[]> read2 = readToListArray(file2);
        List<String[]> read3 = new ArrayList<>();
        read1.forEach(read3::add);
        read2.forEach(read3::add);
        List<String[]> filter = filter(read3);
        write(filter);
    }

    private static void write(List<String[]> filter) throws IOException {
        //FileWriter writer = new FileWriter("D:\\dev\\neo4j_data\\neo4jDatabases\\database-7665b2bb-35ee-4797-8011-6068d7c8edd4\\installation-3.5.14\\import\\gav_data22.csv");
        FileWriter writer = new FileWriter("D:\\dev\\neo4j_data\\neo4jDatabases\\database-7665b2bb-35ee-4797-8011-6068d7c8edd4\\installation-3.5.14\\import\\gav_data00.csv");
        CSVPrinter printer = CSVFormat.EXCEL.print(writer);
        String[] header = {"combination","groupId","artifactId","version"};
        printer.printRecord(header);
        for (String[] arr: filter) {
            printer.printRecord(arr);
        }
        printer.flush();
        printer.close();
    }

    private static List<String[]> filter(List<String[]> listArray) {//1393857
        List<String[]> nc = new ArrayList<>();//1389672
        Set<String> set = new HashSet<>();
        //Traverse the list of Arrays
        for (String[] arr: listArray) {
            String s = arr[0];
            if (set.add(s)){
                nc.add(arr);
            }
        }
        return nc;
    }

    public static List<String[]> readToListArray(File file){
        List<String[]> records = new ArrayList<>();
        try (CSVReader csvReader = new CSVReader(new FileReader(file));) {
            String[] values = null;
            while ((values = csvReader.readNext()) != null) {
                records.add(values);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return records;
    }
}
