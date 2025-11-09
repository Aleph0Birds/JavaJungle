package model.gameIO;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class SaveLoad {
    public static Path savePath;
    public final String recordSuffix = ".record";
    public final String saveSuffix = ".sav";

    public static void initialize() throws IOException {
        savePath = Path.of("saves");
        if (!Files.exists(savePath) || !Files.isDirectory(savePath))
            Files.createDirectory(savePath);
    }

    public static String[] getFileNames(boolean isRecord) throws IOException {
        if (!Files.exists(savePath) || !Files.isDirectory(savePath))
            initialize();

        final String typeSuffix = isRecord ? ".record" : ".sav";

        return Files
                .list(savePath)
                .map(Path::toString)
                .filter(s -> s.endsWith(typeSuffix))
                .map(str -> str.split("\\\\")[1])
                .toArray(String[]::new);
    }

    public static PrintWriter getFileWriter(boolean isRecord) throws IOException {
        String fileName = DateTimeFormatter.ofPattern("MMdd-ss").format(LocalDateTime.now());
        final String typeSuffix = isRecord ? ".record" : ".sav";
        if (!fileName.endsWith(typeSuffix))
            fileName += typeSuffix;
        return new PrintWriter(
                new FileWriter(Path.of(savePath.toString(), fileName).toFile())
        );
    }

    public static BufferedReader getFileReader(String name, boolean isRecord) throws IOException {
        final String typeSuffix = isRecord ? ".record" : ".sav";
        if (!name.endsWith(typeSuffix)) name += typeSuffix;
        return new BufferedReader(new FileReader(Path.of(savePath.toString(), name).toFile()));
    }
}
