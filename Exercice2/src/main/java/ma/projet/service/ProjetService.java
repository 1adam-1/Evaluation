package ma.projet.service;

import ma.projet.classes.EmployeTache;
import ma.projet.classes.Projet;
import ma.projet.classes.Tache;
import ma.projet.dao.IDao;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.List;

@Service
@Transactional
public class ProjetService implements IDao<Projet> {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public boolean create(Projet o) {
        try {
            entityManager.persist(o);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean update(Projet o) {
        try {
            entityManager.merge(o);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean delete(Projet o) {
        try {
            entityManager.remove(entityManager.contains(o) ? o : entityManager.merge(o));
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public Projet findById(Serializable id) {
        try {
            return entityManager.find(Projet.class, id);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<Projet> findAll() {
        try {
            return entityManager.createQuery("FROM Projet", Projet.class).getResultList();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    public void afficherTachesPlanifiees(int projetId) {
        Projet projet = entityManager.find(Projet.class, projetId);
        if (projet == null) return;

        List<Tache> taches = entityManager.createQuery("SELECT t FROM Tache t WHERE t.projet.id = :projetId ORDER BY t.dateDebut", Tache.class)
                .setParameter("projetId", projetId).getResultList();
        if (taches.isEmpty()) return;

        SimpleDateFormat sdf = new SimpleDateFormat("dd MMMM yyyy");
        System.out.println("\nProjet : " + projet.getId() + "\t\tNom : " + projet.getNom() +
                "\t\tDate début : " + sdf.format(projet.getDateDebut()));
        System.out.println("Liste des tâches planifiées:");
        System.out.printf("%-5s %-20s %-20s %-20s %-10s%n", "Num", "Nom", "Date Début Planifiée", "Date Fin Planifiée", "Prix (DH)");
        System.out.println("--------------------------------------------------------------------------------");

        for (Tache t : taches) {
            System.out.printf("%-5d %-20s %-20s %-20s %-10.2f%n", t.getId(), t.getNom(),
                    sdf.format(t.getDateDebut()), sdf.format(t.getDateFin()), t.getPrix());
        }
    }


    public void afficherTachesRealisees(int projetId) {
        Projet projet = entityManager.find(Projet.class, projetId);
        if (projet == null) return;

        List<EmployeTache> taches = entityManager.createQuery("SELECT et FROM EmployeTache et WHERE et.projet.id = :projetId ORDER BY et.dateDebutReelle", EmployeTache.class)
                .setParameter("projetId", projetId).getResultList();
        if (taches.isEmpty()) return;

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        SimpleDateFormat sdfLong = new SimpleDateFormat("dd MMMM yyyy");
        System.out.println("\nProjet : " + projet.getId() + "\t\tNom : " + projet.getNom() +
                "\t\tDate début : " + sdfLong.format(projet.getDateDebut()));
        System.out.println("Liste des tâches:");
        System.out.printf("%-5s %-20s %-20s %-20s%n", "Num", "Nom", "Date Début Réelle", "Date Fin Réelle");
        System.out.println("----------------------------------------------------------------");

        for (EmployeTache et : taches) {
            System.out.printf("%-5d %-20s %-20s %-20s%n", et.getTache().getId(), et.getTache().getNom(),
                    sdf.format(et.getDateDebutReelle()), sdf.format(et.getDateFinReelle()));
        }
    }


    public void afficherTachesReelles(int projetId) {
        Projet projet = entityManager.find(Projet.class, projetId);
        if (projet == null) return;

        List<EmployeTache> employeTaches = entityManager.createQuery(
                "SELECT et FROM EmployeTache et WHERE et.projet.id = :projetId ORDER BY et.dateDebutReelle",
                EmployeTache.class)
                .setParameter("projetId", projetId).getResultList();

        if (employeTaches.isEmpty()) return;

        SimpleDateFormat sdfShort = new SimpleDateFormat("dd/MM/yyyy");
        SimpleDateFormat sdfLong = new SimpleDateFormat("dd MMMM yyyy");

        System.out.println("Projet : " + projet.getId() + "      Nom : " + projet.getNom() +
                "     Date début : " + sdfLong.format(projet.getDateDebut()));
        System.out.println("Liste des tâches:");
        System.out.printf("%-4s%-15s%-21s%-15s%n", "Num", "Nom", "Date Début Réelle", "Date Fin Réelle");

        for (EmployeTache et : employeTaches) {
            System.out.printf("%-4d%-15s%-21s%-15s%n",
                    et.getTache().getId(),
                    et.getTache().getNom(),
                    sdfShort.format(et.getDateDebutReelle()),
                    sdfShort.format(et.getDateFinReelle()));
        }
    }
}
