package com.earthsway;

import com.earthsway.game.gfx.SpriteSheet;

import javax.swing.*;
import java.io.IOException;
import java.util.Arrays;

public class Utilities {
    public static void errorReport(Exception e, Class clas){
        JOptionPane.showMessageDialog(null, "Class Path: " + clas.getName() +
                "\n\nLine: " + e.getStackTrace()[0].getClassName().substring(e.getStackTrace()[0].getClassName().lastIndexOf(".") + 1) + " : " + e.getStackTrace()[0].getMethodName() + " : " + e.getStackTrace()[0].getLineNumber() +
                "\n\nError Info: " + e.getMessage(), "An Error Has Occurred", JOptionPane.ERROR_MESSAGE);
    }
}
