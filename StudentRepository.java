import com.library.exception.ErrorHandling;
import com.library.exception.InvalidInputException;
import com.library.exception.UserNotFoundException;
import com.library.model.User;
import com.library.service.UserService;
import com.library.service.impl.UserServiceImpl;

import java.util.List;

public class StudentRepository {
    private final UserService userService = new UserServiceImpl();

    public void addStudent(Student student) throws ErrorHandling.InvalidInputException {
        try {
            userService.registerUser(student, "student123");
        } catch (InvalidInputException e) {
            throw new ErrorHandling.InvalidInputException(e.getMessage());
        } catch (Exception e) {
            throw new ErrorHandling.InvalidInputException("Database error: " + e.getMessage());
        }
    }

    public User getStudentById(String id) throws ErrorHandling.StudentNotFoundException {
        try {
            return userService.getUserByCode(id);
        } catch (UserNotFoundException e) {
            throw new ErrorHandling.StudentNotFoundException(id);
        } catch (Exception e) {
            throw new ErrorHandling.StudentNotFoundException(id);
        }
    }

    public List<User> getAllStudents() {
        try {
            return userService.getAllUsers();
        } catch (Exception e) {
            return List.of();
        }
    }
}
