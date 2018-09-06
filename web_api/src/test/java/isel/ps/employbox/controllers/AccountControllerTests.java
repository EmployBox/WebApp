package isel.ps.employbox.controllers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.jayield.rapper.mapper.DataMapper;
import com.github.jayield.rapper.mapper.conditions.EqualAndCondition;
import com.github.jayield.rapper.unitofwork.UnitOfWork;
import isel.ps.employbox.model.entities.Account;
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
import org.springframework.restdocs.JUnitRestDocumentation;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.io.IOException;

import static com.github.jayield.rapper.mapper.MapperRegistry.getMapper;
import static isel.ps.employbox.DataBaseUtils.prepareDB;
import static junit.framework.TestCase.assertEquals;
import static org.springframework.restdocs.webtestclient.WebTestClientRestDocumentation.document;
import static org.springframework.restdocs.webtestclient.WebTestClientRestDocumentation.documentationConfiguration;
import static org.springframework.security.test.web.reactive.server.SecurityMockServerConfigurers.springSecurity;
import static org.springframework.web.reactive.function.client.ExchangeFilterFunctions.basicAuthentication;

@RunWith(SpringRunner.class)
@SpringBootTest
public class AccountControllerTests {
    @Rule
    public JUnitRestDocumentation restDocumentation = new JUnitRestDocumentation();
    @Autowired
    private ApplicationContext context;

    private static final Logger logger = LoggerFactory.getLogger(UserAccountControllerTests.class);
    private WebTestClient webTestClient;
    private Account account;

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
        DataMapper<Account, Long> accountMapper = getMapper(Account.class, unitOfWork);
        this.account = accountMapper.find(new EqualAndCondition<>("name", "Maria")).join().get(0);

        unitOfWork.commit().join();
    }

    @After
    public void after() {
        int openedConnections = UnitOfWork.getNumberOfOpenConnections().get();
        logger.info("OPENED CONNECTIONS - {}", openedConnections);
        assertEquals(0, openedConnections);
    }

    @Test
    @WithMockUser(username =  "lol@hotmail.com")
    public void testAuthenticateCredentials() {
        webTestClient
                .get()
                .uri("/accounts")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .consumeWith(document("authenticateCredentials"));
    }

    @Test
    public void testGetAccount() throws IOException {
        String body = new String(
                webTestClient
                        .get()
                        .uri("/accounts/"+account.getIdentityKey())
                        .exchange()
                        .expectStatus().isOk()
                        .expectBody()
                        .returnResult()
                        .getResponseBody()
        );

        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(body);
        assertEquals("Maria", jsonNode.findValuesAsText("name").get(0));
    }
}


