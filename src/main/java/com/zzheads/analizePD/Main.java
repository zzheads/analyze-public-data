package com.zzheads.analizePD;

import com.zzheads.analizePD.model.Country;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.boot.MetadataSources;
import org.hibernate.service.ServiceRegistry;

import java.util.List;


public class Main {
    public static final String USERNAME_FORDB = "sa";
    public static final String PASSWORD_FORDB = "";

    // Hold a reusable reference to a SessionFactory (since we need only one)
    public static final SessionFactory sessionFactory = buildSessionFactory();
    private static SessionFactory buildSessionFactory() {
        final ServiceRegistry registry = new StandardServiceRegistryBuilder().configure().build();
        return new MetadataSources(registry).buildMetadata().buildSessionFactory();
    }

    public static void main(String[] args) {

        //        TODO: Write the application code for a console application that allows a user to view a list of well-formatted data for all countries.
        //                      This should be formatted in columns, contain column headings, and numbers should be rounded to the nearest hundredth.
        //                      For any data that is unreported (NULL in the database), this should be clear in the displayed table.
        //                      Please reference the provided example for data formatting.

        //        TODO: Add to your console application the code that allows a user to view a list of statistics for each indicator,
        //                      including (but not limited to) a maximum and minimum for each indicator, and a correlation coefficient for the two indicators together.
        //                      You may use a third-party library to calculate the correlation coefficient. Keep in mind that all calculated statistics should exclude
        //                      any country that doesn’t have data reported for the indicators under analysis (instead of using zero for missing values).

        //        TODO: Write the application code that allows a user to edit a country’s data.

        //        TODO: Write the application code that allows a user to add a country with data for each indicator.

        //        TODO: Write the application code that allows a user to delete a country’s data.

        System.out.printf("%n%nAll countries: %n%n");
        fetchAllCountries().stream().forEach(System.out :: println);

    }


    private static Country findCountryById (int id) {
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

    private static int save(Country country){
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        int id = (int) session.save(country);
        session.getTransaction().commit();
        session.close();
        return id;
    }


}
