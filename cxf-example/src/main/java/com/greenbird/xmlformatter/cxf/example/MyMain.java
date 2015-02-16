package com.greenbird.xmlformatter.cxf.example;

import org.apache.camel.spring.Main;

/**
 * A Main to let you easily start the application from a IDE.
 * Usually you can just right click and choose Run
 *
 * @version 
 */

public final class MyMain {

    public static void main(String[] args) throws Exception {
        Main main = new Main();
        main.setApplicationContextUri("META-INF/spring/camel-config.xml");
        main.start();
    }
}
