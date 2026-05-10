package org.example;

import org.apache.catalina.Context;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.startup.Tomcat;
import lombok.extern.java.Log;

import java.io.File;
import java.util.logging.Level;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
        // Настройка Tomcat
        Tomcat tomcat = new Tomcat();
        tomcat.setPort(8080);
        tomcat.setBaseDir("temp");
        String contextPath = "/";
        String docBase = new File(".").getAbsolutePath();
        Context ctx = tomcat.addContext(contextPath, docBase);
        //Tomcat.addServelet(ctx, "myServelet", new MyServelet());
        //ctx.addServeletMappingdecoded("/hello", "myServelet");
        try {
            System.out.println("Запуск TomCat на порту 8080");
            tomcat.start();
            // блокируем основной поток
            // ждем пока серсер не скажет остановиться
            tomcat.getServer().await();

        } catch (LifecycleException ex) {
            ex.printStackTrace();
        }
    }
}
