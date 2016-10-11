package ru.javawebinar.topjava.repository.jpa;

import org.springframework.dao.support.DataAccessUtils;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.repository.MealRepository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.time.LocalDateTime;
import java.util.List;

/**
 * User: gkisline
 * Date: 26.08.2014
 */

@Repository
@Transactional(readOnly = true)
public class JpaMealRepositoryImpl implements MealRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    @Transactional
    public Meal save(Meal meal, int userId) {
        User user = entityManager.getReference(User.class, userId);
        meal.setUser(user);
        if(meal.isNew()){
            entityManager.persist(meal);
            return meal;
        }else{
            return entityManager.merge(meal);
        }
    }

    @Override
    public boolean delete(int id, int userId) {
        return entityManager.createNamedQuery(Meal.DELETE).
                setParameter("id",id).
                setParameter("userId",userId).executeUpdate() != 0;
    }

    @Override
    public Meal get(int id, int userId) {
        List<Meal> meals = entityManager.createNamedQuery(Meal.GET, Meal.class).setParameter("id",id).setParameter("user_id",userId).getResultList();
        return DataAccessUtils.singleResult(meals);
    }

    @Override
    public List<Meal> getAll(int userId) {
        return entityManager.createNamedQuery(Meal.ALL_SORTED, Meal.class).setParameter("user_id",userId).getResultList();
    }

    @Override
    public List<Meal> getBetween(LocalDateTime startDate, LocalDateTime endDate, int userId) {
        return entityManager.createNamedQuery(Meal.BETWEEN_SORTED, Meal.class).
                setParameter("user_id",userId).
                setParameter(1,startDate).
                setParameter(2, endDate).
                getResultList();
    }
}