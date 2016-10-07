package com.cg.dao;

import java.util.*;

import com.cg.dao.Impl.JDBCDaoException;
import com.cg.dto.*;

//Follow TODOs (if available)
/**
 * 
 * This is a CarDAO class
 * @see java.lang.Object
 * @author Abhishek
 * 
 *
 */
public interface CarDAO 
{
    public List<CarDTO> findAll() throws JDBCDaoException; 
    public CarDTO findById(int id) throws JDBCDaoException;
    public void create(CarDTO car) throws JDBCDaoException;
    public void update(CarDTO car) throws JDBCDaoException;
    public void delete(String[] ids) throws JDBCDaoException;
}