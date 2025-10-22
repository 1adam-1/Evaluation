package ma.projet.test;

import ma.projet.classes.Employe;
import ma.projet.classes.EmployeTache;
import ma.projet.classes.Projet;
import ma.projet.classes.Tache;
import ma.projet.service.EmployeService;
import ma.projet.service.EmployeTacheService;
import ma.projet.service.ProjetService;
import ma.projet.service.TacheService;
import ma.projet.util.HibernateUtil;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.text.SimpleDateFormat;

public class Test {
    public static void main(String[] args) {
        try {
            // Initialize Spring context
            AnnotationConfigApplicationContext context =
                    new AnnotationConfigApplicationContext(HibernateUtil.class);

            // Get services
            EmployeService employeService = context.getBean(EmployeService.class);
            ProjetService projetService = context.getBean(ProjetService.class);
            TacheService tacheService = context.getBean(TacheService.class);
            EmployeTacheService employeTacheService = context.getBean(EmployeTacheService.class);

            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

            // Create an employee
            Employe employe = new Employe("Dupont", "Jean", "0612345678");
            employeService.create(employe);

            // Create project "Gestion de stock" with date 14 Janvier 2013
            Projet projet = new Projet("Gestion de stock", sdf.parse("14/01/2013"), employe);
            projetService.create(projet);

            // Create tasks
            Tache tache1 = new Tache("Analyse", sdf.parse("01/02/2013"), sdf.parse("28/02/2013"), 1000, projet);
            Tache tache2 = new Tache("Conception", sdf.parse("01/03/2013"), sdf.parse("31/03/2013"), 1500, projet);
            Tache tache3 = new Tache("Développement", sdf.parse("01/04/2013"), sdf.parse("30/04/2013"), 3000, projet);

            tacheService.create(tache1);
            tacheService.create(tache2);
            tacheService.create(tache3);

            // Create EmployeTache with real dates
            EmployeTache et1 = new EmployeTache(employe, projet, tache1,
                    sdf.parse("10/02/2013"), sdf.parse("20/02/2013"));
            EmployeTache et2 = new EmployeTache(employe, projet, tache2,
                    sdf.parse("10/03/2013"), sdf.parse("15/03/2013"));
            EmployeTache et3 = new EmployeTache(employe, projet, tache3,
                    sdf.parse("10/04/2013"), sdf.parse("25/04/2013"));

            employeTacheService.create(et1);
            employeTacheService.create(et2);
            employeTacheService.create(et3);

            // Display the result
            projetService.afficherTachesReelles(projet.getId());

            context.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
