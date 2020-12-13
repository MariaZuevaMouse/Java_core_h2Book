import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.nio.file.Path;
import java.nio.file.Paths;

public class test {
    public static void main(String[] args) {
        File file = new File("");

        Path fs = Paths.get(".");
        System.out.println(fs.getFileSystem());
        System.out.println(fs.getFileSystem().supportedFileAttributeViews());


    }
}
