package sandman.junit;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CustomerDao implements ICustomerDao {

    private static final Logger logger = LogManager.getLogger("CustomerDao");
    private static final String SELECT_CUSTOMERS = """
                        SELECT firstname, lastname, concat(firstname, ' ', lastname) AS fullname 
                        FROM customer;
            """;
    private final Connection connection;
    public CustomerDao(Connection connection) {
        this.connection = connection;
    }

    @Override
    public List<Customer> fetchAllCustomers() throws SQLException {
        logger.traceEntry();
        var customerList = new ArrayList<Customer>();
        try (var statement = connection.createStatement();
             var rs = statement.executeQuery(SELECT_CUSTOMERS)) {

            while (rs.next()) {
                customerList.add(
                        new Customer(rs.getString("fullname"), rs.getString("firstname"), rs.getString("lastname")));
            }

        }
        return logger.traceExit(customerList);
    }
}
