package com.zzheads.analizePD;

import com.zzheads.analizePD.model.Country;
import org.apache.commons.math3.stat.correlation.PearsonsCorrelation;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.boot.MetadataSources;
import org.hibernate.service.ServiceRegistry;

import java.math.BigDecimal;
import java.util.*;


public class Main {

    // Hold a reusable reference to a SessionFactory (since we need only one)
    public static final SessionFactory sessionFactory = buildSessionFactory();
    private static SessionFactory buildSessionFactory() {
        final ServiceRegistry registry = new StandardServiceRegistryBuilder().configure().build();
        return new MetadataSources(registry).buildMetadata().buildSessionFactory();
    }

    public static void main(String[] args) {

        //          DONE: Write the application code for a console application that allows a user to view a list of well-formatted data for all countries.
        //          This should be formatted in columns, contain column headings, and numbers should be rounded to the nearest hundredth.
        //          For any data that is unreported (NULL in the database), this should be clear in the displayed table.
        //          Please reference the provided example for data formatting.
        //
        //          DONE: Add to your console application the code that allows a user to view a list of statistics for each indicator,
        //          including (but not limited to) a maximum and minimum for each indicator, and a correlation coefficient for the two indicators together.
        //          You may use a third-party library to calculate the correlation coefficient. Keep in mind that all calculated statistics should exclude
        //          any country that doesn’t have data reported for the indicators under analysis (instead of using zero for missing values).
        //
        //          DONE: Write the application code that allows a user to edit a country’s data.
        //          DONE: Write the application code that allows a user to add a country with data for each indicator.
        //          DONE: Write the application code that allows a user to delete a country’s data.
        //
        //          DONE: Calculate a correlation coefficient between the two indicators without using a third-party library.
        //          Use the builder pattern for creating new country objects.
        //          DONE: Use Java streams for finding maxima and minima.

        int choice = 0;
        while (choice !=5) {
            choice = prompt();
            switch (choice) {
                case 1:
                    addNewData();
                    break;
                case 2:
                    editData();
                    break;
                case 3:
                    deleteData();
                    break;
                case 4:
                    printReport(fetchAllCountries());
                    break;
                case 5:
                    break;
            }
        }
    }

    public static int prompt () {
        Scanner sc = new Scanner(System.in);
        int choice = 0;
        System.out.printf("%n%n1. Enter new data%n");
        System.out.printf("2. Edit data%n");
        System.out.printf("3. Delete data%n");
        System.out.printf("4. Print report%n");
        System.out.printf("5. Exit program%n%n");
        System.out.printf("Enter your choice:");
        while ((choice<1)||(choice>5)) {
            choice = sc.nextInt();
        }
        return choice;
    }

    private static void deleteData() {
        Scanner sc = new Scanner(System.in);
        System.out.printf("%n%n%n Delete record of database:%n");
        System.out.printf("1. Enter code of country:%n");
        String code = sc.nextLine();
        Country country = findCountryById(code);
        if (country != null) {
            System.out.printf("Are you sure you want to delete (%s)?", country);
            String answer = sc.next();
            if (answer.startsWith("y")) {
                delete(country);
                System.out.printf("Record: %s deleted.%n", country);
            }
        } else {
            System.out.printf("Error, can not find country with code %s...%n", code);
        }
    }

    public static void addNewData () {
        Scanner sc = new Scanner(System.in);
        System.out.printf("%n%n%n Enter new data:%n");
        System.out.printf("1. Enter name of country:%n");
        String name = sc.nextLine();
        System.out.printf("2. Enter rate of internet users:%n");
        double internetUsers = sc.nextDouble();
        System.out.printf("2. Enter rate of literacy:%n");
        double literacy = sc.nextDouble();
        Country country = new Country.CountryBuilder(name)
                .withInternetUsers(BigDecimal.valueOf(internetUsers))
                .withAdultLiteracyRate(BigDecimal.valueOf(literacy)).build();
        save(country);
        System.out.printf("Record: %s added.%n", country);
    }

    public static void editData () {
        Scanner sc = new Scanner(System.in);
        System.out.printf("%n%n%n Edit record of database:%n");
        System.out.printf("1. Enter code of country:%n");
        String code = sc.nextLine();
        Country country = findCountryById(code);
        if (country != null) {
            System.out.printf("2. Edit name of country (%s):%n", country.getName());
            country.setName(sc.nextLine());
            System.out.printf("3. Edit rate of internet users (%f):%n",country.getInternetUsers());
            country.setInternetUsers(BigDecimal.valueOf(sc.nextDouble()));
            System.out.printf("4. Edit rate of literacy (%f):%n", country.getAdultLiteracyRate());
            country.setAdultLiteracyRate(BigDecimal.valueOf(sc.nextDouble()));
            update(country);
            System.out.printf("Record: %s updated.%n", country);
        } else {
            System.out.printf("Error, can not find country with code %s...%n", code);
        }
    }

