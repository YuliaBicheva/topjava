package ru.javawebinar.topjava.repository.mock;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.util.MealsUtil;
import ru.javawebinar.topjava.util.TimeUtil;

import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * GKislin
 * 15.09.2015.
 */
@Repository
public class InMemoryMealRepositoryImpl implements MealRepository {
    private static final Logger LOG = LoggerFactory.getLogger(InMemoryMealRepositoryImpl.class);

    private Map<Integer, Map<Integer, Meal>> repository = new ConcurrentHashMap<>();
    private AtomicInteger counter = new AtomicInteger(0);

    {
        for(int k = 0; k < MealsUtil.MEALS.size(); k++) {
            save(MealsUtil.MEALS.get(k), k < 3 ? MealsUtil.ADMIN_ID : MealsUtil.USER_ID );
        }
    }

    @Override
    public Meal save(Meal meal, int userId) {
        LOG.info("save {} for userId {}", meal, userId);
        if (meal.isNew()) {
            meal.setId(counter.incrementAndGet());
        }
        repository.computeIfAbsent(userId, id -> new TreeMap()).put(meal.getId(), meal);
        return meal;
    }

    @Override
    public boolean delete(int id, int userId) {
        LOG.info("delete {} by userId {}", id, userId);
        return repository.get(userId).remove(id) != null;
    }

    @Override
    public Meal get(int id, int userId) {
        LOG.info("get {} by userId {}", id, userId);

        return repository.get(userId).get(id);
    }

    @Override
    public List<Meal> getAll(int userId) {
        LOG.info("getAll by userId {}", userId);
        List<Meal> usersMealList = repository.get(userId) == null ? Collections.EMPTY_LIST : new ArrayList<>(repository.get(userId).values());
        Collections.sort(usersMealList);
        return usersMealList;
    }

    @Override
    public List<Meal> getBetween(int userId, LocalDate startDate, LocalDate endDate) {
        LOG.info("getAll by userId {}", userId);
        Map<Integer, Meal> integerMealMap = repository.get(userId);
        if(integerMealMap == null){
            return Collections.EMPTY_LIST;
        }
        return repository.get(userId).values().stream()
                .filter(m -> TimeUtil.isBetween(m.getDateTime().toLocalDate(),startDate,endDate))
                .collect(Collectors.toList());
    }
}

