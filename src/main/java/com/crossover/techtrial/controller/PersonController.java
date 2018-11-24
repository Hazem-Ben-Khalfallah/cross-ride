/**
 *
 */
package com.crossover.techtrial.controller;

import com.crossover.techtrial.model.Person;
import com.crossover.techtrial.service.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

/**
 * @author crossover
 */

@RestController
public class PersonController {

    private final PersonService personService;

    public PersonController(PersonService personService) {
        this.personService = personService;
    }

    /**
     * @should register the persons
     */
    @PostMapping(path = "/api/person")
    public ResponseEntity<Person> register(@RequestBody Person person) {
        return ResponseEntity.ok(personService.save(person));
    }

    /**
     * @should list all persons
     */
    @GetMapping(path = "/api/person")
    public ResponseEntity<List<Person>> getAllPersons() {
        return ResponseEntity.ok(personService.getAll());
    }

    /**
     * @should get a person by id
     * @should return a not found response
     */
    @GetMapping(path = "/api/person/{person-id}")
    public ResponseEntity<Person> getPersonById(@PathVariable(name = "person-id") Long personId) {
        Optional<Person> person = personService.findById(personId);
        return person.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

}
