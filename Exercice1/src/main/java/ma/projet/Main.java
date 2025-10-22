package ma.projet;

import ma.projet.util.HibernateUtil;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class Main {

    public static void main(String[] args) {
        // Initialize Spring ApplicationContext using HibernateUtil (active config)
        ApplicationContext context = new AnnotationConfigApplicationContext(HibernateUtil.class);

        System.out.println("Spring Application Context initialized successfully!");

        // Close the context
        ((AnnotationConfigApplicationContext) context).close();
    }
}