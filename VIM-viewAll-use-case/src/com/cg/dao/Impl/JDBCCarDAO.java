package com.cg.dao.Impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import com.cg.dao.CarDAO;
import com.cg.dto.CarDTO;
import com.cg.util.ServiceLocator;
import com.cg.util.ServiceLocatorException;

//TODO 1 Import appropriate classes based on following TODOs
//Follow TODOs (if available)
/**
 * 
 * This is a JDBCCarDAO class
 * @see java.lang.Object
 * @author Abhishek
 * 
 *
 */
 
 //TODO 2 Implement appropriate Interface
public class JDBCCarDAO implements CarDAO {
	//TODO 3 Declare a local variable datasource of type DataSource follow encapsulation principle
	
	String dataSourceJndiName = "jdbc/VIMDataSource";
    DataSource datasource = null;
	
	public JDBCCarDAO() {
		//TODO 4 Initialize the dataSource in TODO 3 using ServiceLocator API
		try {
			datasource =(DataSource) ServiceLocator.getDataSource(dataSourceJndiName);
		} catch (ServiceLocatorException e) {
			System.out.println("Container Service not available");
		}
		
		//TODO 5 If any error occur in getting this service then throw ServiceLocatorException
		//with error message as 'Container Service not available'
		
	}

	
	@Override
	/**
	 * This method is mapped to ADD_CAR_ACTION
	 * @param car a CarDTO 
	 */
	public void create(CarDTO car) throws JDBCDaoException {
		// TODO Auto-generated method stub
		Connection connection = null;
		PreparedStatement insertStatement = null;
		int rows = 0;
		
		String insertQuery = "insert into Car (MAKE,MODEL,MODEL_YEAR) values(?,?,?)";
		
		try{
			try {
				//TODO 6 
				//Get a connection using datasource
				//Start the JDBC transaction
				//Create a PreparedStatement using insertQuery
				//Set the parameters of the PreparedStatement
				//Invoke appropriate API of JDBC to update and commit the record
				connection = (Connection) datasource.getConnection();
				//connection.setAutoCommit(false);
				insertStatement = connection.prepareStatement(insertQuery,PreparedStatement.RETURN_GENERATED_KEYS);
				
				insertStatement.setString(1, car.getMake());
				insertStatement.setString(2, car.getModel());
				insertStatement.setString(3, car.getModelYear());
				
				rows = insertStatement.executeUpdate();
				//connection.commit();
				
				System.out.println(rows+" rows inserted");
				
			} 
			catch (SQLException e) {
//				e.printStackTrace();
				
				if (connection != null)
					connection.rollback();
				throw e;
			} 
			finally {
				if (connection != null)
					connection.close();			
			}
		}
		catch(SQLException e){
			throw new JDBCDaoException("SQL error while excecuting this query: "+ insertQuery,e);
		}
		
	}

	@Override
	/**
	 * This method is mapped to DELETE_CAR_ACTION
	 * @param ids collection of CAR ids. 
	 */
	public void delete(String[] ids) throws JDBCDaoException {
		Connection connection = null;
		PreparedStatement deleteStatement = null;
		int rows = 0;
		String deleteQuery = "delete from car where id=?";

		try{
			try {
				//TODO 7 
				//Get a connection using datasource
				//Start the JDBC transaction
				//Create a PreparedStatement using deleteQuery
				//Set the parameters of the PreparedStatement
				//Invoke appropriate API of JDBC to update and commit the record
				connection = (Connection) datasource.getConnection();
//				connection.setAutoCommit(false);
				deleteStatement = connection.prepareStatement(deleteQuery);
				
				System.out.println(ids);
				
				for(String id : ids){
					deleteStatement.setInt(1, Integer.parseInt(id));
					rows+=deleteStatement.executeUpdate();
				}
				
				System.out.println(rows+" rows deleted");
			} 
			catch (SQLException e) {
				if (connection != null)
					connection.rollback();	
				
				throw e;
			} 
			finally {
				if (connection != null)
					connection.close();				
			}
		}
		catch(SQLException e){
			throw new JDBCDaoException("SQL error while excecuting query: "+ deleteQuery,e);
		}		
	}

