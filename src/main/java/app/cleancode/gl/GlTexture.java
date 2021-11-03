package app.cleancode.gl;

import java.io.InputStream;
import java.nio.ByteBuffer;
import org.lwjgl.opengl.GL30;
import app.cleancode.resources.ResourceReader;
import de.matthiasmann.twl.utils.PNGDecoder;
import de.matthiasmann.twl.utils.PNGDecoder.Format;

public class GlTexture {
    private int id = 0;

    public GlTexture(String name) {
        try (InputStream input =
                ResourceReader.getInputStream(String.format("/textures/%s.png", name))) {
            PNGDecoder decoder = new PNGDecoder(input);
            ByteBuffer texture =
                    ByteBuffer.allocateDirect(4 * decoder.getWidth() * decoder.getHeight());
            decoder.decode(texture, decoder.getWidth() * 4, Format.RGBA);
            texture.flip();
            id = GL30.glGenTextures();
            GL30.glBindTexture(GL30.GL_TEXTURE_2D, id);
            GL30.glPixelStorei(GL30.GL_UNPACK_ALIGNMENT, 1);
            GL30.glTexImage2D(GL30.GL_TEXTURE_2D, 0, GL30.GL_RGBA, decoder.getWidth(),
                    decoder.getHeight(), 0, GL30.GL_RGBA, GL30.GL_UNSIGNED_BYTE, texture);
            GL30.glGenerateMipmap(GL30.GL_TEXTURE_2D);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected void bind() {
        GL30.glActiveTexture(0);
        GL30.glBindTexture(GL30.GL_TEXTURE_2D, id);
    }

    public void cleanup() {
        GL30.glDeleteTextures(id);
    }
}
