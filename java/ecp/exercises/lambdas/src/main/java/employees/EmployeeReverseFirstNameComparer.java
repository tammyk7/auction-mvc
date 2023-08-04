package employees;

import java.util.Comparator;

public class EmployeeReverseFirstNameComparer  implements Comparator<Employee> {
    @Override
    public int compare(Employee o1, Employee o2) {
        return o2.getFirstName().compareTo(o1.getFirstName());
    }
}
