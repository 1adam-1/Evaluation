package ma.projet.test;

import ma.projet.classes.Categorie;
import ma.projet.classes.Commande;
import ma.projet.classes.LigneCommandeProduit;
import ma.projet.classes.Produit;
import ma.projet.util.HibernateUtil;
import ma.projet.service.CategorieService;
import ma.projet.service.CommandeService;
import ma.projet.service.LigneCommandeService;
import ma.projet.service.ProduitService;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Test {

    public static void main(String[] args) {
        ApplicationContext context = new AnnotationConfigApplicationContext(HibernateUtil.class);

        CategorieService cs = context.getBean(CategorieService.class);
        ProduitService ps = context.getBean(ProduitService.class);
        CommandeService comService = context.getBean(CommandeService.class);
        LigneCommandeService lcs = context.getBean(LigneCommandeService.class);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date date1 = null;
        Date date2 = null;
        try {
            date1 = sdf.parse("2013-03-14");
            date2 = sdf.parse("2023-10-20");
        } catch (ParseException e) {
            e.printStackTrace();
        }


        System.out.println("Création des catégories...");
        Categorie cat1 = new Categorie("PC", "Ordinateurs Portables");
        Categorie cat2 = new Categorie("IMP", "Imprimantes");
        cs.create(cat1);
        cs.create(cat2);

        System.out.println("Création des produits...");
        // 2. Créer des Produits
        Produit p1 = new Produit("ES12", 120, cat1);
        Produit p2 = new Produit("ZR85", 100, cat1);
        Produit p3 = new Produit("EE85", 200, cat1);
        Produit p4 = new Produit("HP45", 150, cat2);
        Produit p5 = new Produit("EPSON7", 80, cat2);
        ps.create(p1);
        ps.create(p2);
        ps.create(p3);
        ps.create(p4);
        ps.create(p5);

        System.out.println("Création des commandes...");
        Commande cmd1 = new Commande(date1);
        Commande cmd2 = new Commande(date2);
        comService.create(cmd1);
        comService.create(cmd2);

        System.out.println("Création des lignes de commande...");

        lcs.create(new LigneCommandeProduit(p1, cmd1, 7));
        lcs.create(new LigneCommandeProduit(p2, cmd1, 14));
        lcs.create(new LigneCommandeProduit(p3, cmd1, 5));

        lcs.create(new LigneCommandeProduit(p4, cmd2, 10));
        lcs.create(new LigneCommandeProduit(p1, cmd2, 3));

        System.out.println("--- DONNÉES CRÉÉES, DÉBUT DES TESTS ---");


        System.out.println("\n--- Test 1: Affichage produits de la commande 1 ---");
        Commande cmdTest = comService.findById(cmd1.getId());
        ps.printProduitsDeCommande(cmdTest);

        ((AnnotationConfigApplicationContext) context).close();
    }
}