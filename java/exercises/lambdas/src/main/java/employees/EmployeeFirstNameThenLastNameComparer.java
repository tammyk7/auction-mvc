package employees;

import java.util.Comparator;

public class EmployeeFirstNameThenLastNameComparer implements Comparator<Employee> {
    @Override
    public int compare(Employee o1, Employee o2) {
        if (o1.getFirstName().equals(o2.getLastName())) {
            return o1.getLastName().compareTo(o2.getLastName());
        }
        return o1.getFirstName().compareTo(o2.getFirstName());
    }
}
