package isel.ps.employbox.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.jayield.rapper.DataRepository;
import com.github.jayield.rapper.utils.Pair;
import isel.ps.employbox.exceptions.ResourceNotFoundException;
import isel.ps.employbox.model.entities.Company;
import isel.ps.employbox.model.input.InCompany;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.http.MediaType;
import org.springframework.restdocs.JUnitRestDocumentation;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import static isel.ps.employbox.DataBaseUtils.prepareDB;
import static junit.framework.TestCase.*;
import static org.springframework.restdocs.webtestclient.WebTestClientRestDocumentation.document;
import static org.springframework.restdocs.webtestclient.WebTestClientRestDocumentation.documentationConfiguration;
import static org.springframework.security.test.web.reactive.server.SecurityMockServerConfigurers.springSecurity;
import static org.springframework.web.reactive.function.client.ExchangeFilterFunctions.basicAuthentication;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CompanyControllerTests {
    @Rule
    public JUnitRestDocumentation restDocumentation = new JUnitRestDocumentation();
    @Autowired
    private ApplicationContext context;
    @Autowired
    private DataRepository<Company, Long> companyRepo;
    private WebTestClient webTestClient;
    private Company company;

    @Before
    public void setUp() throws SQLException {
        prepareDB();
        webTestClient = WebTestClient.bindToApplicationContext(context)
                .apply(springSecurity())
                .configureClient()
                .filter(basicAuthentication())
                .filter(documentationConfiguration(restDocumentation))
                .build();
        List<Company> companies = companyRepo.findWhere(new Pair<>("email", "company2@gmail.com")).join();
        assertEquals(1, companies.size());
        company = companies.get(0);
    }

    @Test
    public void testGetAllCompanies(){
        webTestClient
                .get()
                .uri("/accounts/companies")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .consumeWith(document("getAllCompanies"));
    }

    @Test
    public void testGetCompany(){
        webTestClient
                .get()
                .uri("/accounts/companies/" + company.getIdentityKey())
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .consumeWith(document("getCompany"));
    }

    @Test
    public void testCreateCompany() throws Exception {
        InCompany inCompany = new InCompany();
        inCompany.setEmail("someEmail@hotmail.com");
        inCompany.setName("Google");
        inCompany.setPassword("1234");
        inCompany.setDescription("Sou uma empresa simpatica");

        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(inCompany);

        webTestClient
                .post()
                .uri("/accounts/companies")
                .contentType(MediaType.APPLICATION_JSON)
                .syncBody(json)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .consumeWith(document("createCompany"));

        assertTrue(companyRepo.findWhere(new Pair<>("email", "someEmail@hotmail.com")).join().size() != 0);
    }

    @Test
    @WithMockUser(username = "company1@gmail.com")
    public void testUpdateWrongCompany() throws JsonProcessingException {
        InCompany inCompany = new InCompany();
        inCompany.setAccountId(company.getIdentityKey());
        inCompany.setEmail("someEmail@hotmail.com");
        inCompany.setName("Microsoft");
        inCompany.setPassword("1234");
        inCompany.setDescription("Sou uma empresa simpatica");

        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(inCompany);

        webTestClient
                .put()
                .uri("/accounts/companies/" + company.getIdentityKey())
                .contentType(MediaType.APPLICATION_JSON)
                .syncBody(json)
                .exchange()
                .expectStatus().isUnauthorized()
                .expectBody()
                .consumeWith(document("updateWrongCompany"));
    }

    @Test
    @WithMockUser(username = "company2@gmail.com")
    public void testUpdateCompany() throws JsonProcessingException {
        InCompany inCompany = new InCompany();
        inCompany.setAccountId(company.getIdentityKey());
        inCompany.setEmail("someEmail@hotmail.com");
        inCompany.setName("Microsoft");
        inCompany.setPassword("1234");
        inCompany.setDescription("Sou uma empresa simpatica");
        inCompany.setAccountVersion(company.getAccountVersion());
        inCompany.setCompanyVersion(company.getVersion());

        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(inCompany);

        webTestClient
                .put()
                .uri("/accounts/companies/" + company.getIdentityKey())
                .contentType(MediaType.APPLICATION_JSON)
                .syncBody(json)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .consumeWith(document("updateCompany"));

        Company updatedCompany = companyRepo.findById(company.getIdentityKey()).join().orElseThrow(() -> new ResourceNotFoundException("Company not found"));

        assertEquals("Microsoft", updatedCompany.getName());
        assertEquals("Sou uma empresa simpatica", updatedCompany.getDescription());
        assertEquals("someEmail@hotmail.com", updatedCompany.getEmail());
    }

    @Test
    @WithAnonymousUser
    public void testDeleteCompanyWhenNotAuthenticated() {
        webTestClient
                .delete()
                .uri("/accounts/companies/" + company.getIdentityKey())
                .exchange()
                .expectStatus().isUnauthorized()
                .expectBody()
                .consumeWith(document("deleteCompanyWhenNotAuthenticated"));
    }

    @Test
    @WithMockUser
    public void testDeleteWrongCompany() {
        webTestClient
                .delete()
                .uri("/accounts/companies/" + company.getIdentityKey())
                .exchange()
                .expectStatus().isUnauthorized()
                .expectBody()
                .consumeWith(document("deleteWrongCompany"));
    }

    @Test
    @WithMockUser(username = "company2@gmail.com")
    public void testDeleteCompany(){
        webTestClient
                .delete()
                .uri("/accounts/companies/" + company.getIdentityKey())
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .consumeWith(document("deleteCompany"));

        assertFalse(companyRepo.findById(company.getIdentityKey()).join().isPresent());
    }
}
