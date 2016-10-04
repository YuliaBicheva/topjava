package ru.javawebinar.topjava.web.meal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import ru.javawebinar.topjava.AuthorizedUser;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.to.MealWithExceed;
import ru.javawebinar.topjava.service.MealService;
import ru.javawebinar.topjava.util.MealsUtil;

import java.util.List;

/**
 * GKislin
 * 06.03.2015.
 */
@Controller
public class MealRestController {

    private final Logger LOG = LoggerFactory.getLogger(getClass());

    @Autowired
    private MealService service;

    public void save(Meal meal) {
        LOG.info("create " + meal);
        service.save(meal, AuthorizedUser.id());
    }

    public List<MealWithExceed> getAll() {
        LOG.info("getAll");
        LOG.debug("AuthorizedUser id = {}", AuthorizedUser.id());
        LOG.debug("Service {}", service);
        return MealsUtil.getWithExceeded(service.getAll(AuthorizedUser.id()), AuthorizedUser.getCaloriesPerDay());
    }

    public void delete(int id) {
        LOG.info("delete " + id);
        service.delete(id, AuthorizedUser.id());
    }

    public Meal get(int id) {
        LOG.info("get " + id);
        return service.get(id, AuthorizedUser.id());
    }
}
