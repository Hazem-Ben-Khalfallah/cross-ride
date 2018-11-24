/**
 *
 */
package com.crossover.techtrial.controller;

import com.crossover.techtrial.model.Person;
import com.crossover.techtrial.repositories.PersonRepository;
import com.crossover.techtrial.utils.DateUtils;
import com.crossover.techtrial.utils.JsonSerializer;
import org.assertj.core.api.Assertions;
import org.assertj.core.util.Lists;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

/**
 * @author kshah
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class PersonControllerTest extends AbstractControllerTest {

    MockMvc mockMvc;
    @Autowired
    PersonRepository personRepository;
    @Mock
    private PersonController personController;
    @Autowired
    private TestRestTemplate template;
    @Autowired
    private JsonSerializer jsonSerializer;

    @Before
    public void setup() throws Exception {
        mockMvc = MockMvcBuilders.standaloneSetup(personController).build();
        //Delete all persons
        personRepository.deleteAll();
    }

    /**
     * @verifies register the persons
     * @see PersonController#register(Person)
     */
    @Test
    public void register_shouldRegisterThePersons() throws Exception {
        // given
        final Person person = Person.newBuilder() //
                .setName("test 1") //
                .setEmail("test10000000000001@gmail.com") //
                .setRegistrationNumber("41DCT") //
                .setRegistrationDate(DateUtils.now()) //
                .build();
        final HttpEntity<Object> httpEntity = getHttpEntity(jsonSerializer.serialize(person));
        // when
        final ResponseEntity<Person> response = template.postForEntity("/api/person", //
                httpEntity, Person.class);

        // then
        Assertions.assertThat(response).isNotNull();
        Assertions.assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
        Assertions.assertThat(response.getBody()).isNotNull();
        Assertions.assertThat(response.getBody().getId()).isNotNull();
        Assertions.assertThat(response.getBody().getName()).isEqualTo(person.getName());
        Assertions.assertThat(response.getBody().getEmail()).isEqualTo(person.getEmail());
        Assertions.assertThat(response.getBody().getRegistrationNumber()).isEqualTo(person.getRegistrationNumber());
        Assertions.assertThat(response.getBody().getRegistrationDate()).isEqualTo(person.getRegistrationDate());
    }

    /**
     * @verifies list all persons
     * @see PersonController#getAllPersons()
     */
    @Test
    public void getAllPersons_shouldListAllPersons() throws Exception {
        // given
        final List<Person> persons = Lists.newArrayList();
        persons.add(Person.newBuilder()
                .setEmail("user1@mail.com")
                .build());
        persons.add(Person.newBuilder()
                .setEmail("user2@mail.com")
                .build());
        personRepository.saveAll(persons);

        // when
        final ResponseEntity<List<Person>> response = template.exchange("/api/person", //
                HttpMethod.GET, null,  //
                new ParameterizedTypeReference<List<Person>>() {
                });

        // then
        Assertions.assertThat(response).isNotNull();
        Assertions.assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
        Assertions.assertThat(response.getBody()).isNotEmpty();
        Assertions.assertThat(response.getBody()).hasSize(2);
    }

    /**
     * @verifies get a person by id
     * @see PersonController#getPersonById(Long)
     */
    @Test
    public void getPersonById_shouldGetAPersonById() throws Exception {
        // given
        final Person newPerson = personRepository.save(Person.newBuilder()
                .setEmail("user1@mail.com")
                .build());

        // when
        final ResponseEntity<Person> response = template.getForEntity("/api/person/{personId}", //
                Person.class, newPerson.getId());

        // then
        Assertions.assertThat(response).isNotNull();
        Assertions.assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
        Assertions.assertThat(response.getBody()).isNotNull();
        Assertions.assertThat(response.getBody()).isEqualTo(newPerson);
    }

    /**
     * @verifies return a not found response
     * @see PersonController#getPersonById(Long)
     */
    @Test
    public void getPersonById_shouldReturnANotFoundResponse() throws Exception {
        // when
        final ResponseEntity<Person> response = template.getForEntity("/api/person/{personId}", //
                Person.class, "123");

        // then
        Assertions.assertThat(response).isNotNull();
        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

}
