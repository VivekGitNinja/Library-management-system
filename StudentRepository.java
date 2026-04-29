import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StudentRepository {
    private final Map<String, Student> students = new HashMap<>();

    public void addStudent(Student student) throws ErrorHandling.InvalidInputException {
        if (student == null || student.getId() == null || student.getId().trim().isEmpty()) {
            throw new ErrorHandling.InvalidInputException("Student ID cannot be empty.");
        }
        if (students.containsKey(student.getId())) {
            throw new ErrorHandling.InvalidInputException("Student with ID '" + student.getId() + "' already exists.");
        }
        students.put(student.getId(), student);
    }

    public Student getStudentById(String id) throws ErrorHandling.StudentNotFoundException {
        Student student = students.get(id);
        if (student == null) {
            throw new ErrorHandling.StudentNotFoundException(id);
        }
        return student;
    }

    public List<Student> getAllStudents() {
        return new ArrayList<>(students.values());
    }

    public boolean removeStudent(String id) throws ErrorHandling.StudentNotFoundException {
        if (!students.containsKey(id)) {
            throw new ErrorHandling.StudentNotFoundException(id);
        }
        students.remove(id);
        return true;
    }
}
