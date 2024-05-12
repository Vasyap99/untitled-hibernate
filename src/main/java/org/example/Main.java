package org.example;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration; //


import org.jsoup.Jsoup;

import java.io.File;

//import org.w3c.dom.*;

import org.jsoup.nodes.*;
import org.jsoup.select.Elements;

import java.io.FileOutputStream;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.List;

// Press Shift twice to open the Search Everywhere dialog and type `show whitespaces`,
// then press Enter. You can now see whitespace characters in your code.
public class Main {
    boolean save(String url,String i){
        try{
            byte[]a=new URL(url).openStream().readAllBytes();
            FileOutputStream fos=new FileOutputStream((i)+".html");
            fos.write(a);
            return true;
        }catch(Exception e){
            return false;
        }
    }

    public static void main(String[] args) {
        // Press Alt+Enter with your caret at the highlighted text to see how
        // IntelliJ IDEA suggests fixing it.
        /////////////////////
        SessionFactory sessionFactory = new Configuration()
                .configure("hibernate.cfg.xml")
                //
                .addPackage("org.example")
                .addAnnotatedClass(dwnl_link.class)
                .buildSessionFactory();/**/

        /*StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
                .configure() // configures settings from hibernate.cfg.xml
                .build();
            SessionFactory sessionFactory = new MetadataSources(registry)
                    .addAnnotatedClass(org.example.Monter.class)
                    .buildMetadata()
                    .buildSessionFactory();*/

        try {
            //Session session = sessionFactory.openSession();
            //session.close();
        } catch (Exception e) {
        }
        /////////////////////


        Main en = new Main();

        //File input = new File("/spring/");
        try {
            Session session = sessionFactory.openSession();

            //Document doc = (Document) Jsoup.parse(input, "UTF-8", "https://www.geeksforgeeks.org/");
            byte[] a = new URL("https://www.geeksforgeeks.org/java/").openStream().readAllBytes();
            String s = new String(a, StandardCharsets.UTF_8.name());

            String[] ar = s.split("16 Apr, 2023");

            ar = ar[1].split("Similar Reads");

            s = ar[0];

            Document doc = Jsoup.parse(s);

            List<dwnl_link> ll = session.createQuery("from org.example.dwnl_link", dwnl_link.class).list();

            //Element content = doc.getElementById("content");
            Elements links = doc.getElementsByTag("a");
            int i = 0;
            for (Element link : links) {
                String linkHref = link.attr("href");
                String linkText = link.text();
                System.out.println("> " + linkHref);
                try {
                    boolean found = false;

                    for (var ii : ll) {
                        if (ii.url.equals(linkHref)) {
                            found = true;
                            break;
                        }
                    }

                    if (!found) {
                        Transaction transaction = session.beginTransaction();
                        dwnl_link mn = new dwnl_link(linkHref);
                        session.save(mn);
                        transaction.commit();
                    }
                } catch (Exception e) {
                }
                ////
                if (linkHref.toLowerCase().contains("https://")) {
                    try {
                        String nn = linkHref.replace('/', '_').replace(':', '-');
                        System.out.println(nn);
                        en.save(linkHref, "c:/__kko/java tutorial/" + nn + ".html");
                        Thread.sleep(1000);
                        Transaction transaction = session.beginTransaction();
                        session.createQuery(String.format("Update dwnl_link set downl = true where url = '%d'", linkHref));
                        transaction.commit();
                    } catch (Exception e1) {
                    }
                }
                /**///

                i++;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


    }
}