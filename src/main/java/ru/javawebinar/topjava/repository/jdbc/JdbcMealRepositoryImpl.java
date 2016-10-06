package ru.javawebinar.topjava.repository.jdbc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;

import javax.sql.DataSource;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

/**
 * User: gkislin
 * Date: 26.08.2014
 */

@Repository
public class JdbcMealRepositoryImpl implements MealRepository {

    private static final Logger LOG = LoggerFactory.getLogger(JdbcMealRepositoryImpl.class);

    private static final BeanPropertyRowMapper<Meal> ROW_MAPPER = BeanPropertyRowMapper.newInstance(Meal.class);
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    private SimpleJdbcInsert insertUser;

    @Autowired
    public JdbcMealRepositoryImpl(DataSource dataSource) {
        this.insertUser = new SimpleJdbcInsert(dataSource)
                .withTableName("MEALS")
                .usingGeneratedKeyColumns("id");
    }

    @Override
    public Meal save(Meal meal, int userId) {
        MapSqlParameterSource mealMap = new MapSqlParameterSource()
                .addValue("id",meal.getId())
                .addValue("userId",userId)
                .addValue("dateTime",meal.getDateTime())
                .addValue("description",meal.getDescription())
                .addValue("calories",meal.getCalories());
        if(meal.isNew()){
            Number returnedKey = insertUser.executeAndReturnKey(mealMap);
            meal.setId(returnedKey.intValue());
        }else{
            namedParameterJdbcTemplate.update(
                    "UPDATE meals SET user_id:=userId, date_time:=dateTime, description:=description, " +
                            "calories:=calories WHERE id:=id",mealMap);
        }
        return meal;
    }

    @Override
    public boolean delete(int id, int userId) {
        return jdbcTemplate.update("DELETE FROM meals WHERE id=? AND user_id=?", ROW_MAPPER, id, userId) != 0;
    }

    @Override
    public Meal get(int id, int userId) {
        List<Meal> meals = jdbcTemplate.query("SELECT id, date_time, description, calories FROM meals WHERE id=? AND user_id=?", ROW_MAPPER, id, userId);
        return DataAccessUtils.singleResult(meals);
    }

    @Override
    public List<Meal> getAll(int userId) {
        List<Meal> meals = jdbcTemplate.query("SELECT id, date_time as dateTime, description, calories FROM meals WHERE user_id=?",ROW_MAPPER, userId);
        LOG.debug("Meals list {}" + meals);
        if(meals == null || meals.isEmpty()){
            meals = Collections.emptyList();
        }else {
            Collections.sort(meals);
        }
        return meals;
    }

    @Override
    public List<Meal> getBetween(LocalDateTime startDate, LocalDateTime endDate, int userId) {
//        List<Meal> meals = jdbcTemplate.query("SELECT id, date_time, description, calories FROM meals WHERE user_id=? AND date_time BETWEEN ? AND ?", ROW_MAPPER, userId, startDate, endDate);
        List<Meal> meals = jdbcTemplate.query("SELECT id, date_time, description, calories FROM meals WHERE user_id=? AND date_time BETWEEN ? AND ?", ROW_MAPPER, userId, startDate, endDate);
        if(meals == null || meals.isEmpty()){
            meals = Collections.emptyList();
        }else {
            Collections.sort(meals);
        }
        return meals;
    }
}
