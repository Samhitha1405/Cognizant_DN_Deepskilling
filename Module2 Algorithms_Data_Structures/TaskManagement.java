class Task {
    int taskId;
    String taskName;
    String status;

    public Task(int taskId, String taskName, String status) {
        this.taskId = taskId;
        this.taskName = taskName;
        this.status = status;
    }

    @Override
    public String toString() {
        return String.format("ID:%-3d | %-12s | %s", taskId, taskName, status);
    }
}

class Node {
    Task data;
    Node next;

    Node(Task data) {
        this.data = data;
        this.next = null;
    }
}

class TaskLinkedList {
    private Node head;
    private int size;

    // O(n) - appends at tail
    public void add(Task task) {
        Node newNode = new Node(task);
        if (head == null) {
            head = newNode;
        } else {
            Node current = head;
            while (current.next != null) {
                current = current.next;
            }
            current.next = newNode;
        }
        size++;
    }

    // O(n) - must walk the chain from head until found
    public Task search(int taskId) {
        Node current = head;
        while (current != null) {
            if (current.data.taskId == taskId) {
                return current.data;
            }
            current = current.next;
        }
        return null;
    }

    // O(n) - visit every node
    public void traverse() {
        Node current = head;
        while (current != null) {
            System.out.println("  " + current.data);
            current = current.next;
        }
    }

    // O(n) - locate node then re-link previous.next to skip it
    public boolean delete(int taskId) {
        if (head == null) return false;

        if (head.data.taskId == taskId) {
            head = head.next;
            size--;
            return true;
        }

        Node previous = head;
        Node current = head.next;
        while (current != null) {
            if (current.data.taskId == taskId) {
                previous.next = current.next;
                size--;
                return true;
            }
            previous = current;
            current = current.next;
        }
        return false;
    }

    public int size() {
        return size;
    }
}

public class TaskManagement {
    public static void main(String[] args) {
        TaskLinkedList tasks = new TaskLinkedList();

        tasks.add(new Task(1, "Design UI", "Pending"));
        tasks.add(new Task(2, "Write Tests", "In Progress"));
        tasks.add(new Task(3, "Deploy App", "Pending"));
        tasks.add(new Task(4, "Code Review", "Done"));

        System.out.println("All tasks (size=" + tasks.size() + "):");
        tasks.traverse();

        System.out.println("\nSearching for task ID 2:");
        Task found = tasks.search(2);
        System.out.println(found != null ? "  Found -> " + found : "  Not found");

        System.out.println("\nDeleting task ID 1 (head):");
        tasks.delete(1);
        tasks.traverse();

        System.out.println("\nDeleting task ID 3 (middle):");
        tasks.delete(3);
        tasks.traverse();

        System.out.println("\nFinal size: " + tasks.size());

        System.out.println("\nSearching for deleted task ID 1:");
        found = tasks.search(1);
        System.out.println(found != null ? "  Found -> " + found : "  Not found");
    }
}