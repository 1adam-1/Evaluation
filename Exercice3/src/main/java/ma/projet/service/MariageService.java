package ma.projet.service;

import ma.projet.beans.Homme;
import ma.projet.beans.Mariage;
import ma.projet.dao.IDao;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Repository
public class MariageService implements IDao<Mariage> {

    @Autowired
    private SessionFactory sessionFactory;

    @Override
    @Transactional
    public boolean create(Mariage mariage) {
        Session session = sessionFactory.getCurrentSession();
        session.save(mariage);
        return true;
    }

    @Override
    @Transactional
    public boolean delete(Mariage mariage) {
        sessionFactory.getCurrentSession().delete(mariage);
        return true;
    }

    @Override
    @Transactional
    public boolean update(Mariage mariage) {
        sessionFactory.getCurrentSession().update(mariage);
        return true;
    }

    @Override
    @Transactional(readOnly = true)
    public Mariage findById(int id) {
        return sessionFactory.getCurrentSession().get(Mariage.class, id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Mariage> findAll() {
        return sessionFactory.getCurrentSession()
                .createQuery("from Mariage", Mariage.class)
                .list();
    }

    // Méthode : trouver les hommes ayant au moins n épouses entre deux dates
    @Transactional(readOnly = true)
    public List<Homme> findHommesWithAtLeastNFemmesBetweenDates(int n, java.time.LocalDate dateDebut, java.time.LocalDate dateFin) {
        java.util.Date d1 = dateDebut == null ? null : java.sql.Date.valueOf(dateDebut);
        java.util.Date d2 = dateFin == null ? null : java.sql.Date.valueOf(dateFin);

        StringBuilder hql = new StringBuilder("SELECT m.homme FROM Mariage m WHERE 1=1");
        if (d1 != null) hql.append(" AND m.dateDebut >= :d1");
        if (d2 != null) hql.append(" AND (m.dateFin <= :d2 OR m.dateFin IS NULL)");
        hql.append(" GROUP BY m.homme HAVING COUNT(DISTINCT m.femme) >= :n");

        Session session = sessionFactory.getCurrentSession();
        Query<Homme> q = session.createQuery(hql.toString(), Homme.class);
        if (d1 != null) q.setParameter("d1", d1);
        if (d2 != null) q.setParameter("d2", d2);
        q.setParameter("n", (long) n);
        return q.getResultList();
    }

    // Affichage formaté des mariages d'un homme
    @Transactional(readOnly = true)
    public void afficherMariagesAvecDetails(int hommeId) {
        Session session = sessionFactory.getCurrentSession();
        List<Mariage> mariages = session.createQuery(
                "SELECT m FROM Mariage m JOIN FETCH m.femme WHERE m.homme.id = :hommeId",
                Mariage.class)
                .setParameter("hommeId", hommeId)
                .list();

        if (mariages == null || mariages.isEmpty()) {
            System.out.println("Cet homme n'a aucun mariage enregistré.");
            return;
        }

        Homme homme = mariages.get(0).getHomme();
        // afficher prénom puis nom pour correspondre à l'exemple
        System.out.println("Nom : " + homme.getPrenom() + " " + homme.getNom());

        java.text.SimpleDateFormat df = new java.text.SimpleDateFormat("dd/MM/yyyy");

        List<Mariage> enCours = new ArrayList<>();
        List<Mariage> termines = new ArrayList<>();
        for (Mariage m : mariages) {
            if (m.getDateFin() == null) enCours.add(m);
            else termines.add(m);
        }

        System.out.println("Mariages En Cours :");
        if (!enCours.isEmpty()) {
            int i = 1;
            for (Mariage m : enCours) {
                String dd = m.getDateDebut() != null ? df.format(m.getDateDebut()) : "";
                System.out.printf("%d. Femme : %-15s %-15s Date Début : %s    Nbr Enfants : %d%n",
                        i++, m.getFemme().getPrenom(), m.getFemme().getNom(), dd, m.getNbrEnfant());
            }
        } else {
            System.out.println("Aucun");
        }
        System.out.println();

        System.out.println("Mariages échoués :");
        if (!termines.isEmpty()) {
            int i = 1;
            for (Mariage m : termines) {
                String dd = m.getDateDebut() != null ? df.format(m.getDateDebut()) : "";
                String dfm = m.getDateFin() != null ? df.format(m.getDateFin()) : "";
                System.out.printf("%d. Femme : %-15s %-15s Date Début : %s\nDate Fin : %s    Nbr Enfants : %d%n",
                        i++, m.getFemme().getPrenom(), m.getFemme().getNom(), dd, dfm, m.getNbrEnfant());
            }
        } else {
            System.out.println("Aucun");
        }
        System.out.println();
    }

}
