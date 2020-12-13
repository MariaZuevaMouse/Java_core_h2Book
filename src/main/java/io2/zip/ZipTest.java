package io2.zip;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Scanner;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class ZipTest {
    public static void main(String[] args) throws IOException {
        //String zipName = args[0];
        String zipName = "JAVA_documentation.zip";
        showContents(zipName);
        System.out.println("---");
        showContents2(zipName);
    }

    private static void showContents2(String zipName) throws IOException {
        FileSystem fs = FileSystems.newFileSystem(Paths.get(zipName), null);
        Files.walkFileTree(fs.getPath("/"), new SimpleFileVisitor<Path>(){
            @Override
            public FileVisitResult visitFile(Path path, BasicFileAttributes attrs) throws IOException {
                System.out.println(path);
                for(String line : Files.readAllLines(path, Charset.forName("UTF-8")))
                    System.out.println("   " + line);
                return FileVisitResult.CONTINUE;
            }
        });
    }

    private static void showContents (String zipName) throws IOException {
        try(final ZipInputStream zin = new ZipInputStream(new FileInputStream(zipName))){
            ZipEntry entry;
            while((entry = zin.getNextEntry()) != null){
                System.out.println(entry.getName());
                Scanner scanner = new Scanner(zin, String.valueOf(StandardCharsets.UTF_8));
                while (scanner.hasNextLine()){
                    System.out.println("   " + scanner.nextLine()); //do not close scanner
                }
                zin.closeEntry();
            }
        }
    }
}
