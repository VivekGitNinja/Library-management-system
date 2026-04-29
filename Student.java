import com.library.model.Role;
import com.library.model.User;

public class Student extends User {
    public Student(String userCode, String name, String email, String phone) {
        super(userCode, name, email, phone, null, Role.STUDENT);
    }
}
