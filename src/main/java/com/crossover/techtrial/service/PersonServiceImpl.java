/**
 *
 */
package com.crossover.techtrial.service;

import com.crossover.techtrial.model.Person;
import com.crossover.techtrial.repositories.PersonRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * @author crossover
 */
@Service
public class PersonServiceImpl implements PersonService {

    private final PersonRepository personRepository;

    public PersonServiceImpl(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    /* (non-Javadoc)
     * @see com.crossover.techtrial.service.PersonService#getAll()
     */
    @Override
    public List<Person> getAll() {
        List<Person> personList = new ArrayList<>();
        personRepository.findAll().forEach(personList::add);
        return personList;

    }

    public Person save(Person p) {
        return personRepository.save(p);
    }

    @Override
    public Optional<Person> findById(Long personId) {
        return personRepository.findById(personId);
    }

    @Override
    public boolean existsById(Long personId) {
        return personRepository.existsById(personId);
    }


}
