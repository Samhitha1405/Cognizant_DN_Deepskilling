class Student {
    private String name, id, grade;
    public Student(String name, String id, String grade) {
        this.name = name; this.id = id; this.grade = grade;
    }
    public String getName()  { return name; }
    public String getId()    { return id; }
    public String getGrade() { return grade; }
    public void setName(String n)  { this.name  = n; }
    public void setGrade(String g) { this.grade = g; }
}


class StudentView {
    public void displayStudentDetails(String name, String id, String grade) {
        System.out.println("=== Student Details ===");
        System.out.println("Name  : " + name);
        System.out.println("ID    : " + id);
        System.out.println("Grade : " + grade);
        System.out.println("=======================");
    }
}

class StudentController {
    private final Student model;
    private final StudentView view;

    public StudentController(Student model, StudentView view) {
        this.model = model; this.view = view;
    }

    public void setStudentName(String n)  { model.setName(n); }
    public void setStudentGrade(String g) { model.setGrade(g); }

    public void updateView() {
        view.displayStudentDetails(model.getName(), model.getId(), model.getGrade());
    }
}


public class MVCTest {
    public static void main(String[] args) {
        Student student       = new Student("Arjun Sharma", "STU001", "A");
        StudentView view      = new StudentView();
        StudentController ctl = new StudentController(student, view);

        System.out.println("Before update:");
        ctl.updateView();

        ctl.setStudentName("Arjun Kumar Sharma");
        ctl.setStudentGrade("A+");

        System.out.println("\nAfter update:");
        ctl.updateView();
    }
}