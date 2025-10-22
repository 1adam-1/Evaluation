package ma.projet.service;

import ma.projet.beans.Femme;
import ma.projet.dao.IDao;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;

import java.util.List;

@Repository
public class FemmeService implements IDao<Femme> {

    @Autowired
    private SessionFactory sessionFactory;

    @Override
    @Transactional
    public boolean create(Femme femme) {
        Session session = sessionFactory.getCurrentSession();
        session.save(femme);
        return true;
    }

    @Override
    @Transactional
    public boolean delete(Femme femme) {
        sessionFactory.getCurrentSession().delete(femme);
        return true;
    }

    @Override
    @Transactional
    public boolean update(Femme femme) {
        sessionFactory.getCurrentSession().update(femme);
        return true;
    }

    @Override
    @Transactional(readOnly = true)
    public Femme findById(int id) {
        return sessionFactory.getCurrentSession().get(Femme.class, id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Femme> findAll() {
        return sessionFactory.getCurrentSession()
                .createQuery("from Femme", Femme.class)
                .list();
    }


    @Transactional(readOnly = true)
    public int nombreEnfantsEntreDates(int femmeId, LocalDate dateDebut, LocalDate dateFin) {
        Session session = sessionFactory.getCurrentSession();

        // Convert LocalDate -> java.sql.Date (subclass of java.util.Date) for the query
        java.util.Date d1 = dateDebut == null ? null : java.sql.Date.valueOf(dateDebut);
        java.util.Date d2 = dateFin == null ? null : java.sql.Date.valueOf(dateFin);

        var q = session.createNamedQuery("femme.countEnfants");
        q.setParameter("femmeId", femmeId);
        q.setParameter("date1", d1);
        q.setParameter("date2", d2);
        Object result = q.getSingleResult();

        if (result == null) return 0;
        if (result instanceof Number) return ((Number) result).intValue();
        try {
            return Integer.parseInt(result.toString());
        } catch (NumberFormatException ex) {
            return 0;
        }
    }


    @Transactional(readOnly = true)
    public List<Femme> femmesMarieesAuMoinsDeuxFois() {
        return sessionFactory.getCurrentSession()
                .createNamedQuery("femme.findMultipleMarriages", Femme.class)
                .getResultList();
    }


}
