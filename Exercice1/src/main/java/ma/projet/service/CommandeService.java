package ma.projet.service;

import ma.projet.classes.Commande;
import ma.projet.dao.IDao;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.List;

@Service
@Transactional
public class CommandeService implements IDao<Commande> {

    @Autowired
    private SessionFactory sessionFactory;

    @Override
    public boolean create(Commande o) {
        try {
            sessionFactory.getCurrentSession().save(o);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean update(Commande o) {
        try {
            sessionFactory.getCurrentSession().update(o);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean delete(Commande o) {
        try {
            sessionFactory.getCurrentSession().delete(o);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public Commande findById(Serializable id) {
        try {
            return sessionFactory.getCurrentSession().get(Commande.class, id);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<Commande> findAll() {
        try {
            return sessionFactory.getCurrentSession().createQuery("FROM Commande", Commande.class).list();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}