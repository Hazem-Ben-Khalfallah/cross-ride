/**
 * 
 */
package com.crossover.techtrial.service;

import java.util.List;
import java.util.Optional;

import com.crossover.techtrial.model.Person;

/**
 * PersonService interface for Persons.
 * @author cossover
 *
 */
public interface PersonService {
  List<Person> getAll();
  
  Person save(Person p);
  
  Optional<Person> findById(Long personId);

  boolean existsById(Long personId);
}
