package isel.ps.employbox.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.jayield.rapper.DataRepository;
import com.github.jayield.rapper.utils.Pair;
import com.github.jayield.rapper.utils.UnitOfWork;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

import static isel.ps.employbox.DataBaseUtils.prepareDB;
import static junit.framework.TestCase.*;
import static org.springframework.restdocs.webtestclient.WebTestClientRestDocumentation.document;
import static org.springframework.restdocs.webtestclient.WebTestClientRestDocumentation.documentationConfiguration;
import static org.springframework.security.test.web.reactive.server.SecurityMockServerConfigurers.springSecurity;
import static org.springframework.web.reactive.function.client.ExchangeFilterFunctions.basicAuthentication;
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
    private static final Logger logger = LoggerFactory.getLogger(UserAccountControllerTests.class);
    private WebTestClient webTestClient;
    private UserAccount userAccount;
    private long jobId;
    private Application application;

    @Before
    public void setUp() {
        prepareDB();
        webTestClient = WebTestClient.bindToApplicationContext(context)
                .apply(springSecurity())
                .configureClient()
                .filter(basicAuthentication())
                .filter(documentationConfiguration(restDocumentation))
                .build();

        UnitOfWork unitOfWork = new UnitOfWork();
        List<UserAccount> userAccounts = userAccountRepo.findWhere(unitOfWork, new Pair<>("email", "lol@hotmail.com")).join();
        assertEquals(1, userAccounts.size());
        userAccount = userAccounts.get(0);

        List<Job> jobs = jobRepo.findWhere(unitOfWork, new Pair<>("title", "Great Job")).join();
        assertEquals(1, jobs.size());
        jobId = jobs.get(0).getIdentityKey();

        List<Application> applications = applicationRepo.findWhere(unitOfWork, new Pair<>("accountId", userAccount.getIdentityKey()), new Pair<>("jobId", jobId)).join();
        assertEquals(1, applications.size());
        application = applications.get(0);

        unitOfWork.commit().join();
    }

    @After
    public void after() {
        int openedConnections = UnitOfWork.numberOfOpenConnections.get();
        logger.info("OPENED CONNECTIONS - {}", openedConnections);
        assertEquals(0, openedConnections);
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
        UnitOfWork unit = new UnitOfWork();
        List<Application> applications = applicationRepo.findWhere(unit, new Pair<>("accountId", userAccount.getIdentityKey()), new Pair<>("jobId", jobId)).join();
        assertEquals(1, applications.size());
        Application application = applications.get(0);
        unit.commit().join();

        webTestClient
                .get()
                .uri("/accounts/users/" + userAccount.getIdentityKey() + "/jobs/" + jobId + "/applications/" + application.getIdentityKey())
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

        UnitOfWork unit = new UnitOfWork();
        assertEquals(1, userAccountRepo.findWhere(unit, new Pair<>("email", "someEmail@hotmail.com")).join().size());
        unit.commit().join();

        Logger logger = LoggerFactory.getLogger(UserAccountControllerTests.class);
        logger.info("OPENED CONNECTIONS - {}", UnitOfWork.numberOfOpenConnections.get());
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
                .uri("/accounts/users/" + userAccount.getIdentityKey() + "/jobs/" + jobId + "/applications")
                .contentType(MediaType.APPLICATION_JSON)
                .syncBody(json)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .consumeWith(document("createApplication"));

        UnitOfWork unit = new UnitOfWork();
        assertEquals(2, applicationRepo.findWhere(unit, new Pair<>("accountId", userAccount.getIdentityKey()), new Pair<>("jobId", jobId)).join().size());
        unit.commit().join();
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
        inApplication.setApplicationId(application.getIdentityKey());
        inApplication.setAccountId(userAccount.getIdentityKey());
        inApplication.setJobId(jobId);
        inApplication.setDate(new Timestamp(2019, 2, 2, 2, 2, 2, 2));

        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(inApplication);

        webTestClient
                .put()
                .uri("/accounts/users/" + userAccount.getIdentityKey() + "/jobs/" + jobId + "/applications/" + application.getIdentityKey())
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

        UnitOfWork unit = new UnitOfWork();
        UserAccount userAccount = userAccountRepo.findById(unit, this.userAccount.getIdentityKey()).join().orElseThrow(() -> new ResourceNotFoundException("UserAccount not found"));
        unit.commit().join();
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
                .uri("/accounts/users/" + userAccount.getIdentityKey() + "/jobs/"+ jobId + "/applications/" + application.getIdentityKey())
                .contentType(MediaType.APPLICATION_JSON)
                .syncBody(json)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .consumeWith(document("updateApplication"));

        UnitOfWork unit = new UnitOfWork();
        Application updatedApplication = applicationRepo.findById(unit, application.getIdentityKey()).join().orElseThrow(() -> new ResourceNotFoundException("Application not found"));
        unit.commit().join();
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

        UnitOfWork unit = new UnitOfWork();
        assertFalse(userAccountRepo.findById(unit, userAccount.getIdentityKey()).join().isPresent());
        unit.commit().join();
    }

    @Test
    @WithMockUser
    public void testDeleteWrongApplication() {
        UnitOfWork unit = new UnitOfWork();
        List<Application> applications = applicationRepo.findWhere(unit, new Pair<>("accountId", userAccount.getIdentityKey()), new Pair<>("jobId", jobId)).join();
        assertEquals(1, applications.size());
        Application application = applications.get(0);
        unit.commit().join();

        webTestClient
                .delete()
                .uri("/accounts/users/" + userAccount.getIdentityKey() + "/jobs/" + jobId + "/applications/" + application.getIdentityKey())
                .exchange()
                .expectStatus().isUnauthorized()
                .expectBody()
                .consumeWith(document("deleteWrongApplication"));
    }

    @Test
    @WithMockUser(username = "lol@hotmail.com")
    public void testDeleteApplication(){
        UnitOfWork unit = new UnitOfWork();
        List<Application> applications = applicationRepo.findWhere(unit, new Pair<>("accountId", userAccount.getIdentityKey()), new Pair<>("jobId", jobId)).join();
        assertEquals(1, applications.size());
        Application application = applications.get(0);
        unit.commit().join();

        webTestClient
                .delete()
                .uri("/accounts/users/" + userAccount.getIdentityKey() + "/jobs/" + jobId + "/applications/" + application.getIdentityKey())
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .consumeWith(document("deleteApplication"));

        unit = new UnitOfWork();
        assertTrue(applicationRepo.findWhere(unit, new Pair<>("accountId", userAccount.getIdentityKey()), new Pair<>("jobId", jobId)).join().isEmpty());
        unit.commit().join();
    }
}
