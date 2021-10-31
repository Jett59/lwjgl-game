package app.cleancode.resources;

import java.io.BufferedInputStream;
import java.io.InputStream;

public class ResourceReader {
    public static byte[] readResource(String path) throws Exception {
        try (InputStream input = ResourceReader.class.getResourceAsStream(path);
                BufferedInputStream bufferedInput = new BufferedInputStream(input)) {
            return bufferedInput.readAllBytes();
        }
    }
}
