/**
 *
 */
package com.crossover.techtrial.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;


/**
 * @author crossover
 */
@Entity
@Table(name = "person")
public class Person implements Serializable {

    private static final long serialVersionUID = 7401548380514451401L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @NotNull
    @Email
    @Column(name = "email")
    private String email;

    @Column(name = "registration_number")
    private String registrationNumber;

    @Column(name = "registration_date")
    private Instant registrationDate;

    public Person() {
    }

    private Person(Builder builder) {
        setId(builder.id);
        setName(builder.name);
        setEmail(builder.email);
        setRegistrationNumber(builder.registrationNumber);
        setRegistrationDate(builder.registrationDate);
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRegistrationNumber() {
        return registrationNumber;
    }

    public void setRegistrationNumber(String registrationNumber) {
        this.registrationNumber = registrationNumber;
    }

    public Instant getRegistrationDate() {
        return registrationDate;
    }

    public void setRegistrationDate(Instant registrationDate) {
        this.registrationDate = registrationDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Person person = (Person) o;
        return Objects.equals(id, person.id) &&
                Objects.equals(name, person.name) &&
                Objects.equals(email, person.email) &&
                Objects.equals(registrationNumber, person.registrationNumber) &&
                Objects.equals(registrationDate, person.registrationDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, email, registrationNumber, registrationDate);
    }


    public static final class Builder {
        private Long id;
        private String name;
        private @NotNull @Email String email;
        private String registrationNumber;
        private Instant registrationDate;

        private Builder() {
        }

        public Builder setId(Long id) {
            this.id = id;
            return this;
        }

        public Builder setName(String name) {
            this.name = name;
            return this;
        }

        public Builder setEmail(@NotNull @Email String email) {
            this.email = email;
            return this;
        }

        public Builder setRegistrationNumber(String registrationNumber) {
            this.registrationNumber = registrationNumber;
            return this;
        }

        public Builder setRegistrationDate(Instant registrationDate) {
            this.registrationDate = registrationDate;
            return this;
        }

        public Person build() {
            return new Person(this);
        }
    }
}
