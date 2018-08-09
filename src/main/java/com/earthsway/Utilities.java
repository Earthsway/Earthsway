package com.earthsway;

import javax.swing.*;

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
}
