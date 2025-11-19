package model.gameIO;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class SaveLoad {
    public static Path savePath;
    public final static String recordSuffix = ".record";
    public final static String saveSuffix = ".sav";

    public static void initialize() throws IOException {
        savePath = Path.of("saves");
        if (!Files.exists(savePath) || !Files.isDirectory(savePath))
            Files.createDirectory(savePath);
    }

    public static String[] getFileNames(boolean isRecord) throws IOException {
        if (!Files.exists(savePath) || !Files.isDirectory(savePath))
            initialize();

        final String typeSuffix = isRecord ? recordSuffix : saveSuffix;

        return Files
                .list(savePath)
                .map(Path::getFileName)
                .map(Path::toString)
                .filter(s -> s.endsWith(typeSuffix))
                .toArray(String[]::new);
    }

    public static String getTimeString(boolean isRecord) {
        return DateTimeFormatter
                .ofPattern("MMdd-hhmmss")
                .format(LocalDateTime.now())
                + (isRecord ?  recordSuffix : saveSuffix);
    }

    public static PrintWriter getWriter(String fileName, boolean isRecord) throws IOException {
        final String typeSuffix = isRecord ? recordSuffix : saveSuffix;
        if (!fileName.endsWith(typeSuffix))
            fileName += typeSuffix;
        return new PrintWriter(
                new FileWriter(Path.of(savePath.toString(), fileName).toFile())
        );
    }

    public static BufferedReader getReader(String name, boolean isRecord) throws IOException {
        final String typeSuffix = isRecord ? recordSuffix : saveSuffix;
        if (!name.endsWith(typeSuffix)) name += typeSuffix;
        return new BufferedReader(new FileReader(Path.of(savePath.toString(), name).toFile()));
    }
}
