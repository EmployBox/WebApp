package isel.ps.employbox.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.jayield.rapper.DataRepository;
import isel.ps.employbox.model.binder.UserBinder;
import isel.ps.employbox.model.entities.UserAccount;
import isel.ps.employbox.model.input.InUser;
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
public class UserAccountControllerTests {
    @Rule
    public JUnitRestDocumentation restDocumentation = new JUnitRestDocumentation();
    @Autowired
    private ApplicationContext context;
    @Autowired
    private DataRepository<UserAccount, Long> userRepo;
    @Autowired
    private UserBinder userBinder;
    private WebTestClient webTestClient;
    private Connection con;
    private long userAccountId;

    @Before
    public void setUp() throws SQLException {
        con = prepareDB();
        webTestClient = WebTestClient.bindToApplicationContext(context)
                .apply(springSecurity())
                .configureClient()
                .filter(basicAuthentication())
                .filter(documentationConfiguration(restDocumentation))
                .build();
        InUser user = new InUser();
        user.setEmail("teste@gmail.com");
        user.setPassword("password");
        user.setName("Bruno");
        UserAccount userAccount = userBinder.bindInput(user);
        assertTrue(!userRepo.create(userAccount).join().isPresent());

        user.setEmail("lol@hotmail.com");
        user.setPassword("teste123");
        user.setName("Maria");
        userAccount = userBinder.bindInput(user);
        assertTrue(!userRepo.create(userAccount).join().isPresent());

        userAccountId = userAccount.getIdentityKey();
    }

    @After
    public void after() throws SQLException {
        con.close();
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
                .uri("/accounts/users/" + userAccountId)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .consumeWith(document("getUserAccount"));
    }

    @Test
    public void testCreateUserAccount() throws Exception {
        InUser user = new InUser();
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

        assertTrue(userRepo.findWhere(new Pair<>("email", "someEmail@hotmail.com")).join().size() != 0);
    }

    @Test
    @WithMockUser(username = "teste@gmail.com")
    public void testUpdateWrongUserAccount() throws JsonProcessingException {
        InUser inUser = new InUser();
        inUser.setId(userAccountId);
        inUser.setEmail("someEmail@hotmail.com");
        inUser.setName("Manuel");
        inUser.setPassword("1234");
        inUser.setSummary("Sou um tipo simpatico");

        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(inUser);

        webTestClient
                .put()
                .uri("/accounts/users/" + userAccountId)
                .contentType(MediaType.APPLICATION_JSON)
                .syncBody(json)
                .exchange()
                .expectStatus().isUnauthorized()
                .expectBody()
                .consumeWith(document("updateWrongUserAccount"));
    }

    @Test
    @WithMockUser(username = "lol@hotmail.com")
    public void testUpdateUserAccount() throws JsonProcessingException {
        InUser inUser = new InUser();
        inUser.setId(userAccountId);
        inUser.setEmail("someEmail@hotmail.com");
        inUser.setName("Manuel");
        inUser.setPassword("1234");
        inUser.setSummary("Sou um tipo simpatico");

        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(inUser);

        webTestClient
                .put()
                .uri("/accounts/users/" + userAccountId)
                .contentType(MediaType.APPLICATION_JSON)
                .syncBody(json)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .consumeWith(document("updateUserAccount"));
    }

    @Test
    @WithAnonymousUser
    public void testDeleteUserAccountWhenNotAuthenticated() {
        webTestClient
                .delete()
                .uri("/accounts/users/" + userAccountId)
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
                .uri("/accounts/users/" + userAccountId)
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
                .uri("/accounts/users/" + userAccountId)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .consumeWith(document("deleteUserAccount"));

        assertFalse(userRepo.findById(userAccountId).join().isPresent());
    }
}
