package sandman.junit;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.testcontainers.containers.JdbcDatabaseContainer;
import org.testcontainers.containers.MariaDBContainer;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

@Testcontainers
class CustomerDaoIntegrationTest {

    @Container
    static MariaDBContainer<?> mariaDBContainer = new MariaDBContainer<>("mariadb:11.1.2-jammy")
            .withInitScript("sql/init_db.sql");

    @Container
    static PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres:16.0-alpine3.18")
            .withInitScript("sql/init_db.sql");

    public static Stream<Arguments> customerDaoProvider() throws SQLException {
        return Stream.of(
                Arguments.of(new OldCustomerDao(getContainerDatabaseConnection(postgreSQLContainer))),
                Arguments.of(new CustomerDao(getContainerDatabaseConnection(mariaDBContainer)))
        );
    }

    @ParameterizedTest
    @MethodSource("customerDaoProvider")
    void fetchAllCustomers_shouldReturnFiveCustomers_withFirstnameAndLastnameConcatenatedAsFullname(
            ICustomerDao customerDao) throws SQLException {
        // Act
        var customerList = customerDao.fetchAllCustomers();

        // Assert
        assertThat(customerList).containsExactlyInAnyOrder(
                new Customer("Alice Johnson", "Alice", "Johnson"),
                new Customer("Bob Smith", "Bob", "Smith"),
                new Customer("Charlie Williams", "Charlie", "Williams"),
                new Customer("David Brown", "David", "Brown"),
                new Customer("Eva Wilson", "Eva", "Wilson")
        );
    }

    private static Connection getContainerDatabaseConnection(
            JdbcDatabaseContainer<?> databaseContainer) throws SQLException {
        return DriverManager.getConnection(databaseContainer.getJdbcUrl(), databaseContainer.getUsername(),
                databaseContainer.getPassword());
    }
}
