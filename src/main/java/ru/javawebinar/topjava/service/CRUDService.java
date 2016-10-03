package ru.javawebinar.topjava.service;

import ru.javawebinar.topjava.model.Base;

import java.util.List;

/**
 * Created by yulia on 02.10.16.
 */
public interface CRUDService<T extends Base> {

    void saveOrUpdate(T t);

    void delete(Integer id);

    T findById(Integer id);

    List<T> findAll();
}
