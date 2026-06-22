class Employee {
    int employeeId;
    String name;
    String position;
    double salary;

    public Employee(int employeeId, String name, String position, double salary) {
        this.employeeId = employeeId;
        this.name = name;
        this.position = position;
        this.salary = salary;
    }

    @Override
    public String toString() {
        return String.format("ID:%-3d | %-8s | %-10s | $%.2f", employeeId, name, position, salary);
    }
}

class EmployeeArray {
    private Employee[] employees;
    private int count;
    private int capacity;

    public EmployeeArray(int capacity) {
        this.capacity = capacity;
        this.employees = new Employee[capacity];
        this.count = 0;
    }

    // O(1) amortized - direct index assignment
    public boolean add(Employee e) {
        if (count >= capacity) {
            System.out.println("Add failed: array is full (capacity " + capacity + ")");
            return false;
        }
        employees[count] = e;
        count++;
        return true;
    }

    // O(n) - must scan until match is found
    public Employee search(int employeeId) {
        for (int i = 0; i < count; i++) {
            if (employees[i].employeeId == employeeId) {
                return employees[i];
            }
        }
        return null;
    }

    // O(n) - must visit every element
    public void traverse() {
        for (int i = 0; i < count; i++) {
            System.out.println("  " + employees[i]);
        }
    }

    // O(n) - find index then shift subsequent elements left
    public boolean delete(int employeeId) {
        for (int i = 0; i < count; i++) {
            if (employees[i].employeeId == employeeId) {
                for (int j = i; j < count - 1; j++) {
                    employees[j] = employees[j + 1];
                }
                employees[count - 1] = null;
                count--;
                return true;
            }
        }
        return false;
    }
}

public class EmployeeManagement {
    public static void main(String[] args) {
        EmployeeArray staff = new EmployeeArray(5);

        staff.add(new Employee(1, "Nina", "Manager", 85000));
        staff.add(new Employee(2, "Raj", "Developer", 70000));
        staff.add(new Employee(3, "Sofia", "Designer", 65000));

        System.out.println("All employees:");
        staff.traverse();

        System.out.println("\nSearching for ID 2:");
        Employee found = staff.search(2);
        System.out.println(found != null ? "  Found -> " + found : "  Not found");

        System.out.println("\nDeleting ID 2:");
        boolean deleted = staff.delete(2);
        System.out.println("  Deleted: " + deleted);

        System.out.println("\nAll employees after delete:");
        staff.traverse();

        System.out.println("\nSearching for deleted ID 2:");
        found = staff.search(2);
        System.out.println(found != null ? "  Found -> " + found : "  Not found");

        // Demonstrate the fixed-size limitation (capacity is 5)
        System.out.println("\nFilling remaining capacity:");
        staff.add(new Employee(4, "Liam", "Analyst", 60000));
        staff.add(new Employee(5, "Maya", "Intern", 35000));
        staff.add(new Employee(6, "Theo", "Sales", 55000));   // capacity full now
        staff.add(new Employee(7, "Zara", "Support", 48000)); // this one fails

        System.out.println("Final list (capacity reached):");
        staff.traverse();
    }
}