package com.earthsway;

import com.sun.istack.internal.NotNull;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.awt.image.ColorModel;
import java.io.IOException;
import java.nio.Buffer;

public class Utilities {
    public static void errorReport(Exception e, Class clas){
        JOptionPane.showMessageDialog(null, "Class Path: " + clas.getName() +
                "\n\nLine: " + e.getStackTrace()[0].getClassName().substring(e.getStackTrace()[0].getClassName().lastIndexOf(".") + 1) + " : " + e.getStackTrace()[0].getMethodName() + " : " + e.getStackTrace()[0].getLineNumber() +
                "\n\nError Info: " + e.getMessage(), "An Error Has Occurred", JOptionPane.ERROR_MESSAGE);
    }

    public static Thread getThreadByName(String threadName) {
        for (Thread t : Thread.getAllStackTraces().keySet()) {
            if (t.getName().equals(threadName)) return t;
        }
        return null;
    }

    public static BufferedImage loadTexture(String name){
        try {
            return ImageIO.read(Utilities.class.getResource("/textures/" + name + ".png"));
        } catch (IOException e) {
            e.printStackTrace();
            errorReport(e, Utilities.class);
            System.exit(1);
        }
        return null;
    }

    public static BufferedImage scaleNearest(BufferedImage before, double scale) {
        final int interpolation = AffineTransformOp.TYPE_NEAREST_NEIGHBOR;
        return scale(before, scale, interpolation);
    }

    @NotNull
    private static
    BufferedImage scale(final BufferedImage before, final double scale, final int type) {
        int w = before.getWidth();
        int h = before.getHeight();
        int w2 = (int) (w * scale);
        int h2 = (int) (h * scale);
        BufferedImage after = new BufferedImage(w2, h2, before.getType());
        AffineTransform scaleInstance = AffineTransform.getScaleInstance(scale, scale);
        AffineTransformOp scaleOp = new AffineTransformOp(scaleInstance, type);
        scaleOp.filter(before, after);
        return after;
    }
}
