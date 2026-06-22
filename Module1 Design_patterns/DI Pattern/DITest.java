
import java.util.HashMap;
import java.util.Map;

interface CustomerRepository {
    String findCustomerById(int id);
}

class CustomerRepositoryImpl implements CustomerRepository {
    private final Map<Integer, String> db = new HashMap<>();

    public CustomerRepositoryImpl() {
        db.put(1, "Alice Johnson");
        db.put(2, "Bob Smith");
        db.put(3, "Charlie Brown");
    }

    public String findCustomerById(int id) {
        return db.getOrDefault(id, "Customer not found");
    }
}

class CustomerService {
    private final CustomerRepository repository;

    public CustomerService(CustomerRepository repository) {
        this.repository = repository;
    }

    public void printCustomer(int id) {
        System.out.println("Customer [ID=" + id + "]: " + repository.findCustomerById(id));
    }
}

public class DITest {
    public static void main(String[] args) {
        CustomerRepository repo    = new CustomerRepositoryImpl();
        CustomerService    service = new CustomerService(repo); // injected!

        service.printCustomer(1);
        service.printCustomer(2);
        service.printCustomer(99);
    }
}