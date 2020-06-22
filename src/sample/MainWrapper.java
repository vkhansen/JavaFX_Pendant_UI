package sample;

import java.lang.reflect.Method;

public class MainWrapper {
    //This is wrapper class to enable embedded mode, virtual keyboard, and touch.

    public static void main(String[] args) throws Exception
    {  // application - package name
        Class<?> app = Class.forName("sample.Main"); //Wraps the main class
        Method main = app.getDeclaredMethod("main", String[].class);
        System.setProperty("com.sun.javafx.isEmbedded", "true");
        System.setProperty("com.sun.javafx.touch", "true"); //Enables touch screen
        System.setProperty("com.sun.javafx.virtualKeyboard", "javafx"); //Enables on screen keyboard
        Object[] arguments = new Object[]{args};
        main.invoke(null, arguments); // calls the main class with no arguments
    }
}
