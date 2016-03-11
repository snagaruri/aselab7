package com.mongo.service.example;


import java.io.BufferedReader;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Path;

import org.json.*;
//import com.ibm.json.java.JSON;
//import com.ibm.json.java.JSONObject;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.WriteResult;

/**
 * Servlet implementation class UserServlet
 */
@WebServlet("/addPerson")
public class AddPersonServlet extends HttpServlet {
 
    private static final long serialVersionUID = -7060758261496829905L;
 
    protected void doPost(HttpServletRequest request,
            HttpServletResponse response) throws ServletException, IOException {
        String name = request.getParameter("name");
        String country = request.getParameter("country");
        if ((name == null || name.equals(""))
                || (country == null || country.equals(""))) {
            request.setAttribute("error", "Mandatory Parameters Missing");
            RequestDispatcher rd = getServletContext().getRequestDispatcher(
                    "/persons.jsp");
            rd.forward(request, response);
        } else {
            Person p = new Person();
            p.setCountry(country);
            p.setName(name);
            MongoClient mongo = (MongoClient) request.getServletContext()
                    .getAttribute("MONGO_CLIENT");
            MongoDBPersonDAO personDAO = new MongoDBPersonDAO(mongo);
            personDAO.createPerson(p);
            System.out.println("Person Added Successfully with id="+p.getId());
            request.setAttribute("success", "Person Added Successfully");
            List<Person> persons = personDAO.readAllPerson();
            request.setAttribute("persons", persons);
 
            RequestDispatcher rd = getServletContext().getRequestDispatcher(
                    "/persons.jsp");
            rd.forward(request, response);
        }
    }
 
}


@WebServlet("/editPerson")
public class EditPersonServlet extends HttpServlet {
 
    private static final long serialVersionUID = -6554920927964049383L;
 
    protected void doGet(HttpServletRequest request,
            HttpServletResponse response) throws ServletException, IOException {
        String id = request.getParameter("id");
        if (id == null || "".equals(id)) {
            throw new ServletException("id missing for edit operation");
        }
        System.out.println("Person edit requested with id=" + id);
        MongoClient mongo = (MongoClient) request.getServletContext()
                .getAttribute("MONGO_CLIENT");
        MongoDBPersonDAO personDAO = new MongoDBPersonDAO(mongo);
        Person p = new Person();
        p.setId(id);
        p = personDAO.readPerson(p);
        request.setAttribute("person", p);
        List<Person> persons = personDAO.readAllPerson();
        request.setAttribute("persons", persons);
 
        RequestDispatcher rd = getServletContext().getRequestDispatcher(
                "/persons.jsp");
        rd.forward(request, response);
    }
 
    protected void doPost(HttpServletRequest request,
            HttpServletResponse response) throws ServletException, IOException {
        String id = request.getParameter("id"); // keep it non-editable in UI
        if (id == null || "".equals(id)) {
            throw new ServletException("id missing for edit operation");
        }
 
        String name = request.getParameter("name");
        String country = request.getParameter("country");
 
        if ((name == null || name.equals(""))
                || (country == null || country.equals(""))) {
            request.setAttribute("error", "Name and Country Can't be empty");
            MongoClient mongo = (MongoClient) request.getServletContext()
                    .getAttribute("MONGO_CLIENT");
            MongoDBPersonDAO personDAO = new MongoDBPersonDAO(mongo);
            Person p = new Person();
            p.setId(id);
            p.setName(name);
            p.setCountry(country);
            request.setAttribute("person", p);
            List<Person> persons = personDAO.readAllPerson();
            request.setAttribute("persons", persons);
 
            RequestDispatcher rd = getServletContext().getRequestDispatcher(
                    "/persons.jsp");
            rd.forward(request, response);
        } else {
            MongoClient mongo = (MongoClient) request.getServletContext()
                    .getAttribute("MONGO_CLIENT");
            MongoDBPersonDAO personDAO = new MongoDBPersonDAO(mongo);
            Person p = new Person();
            p.setId(id);
            p.setName(name);
            p.setCountry(country);
            personDAO.updatePerson(p);
            System.out.println("Person edited successfully with id=" + id);
            request.setAttribute("success", "Person edited successfully");
            List<Person> persons = personDAO.readAllPerson();
            request.setAttribute("persons", persons);
 
            RequestDispatcher rd = getServletContext().getRequestDispatcher(
                    "/persons.jsp");
            rd.forward(request, response);
        }
    }
 
}


@WebServlet("/deletePerson")
public class DeletePersonServlet extends HttpServlet {
 
    private static final long serialVersionUID = 6798036766148281767L;
 
    protected void doGet(HttpServletRequest request,
            HttpServletResponse response) throws ServletException, IOException {
        String id = request.getParameter("id");
        if (id == null || "".equals(id)) {
            throw new ServletException("id missing for delete operation");
        }
        MongoClient mongo = (MongoClient) request.getServletContext()
                .getAttribute("MONGO_CLIENT");
        MongoDBPersonDAO personDAO = new MongoDBPersonDAO(mongo);
        Person p = new Person();
        p.setId(id);
        personDAO.deletePerson(p);
        System.out.println("Person deleted successfully with id=" + id);
        request.setAttribute("success", "Person deleted successfully");
        List<Person> persons = personDAO.readAllPerson();
        request.setAttribute("persons", persons);
 
        RequestDispatcher rd = getServletContext().getRequestDispatcher(
                "/persons.jsp");
        rd.forward(request, response);
    }
 
}
	@Override
	protected void doOptions(HttpServletRequest arg0, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		super.doOptions(arg0, response);

		response.setHeader("Access-Control-Allow-Origin", "*");
		response.setHeader("Access-Control-Allow-Methods", "GET, POST, DELETE, HEAD, OPTIONS");
		response.setHeader("Access-Control-Allow-Headers", "Content-Type");
		response.setHeader("Access-Control-Max-Age", "86400");
	}


