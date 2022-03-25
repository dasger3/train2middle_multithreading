package task5.service;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Objects;

public class FileService {

    public static String readFromFile(String fileName) throws FileNotFoundException {
        try {
            File file = new File(
                    Objects.requireNonNull(FileService.class.getClassLoader().getResource(fileName)).getFile());
            return FileUtils.readFileToString(file, "UTF-8");
        } catch (IOException | NullPointerException e) {
            throw new FileNotFoundException("Can`t open or something wrong with file named: " + fileName);
        }
    }

    /*public static void writeToFile(String content) throws IOException {
        File file = new File(OUTPUT_FILE);
        Files.write(content.getBytes(StandardCharsets.UTF_8), file);
    }*/
}