    public static void printReport (List<Country> allCountries) {
        System.out.printf("%n%nReport - All countries : ");
        String div = " | ";
        String tableHeader = div+" Id"+div+"          Country Name          "+div+"Internet Users"+div+"Literacy"+div;
        String rowDiv = "%n========================================================================%n";

        System.out.printf(rowDiv+tableHeader+rowDiv);
        allCountries.stream().forEach(System.out :: println);
        System.out.printf(rowDiv);
        System.out.printf("Country with MIN Internet Users: %-29s - %5.2f%s%n", findMinInternet(allCountries).getName(), findMinInternet(allCountries).getInternetUsers(),"%");
        System.out.printf("Country with MAX Internet Users: %-29s - %5.2f%s%n", findMaxInternet(allCountries).getName(), findMaxInternet(allCountries).getInternetUsers(),"%");
        System.out.printf("Country with MIN Literacy Rate: %-30s - %5.2f%s%n", findMinLiteracy(allCountries).getName(), findMinLiteracy(allCountries).getAdultLiteracyRate(),"%");
        System.out.printf("Country with MAX Literacy Rate: %-30s - %5.2f%s", findMaxLiteracy(allCountries).getName(), findMaxLiteracy(allCountries).getAdultLiteracyRate(),"%");
        System.out.printf(rowDiv);
        System.out.printf("%nPearsons correlation                                            - %5.2f", correlation(allCountries));
        System.out.printf("%nCorrelation from Apache Math3                                   - %5.2f", correlationFromApacheMath(allCountries));
    }


    // Pearsons Correlation from Math3 for checking
    public static double correlationFromApacheMath (List<Country> countries) {
        int count =  (int) countries.stream().filter(p->p.getInternetUsers()!=null&&p.getAdultLiteracyRate()!=null).count();
        double [] x = new double [count];
        double [] y = new double [count];
        count = 0;
        for (int i=0;i<countries.size();i++) {
            if ((countries.get(i).getInternetUsers()!=null)&&(countries.get(i).getAdultLiteracyRate()!=null)) {
                x[count] = countries.get(i).getInternetUsers().doubleValue();
                y[count] = countries.get(i).getAdultLiteracyRate().doubleValue();
                count++;
            }
        }
        PearsonsCorrelation correlation = new PearsonsCorrelation();
        return correlation.correlation(x,y);
    }


    public static double correlation (List<Country> countries) {
        // Counts linear correlation - Pearson correlation
        // https://en.wikipedia.org/wiki/Correlation_and_dependence
        //
        ArrayList<Double> x = new ArrayList<>();
        ArrayList<Double> y = new ArrayList<>();
        double summX = 0;
        double summY = 0;

        for (Country c : countries) {
            if ((c.getInternetUsers()!=null) && (c.getAdultLiteracyRate()!=null)) {
                x.add(c.getInternetUsers().doubleValue());
                y.add(c.getAdultLiteracyRate().doubleValue());
                summX+=c.getInternetUsers().doubleValue();
                summY+=c.getAdultLiteracyRate().doubleValue();
            }
        }

        double averageX = summX/x.size();
        double averageY = summY/y.size();

        double standartDeviationX = 0;
        double sum = 0;
        for (Double aX : x) {
            sum += (aX - averageX) * (aX - averageX);
        }
        standartDeviationX = Math.sqrt(sum);

        double standartDeviationY = 0;
        sum = 0;
        for (Double aY : y) {
            sum += (aY - averageY) * (aY - averageY);
        }
        standartDeviationY = Math.sqrt(sum);

        sum = 0;
        for (int i=0;i<x.size();i++) {
            sum += (x.get(i) - averageX)*(y.get(i) - averageY);
        }

        return sum/(standartDeviationX*standartDeviationY);
    }

    private static Country findMaxInternet (List<Country> countries) {
        return countries
                .stream()
                .filter(p -> p.getInternetUsers()!=null)
                .max((c1, c2) -> (c1.getInternetUsers().compareTo(c2.getInternetUsers())))
                .get();
    }

    private static Country findMinInternet (List<Country> countries) {
        return countries
                .stream()
                .filter(p -> p.getInternetUsers()!=null)
                .min((c1, c2) -> (c1.getInternetUsers().compareTo(c2.getInternetUsers())))
                .get();
    }

    private static Country findMaxLiteracy (List<Country> countries) {
        return countries
                .stream()
                .filter(p -> p.getAdultLiteracyRate()!=null)
                .max((c1, c2) -> (c1.getAdultLiteracyRate().compareTo(c2.getAdultLiteracyRate())))
                .get();
    }

    private static Country findMinLiteracy (List<Country> countries) {
        return countries
                .stream()
                .filter(p -> p.getAdultLiteracyRate()!=null)
                .min((c1, c2) -> (c1.getAdultLiteracyRate().compareTo(c2.getAdultLiteracyRate())))
                .get();
    }

    private static Country findCountryById (String id) {
        Session session = sessionFactory.openSession();
        Country country = session.get(Country.class, id);
        session.close();
        return  country;
    }

    private static void update (Country country) {
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        session.update(country);
        session.getTransaction().commit();
        session.close();
    }

    private static void delete (Country country) {
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        session.delete(country);
        session.getTransaction().commit();
        session.close();
    }

    @SuppressWarnings("unchecked")
    private static List<Country> fetchAllCountries() {
        Session session = sessionFactory.openSession();
        Criteria criteria = session.createCriteria(Country.class);
        List<Country> countries = criteria.list();
        session.close();
        return countries;
    }

    private static String save(Country country){
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        String id = (String) session.save(country);
        session.getTransaction().commit();
        session.close();
        return id;
    }


}
