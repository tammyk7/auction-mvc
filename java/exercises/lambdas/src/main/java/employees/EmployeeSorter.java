package employees;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.function.Function;

public class EmployeeSorter {

    private static List<Employee> getEmployees() {
        List<Employee> employees  = new ArrayList<>();
        employees.add(new Employee(6,"Yash", "Chopra", 25));
        employees.add(new Employee(2,"Aman", "Sharma", 28));
        employees.add(new Employee(3,"Aakash", "Yaadav", 52));
        employees.add(new Employee(5,"David", "Kameron", 19));
        employees.add(new Employee(4,"James", "Hedge", 72));
        employees.add(new Employee(8,"Balaji", "Subbu", 88));
        employees.add(new Employee(7,"Karan", "Johar", 59));
        employees.add(new Employee(1,"Lokesh", "Gupta", 32));
        employees.add(new Employee(9,"Vishu", "Bissi", 33));
        employees.add(new Employee(10,"Lokesh", "Ramachandran", 60));
        return employees;
    }

    public static void sortWithComparatorObjects(List<Employee> employees) {
        // sort by first name
        employees.sort(new EmployeeFirstNameComparer());

        // sort by first name, in reverse order
        employees.sort(new EmployeeReverseFirstNameComparer());

        // sort by last name
        employees.sort(new EmployeeLastNameComparer());

        // sort by firstName, then lastName
        employees.sort(new EmployeeFirstNameThenLastNameComparer());
    }

    public static void printOutEmployeesWithFor(List<Employee> employees) {
       for (var employee : employees) {
           System.out.println(employee);
       }
    }

    public static void printOutEmployeesWithLambda(List<Employee> employees) {
        // print out employees with employees.forEach

    }

    public static void sortWithComparatorLambdas(ArrayList<Employee> employees) {
        // sort using Comparator.comparing

        // sort by first name
        employees.sort(Comparator.comparing(employee -> employee.getFirstName()));

        // sort by first name, in reverse order
        //employees.sort();

        // sort by last name
        //employees.sort();

        // sort by firstName, then lastName
        //employees.sort();
    }

    public static void run() {
        List<Employee> employees  = getEmployees();
        Comparator<Employee> employeeComparator = new EmployeeLastNameComparer();

        sortWithComparatorObjects(employees);
        printOutEmployeesWithFor(employees);

        sortWithComparatorLambdas(employees);
        printOutEmployeesWithLambda(employees);
    }
}
