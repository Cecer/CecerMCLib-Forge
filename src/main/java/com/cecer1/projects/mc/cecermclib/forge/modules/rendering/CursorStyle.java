package com.cecer1.projects.mc.cecermclib.forge.modules.rendering;

import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.IResource;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.BufferUtils;
import org.lwjgl.LWJGLException;
import org.lwjgl.input.Cursor;
import org.lwjgl.input.Mouse;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.file.Files;

public class CursorStyle {

    public static void setCursor(Cursor cursorIn) {
        try {
            Mouse.setNativeCursor(cursorIn);
        } catch (LWJGLException e) { e.printStackTrace(); }
    }

    public static Cursor createCursorFromResourceLocation(ResourceLocation locIn) {
        try {
            IResource resource = Minecraft.getMinecraft().getResourceManager().getResource(locIn);
            InputStream stream = resource.getInputStream();
            BufferedImage image = ImageIO.read(stream);
            return createCursor(image);
        } catch (Exception e) { e.printStackTrace(); }
        return null;
    }

    public static Cursor createCursorFromFile(File fileIn) {
        try {
            InputStream stream = Files.newInputStream(fileIn.toPath());
            BufferedImage image = ImageIO.read(stream);
            return createCursor(image);
        } catch (Exception e) { e.printStackTrace(); }
        return null;
    }

    private static Cursor createCursor(BufferedImage imageIn) {
        try {
            int width = imageIn.getWidth();
            int height = imageIn.getHeight();

            int[] pixels = imageIn.getRGB(0, 0, width, height, null, 0, width);

            ByteBuffer buffer = BufferUtils.createByteBuffer(width * height * 4);
            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    int pixel = pixels[y * width + x];

                    buffer.put((byte) ((pixel >> 16) & 0xFF));
                    buffer.put((byte) ((pixel >> 8) & 0xFF));
                    buffer.put((byte) (pixel & 0xFF));
                    buffer.put((byte) ((pixel >> 24) & 0xFF));
                }
            }
            buffer.flip();

            // TODO: Implement cursor texture metadata for animated and off centre textures
            return new Cursor(width, height, width / 2, height / 2, 1, buffer.asIntBuffer(), null);
        } catch (Exception e) { e.printStackTrace(); }
        return null;
    }
}