	@Override
	/**
	 * This method is mapped to EDIT_CAR_ACTION
	 * @param car a CarDTO 
	 */
	public void update(CarDTO car) throws JDBCDaoException {
		// TODO Auto-generated method stub
		String updateQuery = "update car set make=?,model=?,model_year=? where id=?";
		Connection connection = null;
		PreparedStatement updateStatement = null;
		int rows = 0;
		
		try{
			try {
				//TODO 8 
				//Get a connection using datasource
				//Start the JDBC transaction
				//Create a PreparedStatement using updateQuery
				//Set the parameters of the PreparedStatement
				//Invoke appropriate API of JDBC to update and commit the record
				connection = (Connection) datasource.getConnection();
//				connection.setAutoCommit(false);
				updateStatement = connection.prepareStatement(updateQuery);
				
				updateStatement.setString(1, car.getMake());
				updateStatement.setString(2, car.getModel());
				updateStatement.setString(3, car.getModelYear());
				
				updateStatement.setInt(4, car.getId());
				rows = updateStatement.executeUpdate();
//				connection.commit();
				System.out.println(rows+" rows updated");
				
			} 
			catch (SQLException e) {
				if(connection != null)
					connection.rollback();
				
				throw e;
			}
			finally {
				if (connection != null)
					connection.close();			
			}
		}
		catch(SQLException e){
			throw new JDBCDaoException("SQL error while excecuting query: "+ updateQuery,e);
		}
	}
	
	
	@Override
	/**
	 * This method is mapped to VIEW_CAR_LIST_ACTION
	 * @return List list of cars 
	 */
	public List<CarDTO> findAll() throws JDBCDaoException {
		List<CarDTO> carList = new ArrayList<CarDTO>();

		Connection connection = null;
		String selectQuery = "select * from CAR";
		
		
		try{
			try {
				//TODO 9 
				//Get a connection using datasource
				//Don't start the JDBC transaction
				//Create a Statement using selectQuery
				//Invoke appropriate API of JDBC to fire the query
				//For iteration over the ResultSet populate carList with CarDTO 
				
				connection = (Connection)datasource.getConnection();
//				connection.setAutoCommit(false);
				Statement selectStatement = connection.createStatement();
				ResultSet rs;
				
				rs = selectStatement.executeQuery(selectQuery);
				
				while(rs.next()){
					CarDTO car = new CarDTO();
					
					car.setId(rs.getInt(4));
					car.setMake(rs.getString(1));
					car.setModel(rs.getString(2));
					car.setModelYear(rs.getString(3));
					
					
					carList.add(car);
				}

			} 
			finally {
				if (connection != null)
					connection.close();				
			}
		}
		catch(SQLException e){
			throw new JDBCDaoException("SQL error while excecuting query: "+ selectQuery,e);
		}		

		return carList;
	}

	@Override
	/**
	 * This method is utility method for finding car by id.
	 * @param id id of the car to be searched
	 * @return  CarDTO A CarDTO
	 */
	public CarDTO findById(int id) throws JDBCDaoException {
		// TODO Auto-generated method stub
		String selectQuery = "select * from CAR where id=?";
		CarDTO car = new CarDTO();
		Connection connection = null;
		
		try{
			try {
				connection = datasource.getConnection();
				connection.setAutoCommit(false);
				PreparedStatement selectStatement = connection.prepareStatement(selectQuery);
				
				selectStatement.setInt(1, id);
				
				ResultSet result = selectStatement.executeQuery();
				
				if(result.next()){
					car.setId(result.getInt("ID"));
					car.setMake(result.getString("MAKE"));
					car.setModel(result.getString("MODEL"));
					car.setModelYear(result.getString("MODEL_YEAR"));
				}
			} 
			finally {
				if (connection != null)
					connection.close();			
			}
		}
		catch(SQLException e){
			throw new JDBCDaoException("SQL error while excecuting query: "+ selectQuery,e);
		}		

		return car;
	}

	

}
