package ma.projet.service;

import ma.projet.classes.EmployeTache;
import ma.projet.dao.IDao;
import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.List;

@Service
@Transactional
public class EmployeTacheService implements IDao<EmployeTache> {

    @Autowired
    private EntityManager entityManager;

    @Override
    public boolean create(EmployeTache o) {
        try {
            entityManager.persist(o);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean update(EmployeTache o) {
        try {
            entityManager.merge(o);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean delete(EmployeTache o) {
        try {
            entityManager.remove(entityManager.contains(o) ? o : entityManager.merge(o));
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public EmployeTache findById(Serializable id) {
        try {
            return entityManager.find(EmployeTache.class, id);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<EmployeTache> findAll() {
        try {
            return entityManager.createQuery("FROM EmployeTache", EmployeTache.class).getResultList();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
