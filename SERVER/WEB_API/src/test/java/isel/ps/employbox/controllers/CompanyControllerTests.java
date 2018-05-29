package isel.ps.employbox.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.jayield.rapper.DataRepository;
import isel.ps.employbox.model.binder.CompanyBinder;
import isel.ps.employbox.model.entities.Company;
import isel.ps.employbox.model.input.InCompany;
import javafx.util.Pair;
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

import static isel.ps.employbox.DataBaseUtils.prepareDB;
import static junit.framework.TestCase.assertFalse;
import static junit.framework.TestCase.assertTrue;
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
    private CompanyBinder companyBinder;
    @Autowired
    private DataRepository<Company, Long> companyRepo;
    private Connection con;
    private WebTestClient webTestClient;
    private long companyId;

    @Before
    public void setUp() throws SQLException {
        con = prepareDB();
        webTestClient = WebTestClient.bindToApplicationContext(context)
                .apply(springSecurity())
                .configureClient()
                .filter(basicAuthentication())
                .filter(documentationConfiguration(restDocumentation))
                .build();

        InCompany inCompany = new InCompany();
        inCompany.setEmail("company1@gmail.com");
        inCompany.setPassword("741");
        Company company = companyBinder.bindInput(inCompany);
        assertTrue(!companyRepo.create(company).join().isPresent());

        inCompany.setEmail("company2@gmail.com");
        inCompany.setPassword("567");
        company = companyBinder.bindInput(inCompany);
        assertTrue(!companyRepo.create(company).join().isPresent());

        companyId = company.getIdentityKey();
    }

    @After
    public void after() throws SQLException {
        con.close();
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
                .uri("/accounts/companies/" + companyId)
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
        inCompany.setAccountId(companyId);
        inCompany.setEmail("someEmail@hotmail.com");
        inCompany.setName("Microsoft");
        inCompany.setPassword("1234");
        inCompany.setDescription("Sou uma empresa simpatica");

        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(inCompany);

        webTestClient
                .put()
                .uri("/accounts/companies/" + companyId)
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
        inCompany.setAccountId(companyId);
        inCompany.setEmail("someEmail@hotmail.com");
        inCompany.setName("Microsoft");
        inCompany.setPassword("1234");
        inCompany.setDescription("Sou uma empresa simpatica");

        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(inCompany);

        webTestClient
                .put()
                .uri("/accounts/companies/" + companyId)
                .contentType(MediaType.APPLICATION_JSON)
                .syncBody(json)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .consumeWith(document("updateCompany"));
    }

    @Test
    @WithAnonymousUser
    public void testDeleteCompanyWhenNotAuthenticated() {
        webTestClient
                .delete()
                .uri("/accounts/companies/" + companyId)
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
                .uri("/accounts/companies/" + companyId)
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
                .uri("/accounts/companies/" + companyId)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .consumeWith(document("deleteCompany"));

        assertFalse(companyRepo.findById(companyId).join().isPresent());
    }
}
