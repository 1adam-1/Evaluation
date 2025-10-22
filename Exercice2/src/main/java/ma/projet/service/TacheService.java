package ma.projet.service;

import ma.projet.classes.EmployeTache;
import ma.projet.classes.Tache;
import ma.projet.dao.IDao;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Service
@Transactional
public class TacheService implements IDao<Tache> {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public boolean create(Tache o) {
        try {
            entityManager.persist(o);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean update(Tache o) {
        try {
            entityManager.merge(o);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean delete(Tache o) {
        try {
            entityManager.remove(entityManager.contains(o) ? o : entityManager.merge(o));
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public Tache findById(Serializable id) {
        try {
            return entityManager.find(Tache.class, id);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<Tache> findAll() {
        try {
            return entityManager.createQuery("FROM Tache", Tache.class).getResultList();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    public void afficherTachesPrixSuperieur(double prix) {
        List<Tache> taches = entityManager.createNamedQuery("Tache.findByPrixSuperieur", Tache.class)
                .setParameter("prix", prix).getResultList();
        if (taches.isEmpty()) return;

        System.out.println("\n=== Tâches dont le prix est supérieur à " + prix + " DH ===");
        System.out.printf("%-5s %-30s %-20s %-15s%n", "ID", "Nom", "Projet", "Prix (DH)");
        System.out.println("------------------------------------------------------------------------");

        for (Tache t : taches) {
            System.out.printf("%-5d %-30s %-20s %-15.2f%n", t.getId(), t.getNom(),
                    t.getProjet() != null ? t.getProjet().getNom() : "N/A", t.getPrix());
        }
    }


    public void afficherTachesEntreDeuxDates(Date dateDebut, Date dateFin) {
        List<EmployeTache> taches = entityManager.createQuery("SELECT et FROM EmployeTache et WHERE et.dateDebutReelle >= :dateDebut AND et.dateFinReelle <= :dateFin ORDER BY et.dateDebutReelle", EmployeTache.class)
                .setParameter("dateDebut", dateDebut).setParameter("dateFin", dateFin).getResultList();
        if (taches.isEmpty()) return;

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        System.out.println("\n=== Tâches réalisées entre le " + sdf.format(dateDebut) + " et le " + sdf.format(dateFin) + " ===");
        System.out.printf("%-5s %-20s %-20s %-20s %-20s %-20s%n", "ID", "Nom Tâche", "Employé", "Projet", "Date Début", "Date Fin");
        System.out.println("--------------------------------------------------------------------------------------------");

        for (EmployeTache et : taches) {
            System.out.printf("%-5d %-20s %-20s %-20s %-20s %-20s%n", et.getTache().getId(), et.getTache().getNom(),
                    et.getEmploye().getNom() + " " + et.getEmploye().getPrenom(), et.getProjet().getNom(),
                    sdf.format(et.getDateDebutReelle()), sdf.format(et.getDateFinReelle()));
        }
    }
}
