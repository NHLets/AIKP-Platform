package org.afdb.aikp.modules.person.domain.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.afdb.aikp.modules.organization.domain.valueobject.OrganizationId;
import org.afdb.aikp.modules.person.domain.valueobject.PersonFullName;
import org.afdb.aikp.modules.person.domain.valueobject.PersonId;

import org.junit.jupiter.api.Test;

class PersonTest {

    @Test
    void shouldCreateActivePerson() {

        PersonId personId = PersonId.generate();
        OrganizationId organizationId =
                OrganizationId.generate();

        Person person = Person.create(
                personId,
                PersonFullName.of(
                        "John Michael Doe"),
                organizationId);

        assertEquals(
                personId,
                person.getPersonId());

        assertEquals(
                "John Michael Doe",
                person.getFullName().getValue());

        assertEquals(
                organizationId,
                person.getOrganizationId());

        assertTrue(person.isActive());
    }

    @Test
    void shouldRestorePerson() {

        PersonId personId = PersonId.generate();
        OrganizationId organizationId =
                OrganizationId.generate();

        Person person = Person.restore(
                personId,
                PersonFullName.of(
                        "Jane Mary Doe"),
                organizationId,
                false);

        assertEquals(
                personId,
                person.getPersonId());

        assertEquals(
                "Jane Mary Doe",
                person.getFullName().getValue());

        assertEquals(
                organizationId,
                person.getOrganizationId());

        assertFalse(person.isActive());
    }

    @Test
    void shouldChangeFullName() {

        Person person = Person.create(
                PersonId.generate(),
                PersonFullName.of("Initial Name"),
                OrganizationId.generate());

        person.changeFullName(
                PersonFullName.of(
                        "Updated Official Name"));

        assertEquals(
                "Updated Official Name",
                person.getFullName().getValue());
    }

    @Test
    void shouldChangeOrganization() {

        OrganizationId initialOrganizationId =
                OrganizationId.generate();

        OrganizationId newOrganizationId =
                OrganizationId.generate();

        Person person = Person.create(
                PersonId.generate(),
                PersonFullName.of("John Doe"),
                initialOrganizationId);

        person.changeOrganization(
                newOrganizationId);

        assertEquals(
                newOrganizationId,
                person.getOrganizationId());
    }

    @Test
    void shouldDeactivateAndActivatePerson() {

        Person person = Person.create(
                PersonId.generate(),
                PersonFullName.of("John Doe"),
                OrganizationId.generate());

        assertTrue(person.isActive());

        person.deactivate();

        assertFalse(person.isActive());

        person.activate();

        assertTrue(person.isActive());
    }
}
