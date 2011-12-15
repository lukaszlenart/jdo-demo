package org.demo;

import javax.jdo.JDOHelper;
import javax.jdo.PersistenceManager;
import javax.jdo.PersistenceManagerFactory;
import javax.jdo.Query;
import javax.jdo.Transaction;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Properties;

public class PersonPersister {

    public static void main(String[] args) {
        if (args == null || args.length == 0) {
            System.out.println("Usage:");
            System.out.println("To add a new Person: java PersonPersister add <PersonName> <PersonAge>");
            System.out.println("To list already defined persons: java PersonPersister list");
            System.out.println("To delete already defined persons: java PersonPersister delete <id>");
        }
        if (args.length == 1 && "list".equals(args[0])) {
            PersonPersister persister = new PersonPersister();
            System.out.println("Results:");
            for (Person person : persister.list()) {
                System.out.println(person);
            }
        }
        if (args.length == 2 && "delete".equals(args[0])) {
            PersonPersister persister = new PersonPersister();
            persister.delete(Long.parseLong(args[1]));
            System.out.println("Person of id [" + args[1] + "] was deleted!");
        }
        if (args.length == 3 && "add".equals(args[0])) {
            String name = args[1];
            int age = Integer.parseInt(args[2]);
            PersonPersister persister = new PersonPersister();
            System.out.println("Person added: " + persister.persist(new Person(name, age)));
        }
    }

    public List<Person> list() {
        PersistenceManager pm = createPM();

        try {
            Transaction tx = pm.currentTransaction();
            tx.begin();

            Query query = pm.newQuery(Person.class);
            List<Person> list = (List<Person>) query.execute();

            tx.commit();
            return list;
        } catch (Exception e) {
            if (pm.currentTransaction().isActive()) {
                pm.currentTransaction().rollback();
            }
            e.printStackTrace();
            return Collections.emptyList();
        } finally {
            pm.close();
        }
    }

    public Person persist(Person person) {

        PersistenceManager pm = createPM();

        try {
            Transaction tx = pm.currentTransaction();
            tx.begin();

            Person persisted = pm.makePersistent(person);

            tx.commit();
            return persisted;
        } catch (Exception e) {
            if (pm.currentTransaction().isActive()) {
                pm.currentTransaction().rollback();
            }
            e.printStackTrace();
            return null;
        } finally {
            pm.close();
        }
    }

    public Person read(long id) {

        PersistenceManager pm = createPM();

        try {
            Transaction tx = pm.currentTransaction();
            tx.begin();

            Person person = pm.getObjectById(Person.class, id);

            tx.commit();
            return person;
        } catch (Exception e) {
            if (pm.currentTransaction().isActive()) {
                pm.currentTransaction().rollback();
            }
            e.printStackTrace();
            return null;
        } finally {
            pm.close();
        }
    }

    public void delete(long id) {

        PersistenceManager pm = createPM();

        try {
            Transaction tx = pm.currentTransaction();
            tx.begin();

            Person person = pm.getObjectById(Person.class, id);

            pm.deletePersistent(person);
            tx.commit();
        } catch (Exception e) {
            if (pm.currentTransaction().isActive()) {
                pm.currentTransaction().rollback();
            }
            e.printStackTrace();
        } finally {
            pm.close();
        }
    }

    private PersistenceManager createPM() {
        Properties properties = new Properties();
        try {
            properties.load(getClass().getResource("/jdo.properties").openStream());
        } catch (IOException e) {
            throw new RuntimeException("File 'jdo.properties' not found");
        }
        PersistenceManagerFactory pmf = JDOHelper.getPersistenceManagerFactory(properties);
        return pmf.getPersistenceManager();
    }

}
