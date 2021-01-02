package tools;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

// Dosya yazma işlemlerini düzenleyen class yapısı
public class FileMaker {
    // Verilen klasör ve dosya adındaki dosyaı açıp verilen texti önceki textin sonuna ekleyen fonksiyon
    public static void addFile(String folderName,String fileName,String text) {
        try {
            File file = new File(folderName+"/"+fileName+".txt");
            FileWriter fr = new FileWriter(file, true);
            BufferedWriter br = new BufferedWriter(fr);
            br.write(text);
            br.write("\n");
            br.close();
            fr.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    // verilen isimdeki klasörü ana dizinde oluşturan fonksiyon
    public static boolean generateFolders(String folderName){
        File file = new File(folderName);
        if(file.mkdir()){
            System.out.println(folderName+ " folder created");
            return true;
        }
        else{
            System.out.println(folderName+ " already exist.");
            return false;
        }
    }
    // verilen isimdeki klasörü ve içindeki dosyaları silen fonksiyon
    public static void deleteDirectory(String folderName) {
        File directoryToBeDeleted = new File(folderName);
        File[] allContents = directoryToBeDeleted.listFiles();
        if (allContents != null) {
            for (File file : allContents) {
                if (file.delete()) {
                    System.out.println("Deleted the file: " + file.getName());
                } else {
                    System.out.println("Failed to delete the file.");
                }
            }
        }
        if (directoryToBeDeleted.delete()) {
            System.out.println("Deleted the folder: " + directoryToBeDeleted.getName());
        } else {
            System.out.println("Failed to delete the file.");
        }
    }
}
