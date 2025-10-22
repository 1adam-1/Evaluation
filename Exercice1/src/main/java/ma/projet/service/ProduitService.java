package ma.projet.service;

import ma.projet.classes.Categorie;
import ma.projet.classes.Commande;
import ma.projet.classes.LigneCommandeProduit;
import ma.projet.classes.Produit;
import ma.projet.dao.IDao;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

@Service
@Transactional
public class ProduitService implements IDao<Produit> {

    @Autowired
    private SessionFactory sessionFactory;


    @Override
    public boolean create(Produit o) {
        try {
            sessionFactory.getCurrentSession().save(o);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean update(Produit o) {
        try {
            sessionFactory.getCurrentSession().update(o);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean delete(Produit o) {
        try {
            sessionFactory.getCurrentSession().delete(o);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public Produit findById(Serializable id) {
        try {
            return sessionFactory.getCurrentSession().get(Produit.class, id);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<Produit> findAll() {
        try {
            return sessionFactory.getCurrentSession().createQuery("FROM Produit", Produit.class).list();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    public List<Produit> findProduitsByCategorie(Categorie c) {
        try {
            Query<Produit> query = sessionFactory.getCurrentSession()
                    .createQuery("FROM Produit p WHERE p.categorie = :categorie", Produit.class);
            query.setParameter("categorie", c);
            return query.list();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    public List<Produit> findProduitsCommandesEntreDates(Date dateDebut, Date dateFin) {
        try {
            Query<Produit> query = sessionFactory.getCurrentSession().createQuery(
                    "SELECT DISTINCT l.produit FROM LigneCommandeProduit l " +
                            "WHERE l.commande.date BETWEEN :dateDebut AND :dateFin", Produit.class
            );
            query.setParameter("dateDebut", dateDebut);
            query.setParameter("dateFin", dateFin);
            return query.list();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    public void printProduitsDeCommande(Commande commande) {
        try {
            // 1. Récupérer les lignes de commande pour la commande donnée
            Query<LigneCommandeProduit> query = sessionFactory.getCurrentSession().createQuery(
                    "FROM LigneCommandeProduit l WHERE l.commande = :commande",
                    LigneCommandeProduit.class
            );
            query.setParameter("commande", commande);
            List<LigneCommandeProduit> lignes = query.list();

            // 2. Formater l'affichage comme demandé
            SimpleDateFormat sdf = new SimpleDateFormat("dd MMMM yyyy", Locale.FRENCH);
            System.out.println("Commande : " + commande.getId() + "\t Date : " + sdf.format(commande.getDate()));
            System.out.println("Liste des produits :");
            System.out.println("Référence\tPrix\t\tQuantité");

            double total = 0;
            for (LigneCommandeProduit ligne : lignes) {
                Produit p = ligne.getProduit();
                System.out.println(
                        p.getReference() + "\t\t" +
                                p.getPrix() + " DH\t" +
                                ligne.getQuantite()
                );
                total += p.getPrix() * ligne.getQuantite();
            }
            System.out.println("Total : " + total + " DH");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public List<Produit> findProduitsPrixSuperieurA100() {
        try {
            Query<Produit> query = sessionFactory.getCurrentSession()
                    .getNamedQuery("produit.prixSup100");
            return query.list();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}