package DAO;

import java.util.List;
/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author akilanilusha
 */

public abstract class CommonDAO<T> {

    public abstract boolean insert(T entity);

    public abstract boolean update(T entity);

    public abstract boolean delete(int id);

    public abstract List<T> getAll(); //to fetch all entities

    public abstract T getById(int id); // to fetch by ID
}
