package core.wrappers.impl;

import core.wrappers.DatabaseDriver;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import java.util.List;

public class ExtHibernateDriver implements DatabaseDriver {
    private SessionFactory sessionFactory;

    @Override
    public void openConnection(String url, String username, String password) throws Exception {
        sessionFactory =  new Configuration()
                .configure()
                .buildSessionFactory();
    }

    @Override
    public <T> T insert(T t) {
        try (var session = sessionFactory.openSession()) {
            var transaction = session.beginTransaction();

            session.persist(t);

            transaction.commit();
        }
        return t;
    }

    @Override
    public <T> List<T> insert(List<T> t) {
        try (var session = sessionFactory.openSession()) {
            var transaction = session.beginTransaction();

            for (var obj : t) {
                session.persist(obj);
            }

            transaction.commit();
        }
        return t;
    }

    @Override
    public <T> T update(T t) {
        try (var session = sessionFactory.openSession()) {
            var transaction = session.beginTransaction();

            session.merge(t);

            transaction.commit();
        }
        return t;
    }

    @Override
    public <T> List<T> update(List<T> t) {
        try (var session = sessionFactory.openSession()) {
            var transaction = session.beginTransaction();

            for (var obj : t) {
                session.merge(obj);
            }

            transaction.commit();
        }
        return t;
    }

    @Override
    public <T> void delete(T t) {
        try (var session = sessionFactory.openSession()) {
            var transaction = session.beginTransaction();

            session.remove(t);

            transaction.commit();
        }
    }

    @Override
    public <T> void delete(List<T> t) {
        try (var session = sessionFactory.openSession()) {
            var transaction = session.beginTransaction();

            for (var obj : t) {
                session.remove(obj);
            }

            transaction.commit();
        }
    }

    @Override
    public <T> T selectById(Object id, Class<T> t) {
        try (var session = sessionFactory.openSession()) {
            return session.get(
                    t,
                    id
            );
        }
    }

    @Override
    public <T> List<T> customSelect(String query, Class<T> t) {
        try (var session = sessionFactory.openSession()) {
            return session.createQuery(
                    query,
                    t
            ).getResultList();
        }
    }

    @Override
    public <T> int customUpdate(String query, Class<T> t) {
        int rowsUpdated;

        try (var session = sessionFactory.openSession()) {
            var transaction = session.beginTransaction();

            rowsUpdated = session.createQuery(
                    query,
                    t
            ).executeUpdate();

            transaction.commit();
        }

        return rowsUpdated;
    }
}
