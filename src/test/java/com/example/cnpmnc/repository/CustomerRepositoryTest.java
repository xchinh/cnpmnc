package com.example.cnpmnc.repository;


import com.example.cnpmnc.entity.Customer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.junit.jupiter.api.DisplayName;
import java.time.LocalDateTime;
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
    @Test
    @DisplayName("Test search by keyword - tìm theo tên")
    void testSearchByKeyword_Name() {
        // Given
        Customer customer1 = new Customer();
        customer1.setName("Nguyễn Văn A");
        customer1.setEmail("a@example.com");
        customer1.setCompany("Company A");
        customer1.setTeamId(1L);
        customer1.setCreatedBy(1L);
        entityManager.persist(customer1);
        
        Customer customer2 = new Customer();
        customer2.setName("Trần Văn B");
        customer2.setEmail("b@example.com");
        customer2.setCompany("Company B");
        customer2.setTeamId(1L);
        customer2.setCreatedBy(1L);
        entityManager.persist(customer2);
        
        entityManager.flush();

        // When
        List<Customer> results = customerRepository.searchByKeyword("Nguyễn");

        // Then
        assertThat(results).hasSize(1);
        assertThat(results.get(0).getName()).contains("Nguyễn");
    }

    @Test
    @DisplayName("Test search by keyword - tìm theo email")
    void testSearchByKeyword_Email() {
        // Given
        Customer customer = new Customer();
        customer.setName("Test User");
        customer.setEmail("test@techcorp.vn");
        customer.setCompany("TechCorp");
        customer.setTeamId(1L);
        customer.setCreatedBy(1L);
        entityManager.persistAndFlush(customer);

        // When
        List<Customer> results = customerRepository.searchByKeyword("techcorp.vn");

        // Then
        assertThat(results).hasSize(1);
        assertThat(results.get(0).getEmail()).contains("techcorp.vn");
    }

    @Test
    @DisplayName("Test search by keyword - tìm theo công ty")
    void testSearchByKeyword_Company() {
        // Given
        Customer customer1 = new Customer();
        customer1.setName("User 1");
        customer1.setEmail("user1@example.com");
        customer1.setCompany("ABC Company");
        customer1.setTeamId(1L);
        customer1.setCreatedBy(1L);
        entityManager.persist(customer1);
        
        Customer customer2 = new Customer();
        customer2.setName("User 2");
        customer2.setEmail("user2@example.com");
        customer2.setCompany("XYZ Corporation");
        customer2.setTeamId(1L);
        customer2.setCreatedBy(1L);
        entityManager.persist(customer2);
        
        entityManager.flush();

        // When
        List<Customer> results = customerRepository.searchByKeyword("ABC");

        // Then
        assertThat(results).hasSize(1);
        assertThat(results.get(0).getCompany()).contains("ABC");
    }

    @Test
    @DisplayName("Test search by keyword - case insensitive")
    void testSearchByKeyword_CaseInsensitive() {
        // Given
        Customer customer = new Customer();
        customer.setName("TechCorp Vietnam");
        customer.setEmail("contact@TECHCORP.vn");
        customer.setCompany("TECHCORP");
        customer.setTeamId(1L);
        customer.setCreatedBy(1L);
        entityManager.persistAndFlush(customer);

        // When - search với lowercase
        List<Customer> results = customerRepository.searchByKeyword("techcorp");

        // Then
        assertThat(results).hasSize(1);
    }

    @Test
    @DisplayName("Test search by keyword - không tìm thấy")
    void testSearchByKeyword_NotFound() {
        // Given
        Customer customer = new Customer();
        customer.setName("Test Customer");
        customer.setEmail("test@example.com");
        customer.setCompany("Test Company");
        customer.setTeamId(1L);
        customer.setCreatedBy(1L);
        entityManager.persistAndFlush(customer);

        // When
        List<Customer> results = customerRepository.searchByKeyword("nonexistent");

        // Then
        assertThat(results).isEmpty();
    }

    @Test
    @DisplayName("Test search by keyword - bỏ qua deleted customers")
    void testSearchByKeyword_IgnoreDeleted() {
        // Given
        Customer activeCustomer = new Customer();
        activeCustomer.setName("Active Customer");
        activeCustomer.setEmail("active@example.com");
        activeCustomer.setCompany("Active Company");
        activeCustomer.setTeamId(1L);
        activeCustomer.setCreatedBy(1L);
        entityManager.persist(activeCustomer);
        
        Customer deletedCustomer = new Customer();
        deletedCustomer.setName("Deleted Customer");
        deletedCustomer.setEmail("deleted@example.com");
        deletedCustomer.setCompany("Deleted Company");
        deletedCustomer.setTeamId(1L);
        deletedCustomer.setCreatedBy(1L);
        deletedCustomer.setDeletedAt(LocalDateTime.now());
        entityManager.persist(deletedCustomer);
        
        entityManager.flush();

        // When
        List<Customer> results = customerRepository.searchByKeyword("Customer");

        // Then - chỉ trả về active customer
        assertThat(results).hasSize(1);
        assertThat(results.get(0).getName()).isEqualTo("Active Customer");
    }
}