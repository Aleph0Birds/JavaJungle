package model.gameIO;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class SaveLoad {
    public static Path savePath;

    public static void initialize() throws IOException {
        savePath = Path.of("saves");
        if (!Files.exists(savePath) || !Files.isDirectory(savePath))
            Files.createDirectory(savePath);
    }

    public static String[] getFileNames() throws IOException {
        if (!Files.exists(savePath) || !Files.isDirectory(savePath))
            return null;

        return Files
                .list(savePath)
                .map(Path::toString)
                .filter(s -> s.endsWith(".record"))
                .map(str -> str.split("\\\\")[1])
                .toArray(String[]::new);
    }

    public static PrintWriter getFileWriter() throws IOException {
        String fileName = DateTimeFormatter.ofPattern("MMdd-ss").format(LocalDateTime.now());
        return new PrintWriter(
                new FileWriter(Path.of(savePath.toString(), fileName + ".recording").toFile())
        );
    }

    public static BufferedReader getFileReader(String name) throws IOException {
        return new BufferedReader(new FileReader(Path.of(savePath.toString(), name + ".recording").toFile()));
    }
}
