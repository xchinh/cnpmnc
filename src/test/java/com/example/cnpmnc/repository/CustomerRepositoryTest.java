package com.example.cnpmnc.repository;


import com.example.cnpmnc.entity.Customer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest // Tự động config in-memory database cho test
class CustomerRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private CustomerRepository customerRepository;

    private Customer testCustomer;

    @BeforeEach
    void setUp() {
        // Tạo test data
        testCustomer = new Customer();
        testCustomer.setName("Test Customer");
        testCustomer.setEmail("test@example.com");
        testCustomer.setPhone("0901234567");
        testCustomer.setCompany("Test Company");
        testCustomer.setNotes("Test notes");
        testCustomer.setTeamId(1L);
        testCustomer.setCreatedBy(1L);
    }

    @Test
    void testSaveCustomer() {
        // When
        Customer saved = customerRepository.save(testCustomer);

        // Then
        assertThat(saved.getId()).isNotNull();
        assertThat(saved.getName()).isEqualTo("Test Customer");
        assertThat(saved.getEmail()).isEqualTo("test@example.com");
    }

    @Test
    void testFindByIdAndDeletedAtIsNull() {
        // Given
        Customer saved = entityManager.persistAndFlush(testCustomer);

        // When
        Optional<Customer> found = customerRepository.findByIdAndDeletedAtIsNull(saved.getId());

        // Then
        assertThat(found).isPresent();
        assertThat(found.get().getName()).isEqualTo("Test Customer");
    }

    @Test
    void testFindByDeletedAtIsNull() {
        // Given
        entityManager.persist(testCustomer);
        
        Customer deletedCustomer = new Customer();
        deletedCustomer.setName("Deleted Customer");
        deletedCustomer.setEmail("deleted@example.com");
        deletedCustomer.setTeamId(1L);
        deletedCustomer.setCreatedBy(1L);
        deletedCustomer.setDeletedAt(java.time.LocalDateTime.now());
        entityManager.persist(deletedCustomer);
        
        entityManager.flush();

        // When
        List<Customer> customers = customerRepository.findByDeletedAtIsNull();

        // Then
        assertThat(customers).hasSize(1);
        assertThat(customers.get(0).getName()).isEqualTo("Test Customer");
    }

    @Test
    void testSearchByKeyword() {
        // Given
        entityManager.persist(testCustomer);
        
        Customer another = new Customer();
        another.setName("Another Customer");
        another.setEmail("another@company.vn");
        another.setCompany("Another Company");
        another.setTeamId(1L);
        another.setCreatedBy(1L);
        entityManager.persist(another);
        
        entityManager.flush();

        // When - tìm theo tên
        List<Customer> results = customerRepository.searchByKeyword("test");

        // Then
        assertThat(results).hasSize(1);
        assertThat(results.get(0).getName()).isEqualTo("Test Customer");

        // When - tìm theo company
        results = customerRepository.searchByKeyword("company");

        // Then
        assertThat(results).hasSize(2); // cả 2 đều có "company"
    }

    @Test
    void testFindByTeamIdAndDeletedAtIsNull() {
        // Given
        entityManager.persist(testCustomer);
        
        Customer otherTeam = new Customer();
        otherTeam.setName("Other Team Customer");
        otherTeam.setEmail("other@example.com");
        otherTeam.setTeamId(2L);
        otherTeam.setCreatedBy(1L);
        entityManager.persist(otherTeam);

        entityManager.flush();

        // When
        List<Customer> team1Customers = customerRepository.findByTeamIdAndDeletedAtIsNull(1L);

        // Then
        assertThat(team1Customers).hasSize(1);
        assertThat(team1Customers.get(0).getName()).isEqualTo("Test Customer");
    }
}