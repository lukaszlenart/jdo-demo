package org.demo;

import org.testng.annotations.Test;

import java.util.List;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

public class PersonPersisterTest {

    @Test
    public void shouldPersitPerson() throws Exception {
        // given
        PersonPersister persister = new PersonPersister();
        Person person = new Person("Jasiek", 20);

        // when
        Person actual = persister.persist(person);

        // then
        assertEquals(actual, persister.read(actual.getId()));
        persister.delete(actual.getId());
    }

    @Test
    public void shouldListPersons() throws Exception {
        // given
        PersonPersister persister = new PersonPersister();
        persister.persist(new Person("Jasiek", 20));
        persister.persist(new Person("Danka", 34));
        persister.persist(new Person("Mietek", 18));

        // when
        List<Person> actual = persister.list();

        // then
        assertNotNull(actual);
        assertTrue(actual.size() == 3, "Expected 3 but was " + actual.size() + " [" + actual + "]");
        for (Person person : actual) {
            persister.delete(person.getId());
        }
    }
}
