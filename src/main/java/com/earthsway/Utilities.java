package com.earthsway;

import com.earthsway.game.gfx.SpriteSheet;

import javax.swing.*;
import java.io.IOException;

public class Utilities {
    public static void errorReport(Exception e, Class clas){
        JOptionPane.showMessageDialog(null, "In Class: " + clas.getName() + "\n Full Error:\n" + e.getMessage(), "An Error Has Occurred", JOptionPane.ERROR_MESSAGE);
    }
}
