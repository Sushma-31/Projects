package bank.controller;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import bank.dao.CustomerDAO;
import bank.model.Customer;

@WebServlet("/CustomerRegisterServlet")
public class CustomerRegisterServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String fullName = request.getParameter("fullname");
        String address = request.getParameter("address");
        String mobileNo = request.getParameter("mobileno");
        String emailId = request.getParameter("email");
        String accountType = request.getParameter("type");
        String dob = request.getParameter("DOB");
        String idProof = request.getParameter("aadhar");
        String accountNo = request.getParameter("accountno");
        String password = request.getParameter("password");
        
        // Validate the inputs (example validation)
        if(fullName == null || fullName.isEmpty() || 
           emailId == null || emailId.isEmpty() || 
           idProof == null || idProof.isEmpty() || 
           accountNo == null || accountNo.isEmpty() || 
           password == null || password.isEmpty()) {
            request.setAttribute("errorMessage", "All fields marked as required must be filled.");
            request.getRequestDispatcher("admindashboard.jsp").forward(request, response);
            return;
        }
        
        // Save to database
        try {
            CustomerDAO customerDAO = new CustomerDAO();
            Customer customer = new Customer();
            customerDAO.insertCustomer(customer);
            response.sendRedirect("success.jsp");
        } catch (SQLException e) {
            e.printStackTrace();
            request.setAttribute("errorMessage", "Error registering customer. Please try again.");
            request.getRequestDispatcher("customerregister.jsp").forward(request, response);
        }
    }
}
