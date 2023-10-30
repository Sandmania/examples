package sandman.junit;

import java.sql.SQLException;
import java.util.List;

public interface ICustomerDao {
    
    List<Customer> fetchAllCustomers() throws SQLException;
}
