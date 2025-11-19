import model.gameIO.SaveLoad;
import org.junit.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static model.gameIO.SaveLoad.savePath;

public class TestIO {
    @Test
    public void testSave() {
        Path saveFolderPath = Path.of("../saves");
        System.out.println("Save folder path: " + saveFolderPath.toAbsolutePath());
    }

    @Test
    public void testList() throws IOException {
        SaveLoad.initialize();

        var a = SaveLoad.getFileNames(true);
        for(var b : a) {
            System.out.println(b);
        }
    }
}
