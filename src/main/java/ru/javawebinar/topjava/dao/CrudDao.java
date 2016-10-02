package ru.javawebinar.topjava.dao;

import ru.javawebinar.topjava.model.Base;

import java.util.List;

/**
 * Created by yulia on 02.10.16.
 */
public interface CrudDao<T extends Base> {

    T saveOrUpdate(T t);

    boolean delete (Integer id);

    T findById(Integer id);

    List<T> findAll();
}
