package app.cleancode.gl;

import java.util.ArrayList;
import java.util.List;

public class GlContext implements AutoCloseable {
    private final List<AutoCloseable> objects;

    public GlContext() {
        objects = new ArrayList<>();
    }

    public <T extends AutoCloseable> T addObject(T object) {
        objects.add(object);
        return object;
    }

    @Override
    public void close() throws Exception {
        objects.forEach(object -> {
            try {
                object.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

}
