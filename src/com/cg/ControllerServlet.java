package com.cg;

import java.io.IOException;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.cg.dao.CarDAO;
import com.cg.dao.Impl.JDBCDaoException;
import com.cg.dto.CarDTO;

//TODO 1 Import appropriate classes

//Follow TODOs (if available)
/**
 * 
 * This is a ControllerServlet class
 * @see java.lang.Object
 * @author Abhishek
 * 
 *
 */


public class ControllerServlet extends HttpServlet
{
    private static final String ACTION_KEY = "action";
    private static final String VIEW_CAR_LIST_ACTION = "viewCarList";
    private static final String ADD_CAR_ACTION = "addCar";
    private static final String SAVE_CAR_ACTION = "saveCar";
    private static final String EDIT_CAR_ACTION = "editCar";
    private static final String DELETE_CAR_ACTION = "deleteCar";
    private static final String ERROR_KEY = "errorMessage";

    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
		
        //TODO 2 Invoke processRequest
    	
    	try {
			processRequest(request, response);
		} catch (JDBCDaoException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        //TODO 3 Invoke processRequest
    	try {
			processRequest(request, response);
		} catch (JDBCDaoException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }

    /**
     * This method will process request based on action performed on screen. 
     * @param request				HttpServletRequest
     * @param response				HttpServletResponse
     * @throws ServletException		if error occurs
     * @throws IOException			if error occurs
     * @throws JDBCDaoException 
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, JDBCDaoException {
        String actionName = request.getParameter(ACTION_KEY);
        String destinationPage = null; 
        
//        CarDAO carDAO = DBUtility.getCarDAO();
        
        WebApplicationContext appContext = WebApplicationContextUtils.getRequiredWebApplicationContext(request.getServletContext());
        CarDAO carDAO = appContext.getBean("jdbcCarDao", CarDAO.class);
        
        // perform action
        if(VIEW_CAR_LIST_ACTION.equals(actionName))
        {            
            //TODO 4 
			//Use carDao to get the list of the cars
			//Set the list in request with attribute name as 'carList'
        	List<CarDTO> carList = carDAO.findAll();
        	request.setAttribute("carList", carList);
			//Set the destination page to carList.jsp
			destinationPage = "carList.jsp";
        	
        }
        else if(ADD_CAR_ACTION.equals(actionName))
        {
			//TODO 5 
			//Create a new CarDTO and set in request with attribute name as 'car'
        	request.setAttribute("car", new CarDTO());
			//Set the destination page to carForm.jsp
        	destinationPage="carForm.jsp";
            
        }  
        else if(EDIT_CAR_ACTION.equals(actionName))
        {
        	
			//TODO 6 
			//Get the car id from request, with parameter name as 'id'
        	int id = Integer.parseInt(request.getParameter("id"));
			//Find the respective car (CarDTO) from carDAO using appropriate API of DAO
			//Set the found car in request with name as 'car'
        	
        	request.setAttribute("car", carDAO.findById(id));
			//Set the destination page to carForm.jsp
        	destinationPage="carForm.jsp";
            
        }        
        else if(SAVE_CAR_ACTION.equals(actionName))
        {
			//TODO 7 
			//Create a new CarDTO
        	CarDTO car = null;
        	int id = -1;
			//set the properties of the DTO from request parameters
        	
        	System.out.println("id = "+request.getParameter("id"));
        	
        	if(!request.getParameter("id").equals("null") || !request.getParameter("id").equals("-1"))
        		id = Integer.parseInt(request.getParameter("id"));
        	
			//If it is a new car then invoke create api of DAO else update api
        	
        	if(id==-1){
        		car = new CarDTO();
        		
        		car.setMake(request.getParameter("make"));
        		car.setModel(request.getParameter("model"));
        		car.setModelYear(request.getParameter("modelYear"));
        		
        		carDAO.create(car);
        		
        	}else{
        		car = carDAO.findById(id);
        		
        		car.setMake(request.getParameter("make"));
        		car.setModel(request.getParameter("model"));
        		car.setModelYear(request.getParameter("modelYear"));
        		
        		carDAO.update(car);
        	}
        	
			//Get all the Cars using DAO
        	List<CarDTO> cars = carDAO.findAll();
			
        	//Set the found cars in request with name as 'carList'
        	request.setAttribute("carList", cars);
			
        	//Set the destination page to carList.jsp
        	destinationPage="carList.jsp";
            
        }  
        else if(DELETE_CAR_ACTION.equals(actionName))
        {
            //TODO 8 
			//Get the ids of all cars to be deleted from request
        	String ids[] = request.getParameterValues("id");
			
        	//Use appropriate api of DAO to delete all cars
        	carDAO.delete(ids);
			
        	//Get all the Cars using DAO
        	List<CarDTO> cars = carDAO.findAll();
        	
			//Set the found cars in request with name as 'carList'
        	request.setAttribute("carList", cars);
        	
			//Set the destination page to carList.jsp
        	destinationPage="carList.jsp";
            
        }                    
        else
        {
            String errorMessage = "[" + actionName + "] is not a valid action.";
            request.setAttribute(ERROR_KEY, errorMessage);
        }


        //TODO 9 Use appropriate Servlet API to forward the request to 
		//appropriate destination page set in above if else blocks depending on action.
        RequestDispatcher rd = request.getRequestDispatcher(destinationPage);
        System.out.println(destinationPage);
        rd.forward(request, response);
        
    }
}
