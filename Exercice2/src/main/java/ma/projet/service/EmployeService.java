package ma.projet.service;

import ma.projet.classes.Employe;
import ma.projet.classes.EmployeTache;
import ma.projet.classes.Projet;
import ma.projet.dao.IDao;
import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.List;

@Service
@Transactional
public class EmployeService implements IDao<Employe> {

    @Autowired
    private EntityManager entityManager;

    @Override
    public boolean create(Employe o) {
        try {
            entityManager.persist(o);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean update(Employe o) {
        try {
            entityManager.merge(o);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean delete(Employe o) {
        try {
            entityManager.remove(entityManager.contains(o) ? o : entityManager.merge(o));
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public Employe findById(Serializable id) {
        try {
            return entityManager.find(Employe.class, id);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<Employe> findAll() {
        try {
            return entityManager.createQuery("FROM Employe", Employe.class).getResultList();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }



    public void afficherTachesRealisees(int employeId) {
        List<EmployeTache> taches = entityManager.createQuery("SELECT et FROM EmployeTache et WHERE et.employe.id = :employeId ORDER BY et.dateDebutReelle", EmployeTache.class)
                .setParameter("employeId", employeId).getResultList();
        if (taches.isEmpty()) return;

        Employe emp = taches.get(0).getEmploye();
        System.out.println("\n=== Tâches réalisées par " + emp.getNom() + " " + emp.getPrenom() + " ===");
        System.out.printf("%-5s %-20s %-20s %-20s %-20s%n", "Num", "Nom Tâche", "Projet", "Date Début Réelle", "Date Fin Réelle");
        System.out.println("--------------------------------------------------------------------------------");

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        for (EmployeTache et : taches) {
            System.out.printf("%-5d %-20s %-20s %-20s %-20s%n", et.getTache().getId(), et.getTache().getNom(),
                    et.getProjet().getNom(), sdf.format(et.getDateDebutReelle()), sdf.format(et.getDateFinReelle()));
        }
    }


    public void afficherProjetsGeres(int employeId) {
        List<Projet> projets = entityManager.createQuery("SELECT p FROM Projet p WHERE p.employe.id = :employeId ORDER BY p.dateDebut", Projet.class)
                .setParameter("employeId", employeId).getResultList();
        if (projets.isEmpty()) return;

        Employe emp = findById(employeId);
        System.out.println("\n=== Projets gérés par " + emp.getNom() + " " + emp.getPrenom() + " ===");
        System.out.printf("%-5s %-30s %-20s%n", "ID", "Nom du Projet", "Date Début");
        System.out.println("----------------------------------------------------------------");

        SimpleDateFormat sdf = new SimpleDateFormat("dd MMMM yyyy");
        for (Projet p : projets) {
            System.out.printf("%-5d %-30s %-20s%n", p.getId(), p.getNom(), sdf.format(p.getDateDebut()));
        }
    }
}
