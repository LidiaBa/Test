package org.example;

import org.apache.catalina.Context;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.WebResourceRoot;
//import org.apache.catalina.startup.ContextConfig;
import org.apache.catalina.startup.Tomcat;
import org.apache.catalina.webresources.DirResourceSet;
import org.apache.catalina.webresources.StandardRoot;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.repository.DatabaseConnection;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.sql.Connection;


public class Main {
    private static final Logger log = LogManager.getLogger(Main.class);

    public static void main(String[] args) {
        // Настройка Tomcat
        Tomcat tomcat = new Tomcat();
        tomcat.setPort(8081);
        tomcat.setBaseDir(createTempDir("temp"));
        tomcat.getConnector();
        String contextPath = "";
        // String docBase = new File(".").getAbsolutePath();
        String webappDir = new File("src/main/webapp/").getAbsolutePath();
        Context ctx = tomcat.addWebapp(contextPath, webappDir);
        // будем искать все сервелеты отталкиваясь от нашего мейн класса, все грубже пакета org.example;
       // ctx.addLifecycleListener(new ContextConfig());
       // ctx.setParentClassLoader(Main.class.getClassLoader());

        // Tomcat.addServlet(ctx, "myServelet", new HelloController());
        // ctx.addServletMappingDecoded("/hello", "myServelet");

        File additionWebInfClasses = new File("target/classes");
        WebResourceRoot resources = new StandardRoot(ctx);
        resources.addPreResources(new DirResourceSet(resources, "/WEB-INF/classes", additionWebInfClasses.getAbsolutePath(), "/"));
        ctx.setResources(resources);

        ctx.addServletContainerInitializer(
                ( c, context) -> {
                    context.addServlet("jsp", "org.apache.jasper.servlet.JspServlet");
                },
                 null
        );


        try {
            log.info("start tomcat");
            tomcat.start();
            // блокируем основной поток
            // ждем пока серсер не скажет остановиться
            tomcat.getServer().await();

        } catch (LifecycleException ex) {
            ex.printStackTrace();
        }
        log.info("stop tomcat");
    }

    private static String createTempDir(String prefix) {
        try{
            File tempDir = Files.createTempDirectory(prefix).toFile();
            tempDir.deleteOnExit();
            return tempDir.getAbsolutePath();
        } catch (IOException e) {
            throw new RuntimeException("Не удалось создать временную директорию", e);
        }

    }
}
