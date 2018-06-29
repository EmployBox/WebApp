package isel.ps.employbox.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.jayield.rapper.DataRepository;
import com.github.jayield.rapper.utils.Pair;
import isel.ps.employbox.exceptions.ResourceNotFoundException;
import isel.ps.employbox.model.entities.Application;
import isel.ps.employbox.model.entities.Job;
import isel.ps.employbox.model.entities.UserAccount;
import isel.ps.employbox.model.input.InApplication;
import isel.ps.employbox.model.input.InUserAccount;
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
import java.sql.Timestamp;
import java.util.List;

import static junit.framework.TestCase.*;
import static org.springframework.restdocs.webtestclient.WebTestClientRestDocumentation.document;
import static org.springframework.restdocs.webtestclient.WebTestClientRestDocumentation.documentationConfiguration;
import static org.springframework.security.test.web.reactive.server.SecurityMockServerConfigurers.springSecurity;
import static org.springframework.web.reactive.function.client.ExchangeFilterFunctions.basicAuthentication;
/*
@RunWith(SpringRunner.class)
@SpringBootTest
public class UserAccountControllerTests {
    @Rule
    public JUnitRestDocumentation restDocumentation = new JUnitRestDocumentation();
    @Autowired
    private ApplicationContext context;
    @Autowired
    private DataRepository<UserAccount, Long> userAccountRepo;
    @Autowired
    private DataRepository<Job, Long> jobRepo;
    @Autowired
    private DataRepository<Application, Long> applicationRepo;
    private WebTestClient webTestClient;
    private Connection con;
    private UserAccount userAccount;
    private long jobId;
    private Application application;

    @Before
    public void setUp() throws SQLException {
        prepareDB();
        webTestClient = WebTestClient.bindToApplicationContext(context)
                .apply(springSecurity())
                .configureClient()
                .filter(basicAuthentication())
                .filter(documentationConfiguration(restDocumentation))
                .build();
        List<UserAccount> userAccounts = userAccountRepo.findWhere(new Pair<>("email", "lol@hotmail.com")).join();
        assertEquals(1, userAccounts.size());
        userAccount = userAccounts.get(0);

        List<Job> jobs = jobRepo.findWhere(new Pair<>("title", "Great Job")).join();
        assertEquals(1, jobs.size());
        jobId = jobs.get(0).getIdentityKey();

        List<Application> applications = applicationRepo.findWhere(new Pair<>("accountId", userAccount.getIdentityKey()), new Pair<>("jobId", jobId)).join();
        assertEquals(1, applications.size());
        application = applications.get(0);
    }

    @Test
    public void testGetAllUserAccounts() {
        webTestClient
                .get()
                .uri("/accounts/users")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .consumeWith(document("getAllUserAccounts"));
    }

    @Test
    public void testGetUserAccount() {
        webTestClient
                .get()
                .uri("/accounts/users/" + userAccount.getIdentityKey())
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .consumeWith(document("getUserAccount"));
    }

    @Test
    public void testGetAllApplications() {
        webTestClient
                .get()
                .uri("/accounts/users/" + userAccount.getIdentityKey() + "/applications")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .consumeWith(document("getAllApplications"));
    }

    @Test
    public void testGetApplication() {
        webTestClient
                .get()
                .uri("/accounts/users/" + userAccount.getIdentityKey() + "/applications/" + jobId)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .consumeWith(document("getApplication"));
    }

    @Test
    public void testCreateUserAccount() throws Exception {
        InUserAccount user = new InUserAccount();
        user.setEmail("someEmail@hotmail.com");
        user.setName("Manuel");
        user.setPassword("1234");
        user.setSummary("Sou um tipo simpatico");

        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(user);

        webTestClient
                .post()
                .uri("/accounts/users")
                .contentType(MediaType.APPLICATION_JSON)
                .syncBody(json)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .consumeWith(document("createUserAccount"));

        assertEquals(1, userAccountRepo.findWhere(new Pair<>("email", "someEmail@hotmail.com")).join().size());
    }

    @Test
    @WithMockUser(username =  "lol@hotmail.com")
    public void testCreateApplication() throws Exception {
        InApplication inApplication = new InApplication();
        inApplication.setAccountId(userAccount.getIdentityKey());
        inApplication.setJobId(jobId);

        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(inApplication);

        webTestClient
                .post()
                .uri("/accounts/users/" + userAccount.getIdentityKey() + "/applications/" + jobId)
                .contentType(MediaType.APPLICATION_JSON)
                .syncBody(json)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .consumeWith(document("createApplication"));

        assertEquals(2, applicationRepo.findWhere(new Pair<>("accountId", userAccount.getIdentityKey()), new Pair<>("jobId", jobId)).join().size());
    }

    @Test
    @WithMockUser(username = "teste@gmail.com")
    public void testUpdateWrongUserAccount() throws JsonProcessingException {
        InUserAccount inUserAccount = new InUserAccount();
        inUserAccount.setId(userAccount.getIdentityKey());
        inUserAccount.setEmail("someEmail@hotmail.com");
        inUserAccount.setName("Manuel");
        inUserAccount.setPassword("1234");
        inUserAccount.setSummary("Sou um tipo simpatico");

        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(inUserAccount);

        webTestClient
                .put()
                .uri("/accounts/users/" + userAccount.getIdentityKey())
                .contentType(MediaType.APPLICATION_JSON)
                .syncBody(json)
                .exchange()
                .expectStatus().isUnauthorized()
                .expectBody()
                .consumeWith(document("updateWrongUserAccount"));
    }

    @Test
    @WithMockUser(username = "teste@gmail.com")
    public void testUpdateWrongApplication() throws JsonProcessingException {
        InApplication inApplication = new InApplication();
        inApplication.setAccountId(userAccount.getIdentityKey());
        inApplication.setJobId(jobId);
        inApplication.setDate(new Timestamp(2019, 2, 2, 2, 2, 2, 2));

        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(inApplication);

        webTestClient
                .put()
                .uri("/accounts/users/" + userAccount.getIdentityKey() + "/applications/" + jobId)
                .contentType(MediaType.APPLICATION_JSON)
                .syncBody(json)
                .exchange()
                .expectStatus().isUnauthorized()
                .expectBody()
                .consumeWith(document("updateWrongApplication"));
    }

    @Test
    @WithMockUser(username = "lol@hotmail.com")
    public void testUpdateUserAccount() throws JsonProcessingException {
        InUserAccount inUserAccount = new InUserAccount();
        inUserAccount.setId(userAccount.getIdentityKey());
        inUserAccount.setEmail("someEmail@hotmail.com");
        inUserAccount.setName("Manuel");
        inUserAccount.setPassword("1234");
        inUserAccount.setSummary("Sou um tipo simpatico");
        inUserAccount.setUserVersion(userAccount.getVersion());
        inUserAccount.setAccountVersion(userAccount.getAccountVersion());

        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(inUserAccount);

        webTestClient
                .put()
                .uri("/accounts/users/" + userAccount.getIdentityKey())
                .contentType(MediaType.APPLICATION_JSON)
                .syncBody(json)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .consumeWith(document("updateUserAccount"));

        UserAccount userAccount = userAccountRepo.findById(this.userAccount.getIdentityKey()).join().orElseThrow(() -> new ResourceNotFoundException("UserAccount not found"));
        assertEquals("Sou um tipo simpatico", userAccount.getSummary());
        assertEquals("Manuel", userAccount.getName());
        assertEquals("someEmail@hotmail.com", userAccount.getEmail());
    }

    @Test
    @WithMockUser(username = "lol@hotmail.com")
    public void testUpdateApplication() throws JsonProcessingException {
        InApplication inApplication = new InApplication();
        inApplication.setApplicationId(application.getIdentityKey());
        inApplication.setAccountId(userAccount.getIdentityKey());
        inApplication.setJobId(jobId);
        inApplication.setDate(new Timestamp(2019, 2, 2, 2, 2, 2, 2));
        inApplication.setVersion(application.getVersion());

        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(inApplication);

        webTestClient
                .put()
                .uri("/accounts/users/" + userAccount.getIdentityKey() + "/applications/" + jobId)
                .contentType(MediaType.APPLICATION_JSON)
                .syncBody(json)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .consumeWith(document("updateApplication"));

        Application updatedApplication = applicationRepo.findById(application.getIdentityKey()).join().orElseThrow(() -> new ResourceNotFoundException("Application not found"));
        assertTrue(updatedApplication.getVersion() != application.getVersion());
    }

    @Test
    @WithAnonymousUser
    public void testDeleteUserAccountWhenNotAuthenticated() {
        webTestClient
                .delete()
                .uri("/accounts/users/" + userAccount.getIdentityKey())
                .exchange()
                .expectStatus().isUnauthorized()
                .expectBody()
                .consumeWith(document("deleteUserAccountWhenNotAuthenticated"));
    }

    @Test
    @WithMockUser
    public void testDeleteWrongUserAccount() {
        webTestClient
                .delete()
                .uri("/accounts/users/" + userAccount.getIdentityKey())
                .exchange()
                .expectStatus().isUnauthorized()
                .expectBody()
                .consumeWith(document("deleteWrongUserAccount"));
    }

    @Test
    @WithMockUser(username = "lol@hotmail.com")
    public void testDeleteUserAccount(){
        webTestClient
                .delete()
                .uri("/accounts/users/" + userAccount.getIdentityKey())
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .consumeWith(document("deleteUserAccount"));

        assertFalse(userAccountRepo.findById(userAccount.getIdentityKey()).join().isPresent());
    }

    @Test
    @WithMockUser
    public void testDeleteWrongApplication() {
        webTestClient
                .delete()
                .uri("/accounts/users/" + userAccount.getIdentityKey() + "/applications/" + jobId)
                .exchange()
                .expectStatus().isUnauthorized()
                .expectBody()
                .consumeWith(document("deleteWrongApplication"));
    }

    @Test
    @WithMockUser(username = "lol@hotmail.com")
    public void testDeleteApplication(){
        webTestClient
                .delete()
                .uri("/accounts/users/" + userAccount.getIdentityKey() + "/applications/" + jobId)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .consumeWith(document("deleteApplication"));

        assertTrue(applicationRepo.findWhere(new Pair<>("accountId", userAccount.getIdentityKey()), new Pair<>("jobId", jobId)).join().isEmpty());
    }
}*/
