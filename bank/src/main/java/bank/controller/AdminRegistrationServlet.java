package bankcontrollers;

import bank.dao.DatabaseConnection;
import java.io.IOException;
import java.security.SecureRandom;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/approveCustomer")
public class AdminRegistrationServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String customerId = request.getParameter("customerId");

        try {
            DatabaseConnection db = new DatabaseConnection();

            String selectSql = "SELECT * FROM pending_customers WHERE id = ?";
            PreparedStatement selectStatement = db.createPreparedStatement(selectSql);
            selectStatement.setString(1, customerId);
            ResultSet resultSet = db.executeQuery(selectStatement);

            if (resultSet.next()) {
                String fullName = resultSet.getString("full_name");
                String address = resultSet.getString("address");
                String mobileNo = resultSet.getString("mobile_no");
                String emailId = resultSet.getString("email_id");
                String accountType = resultSet.getString("account_type");
                double initialBalance = resultSet.getDouble("initial_balance");
                String dob = resultSet.getString("dob");
                String idProof = resultSet.getString("id_proof");

                String accountNo = generateAccountNo();
                String tempPassword = generateTempPassword();

                String insertSql = "INSERT INTO customer (account_no, full_name, address, mobile_no, email_id, account_type, balance, dob, id_proof, password) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
                PreparedStatement insertStatement = db.createPreparedStatement(insertSql);
                insertStatement.setString(1, accountNo);
                insertStatement.setString(2, fullName);
                insertStatement.setString(3, address);
                insertStatement.setString(4, mobileNo);
                insertStatement.setString(5, emailId);
                insertStatement.setString(6, accountType);
                insertStatement.setDouble(7, initialBalance);
                insertStatement.setString(8, dob);
                insertStatement.setString(9, idProof);
                insertStatement.setString(10, tempPassword);

                insertStatement.executeUpdate();

                String deleteSql = "DELETE FROM pending_customers WHERE id = ?";
                PreparedStatement deleteStatement = db.createPreparedStatement(deleteSql);
                deleteStatement.setString(1, customerId);
                deleteStatement.executeUpdate();

                response.sendRedirect("admin_dashboard.jsp");
            } else {
                response.setContentType("text/html");
                response.getWriter().println("<html><body>");
                response.getWriter().println("<h3>Customer not found. Please try again.</h3>");
                response.getWriter().println("<a href='admin_dashboard.jsp'>Back to Dashboard</a>");
                response.getWriter().println("</body></html>");
            }

            db.closeConnection();
        } catch (SQLException | ClassNotFoundException e) {
            throw new ServletException("Database access error", e);
        }
    }

    private String generateAccountNo() {
        return String.valueOf((long) (Math.random() * 10000000000L));
    }

    private String generateTempPassword() {
        SecureRandom random = new SecureRandom();
        int password = 100000 + random.nextInt(900000);
        return String.valueOf(password);
    }
}
