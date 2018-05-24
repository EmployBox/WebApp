package isel.ps.employbox.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.jayield.rapper.DataRepository;
import com.github.jayield.rapper.utils.ConnectionManager;
import isel.ps.employbox.EmployBoxAPIApp;
import isel.ps.employbox.model.binder.UserBinder;
import isel.ps.employbox.model.entities.UserAccount;
import isel.ps.employbox.model.input.InUser;
import isel.ps.employbox.security.SecurityConfig;
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
import org.springframework.security.test.web.reactive.server.SecurityMockServerConfigurers;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.net.URLDecoder;
import java.sql.Connection;
import java.sql.SQLException;

import static isel.ps.employbox.DataBaseUtils.createUsers;
import static junit.framework.TestCase.assertFalse;
import static junit.framework.TestCase.assertTrue;
import static org.springframework.restdocs.webtestclient.WebTestClientRestDocumentation.document;
import static org.springframework.restdocs.webtestclient.WebTestClientRestDocumentation.documentationConfiguration;
import static org.springframework.security.test.web.reactive.server.SecurityMockServerConfigurers.*;
import static org.springframework.web.reactive.function.client.ExchangeFilterFunctions.basicAuthentication;

@RunWith(SpringRunner.class)
@SpringBootTest//(classes = {EmployBoxAPIApp.class, SecurityConfig.class})
public class UsersControllerTests {
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
        ConnectionManager connectionManager = ConnectionManager.getConnectionManager(
                "jdbc:hsqldb:file:" + URLDecoder.decode(this.getClass().getClassLoader().getResource("testdb").getPath()) + "/testdb",
                "SA", "");
        con = connectionManager.getConnection();
        con.prepareCall("{call deleteDB()}").execute();
        con.prepareCall("{call populateDB()}").execute();
        con.commit();

        webTestClient = WebTestClient.bindToApplicationContext(context)
                .apply(springSecurity())
                .configureClient()
                .filter(basicAuthentication())
                .filter(documentationConfiguration(restDocumentation))
                .build();

        userAccountId = createUsers(userBinder, userRepo, con);
    }

    @After
    public void after() throws SQLException {
        con.close();
    }

    @Test
    public void testCreateUser() throws Exception {
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
                .consumeWith(document("createUser"));

        assertTrue(userRepo.findWhere(new Pair<>("email", "someEmail@hotmail.com")).join().size() != 0);
    }

    @Test
    public void testGetAllUsers() {
        webTestClient
                .get()
                .uri("/accounts/users")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .consumeWith(document("getAllUsers"));
    }

    @Test
    public void testGetUser() {
        webTestClient
                .get()
                .uri("/accounts/users/" + userAccountId)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .consumeWith(document("getUser"));
    }

    @Test
    @WithAnonymousUser
    public void testDeleteUserWhenNotAuthenticated() {
        webTestClient
                .delete()
                .uri("/accounts/users/" + userAccountId)
                .exchange()
                .expectStatus().isUnauthorized()
                .expectBody()
                .consumeWith(document("deleteUserWhenNotAuthenticated"));
    }

    @Test
    @WithMockUser
    public void testDeleteWrongUser() {
        webTestClient
                .delete()
                .uri("/accounts/users/" + userAccountId)
                .exchange()
                .expectStatus().isUnauthorized()
                .expectBody()
                .consumeWith(document("deleteWrongUser"));
    }
}
