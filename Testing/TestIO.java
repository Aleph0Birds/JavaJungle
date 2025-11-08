import model.gameIO.SaveLoad;
import org.junit.Test;

import java.io.IOException;
import java.nio.file.Path;

public class TestIO {
    @Test
    public void testSave() {
        Path saveFolderPath = Path.of("../saves");
        System.out.println("Save folder path: " + saveFolderPath.toAbsolutePath());
    }

    @Test
    public void testList() {
        try {
            SaveLoad.initialize();
            for (String fileName : SaveLoad.getFileNames()) {
                System.out.println(fileName);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
