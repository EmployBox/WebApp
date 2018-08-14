package isel.ps.employbox.controllers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.jayield.rapper.mapper.DataMapper;
import com.github.jayield.rapper.mapper.conditions.EqualCondition;
import com.github.jayield.rapper.unitofwork.UnitOfWork;
import isel.ps.employbox.controllers.curricula.CurriculumControllerTests;
import isel.ps.employbox.model.entities.Follows;
import isel.ps.employbox.model.entities.UserAccount;
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
import java.util.List;

import static com.github.jayield.rapper.mapper.MapperRegistry.getMapper;
import static isel.ps.employbox.DataBaseUtils.prepareDB;
import static junit.framework.TestCase.assertEquals;
import static org.springframework.restdocs.webtestclient.WebTestClientRestDocumentation.documentationConfiguration;
import static org.springframework.security.test.web.reactive.server.SecurityMockServerConfigurers.springSecurity;
import static org.springframework.web.reactive.function.client.ExchangeFilterFunctions.basicAuthentication;

@RunWith(SpringRunner.class)
@SpringBootTest
public class FollowsControllerTests {

    private static final Logger logger = LoggerFactory.getLogger(CurriculumControllerTests.class);
    @Rule
    public JUnitRestDocumentation restDocumentation = new JUnitRestDocumentation();
    @Autowired
    private ApplicationContext context;

    private WebTestClient webTestClient;
    private UserAccount userAccount, userAccount2;
    private Follows follows;


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
        DataMapper<UserAccount, Long> userAccountMapper = getMapper(UserAccount.class, unitOfWork);
        List<UserAccount> userAccounts = userAccountMapper.find(new EqualCondition<>("name", "Bruno")).join();
        assertEquals(1, userAccounts.size());
        userAccount = userAccounts.get(0);

        userAccounts = userAccountMapper.find(new EqualCondition<>("name", "Maria")).join();
        assertEquals(1, userAccounts.size());
        userAccount2 = userAccounts.get(0);



        DataMapper<Follows, Follows.FollowKey> commentsMapper = getMapper(Follows.class, unitOfWork);
        List<Follows> comments = commentsMapper.find(new EqualCondition<>("accountIdFollower", userAccount.getIdentityKey())).join();
        assertEquals(2, comments.size());
        follows = comments.get(0);

        unitOfWork.commit().join();
    }

    @After
    public void after() {
        int openedConnections = UnitOfWork.getNumberOfOpenConnections().get();
        logger.info("OPENED CONNECTIONS - {}", openedConnections);
        assertEquals(0, openedConnections);
    }

    @Test
    @WithMockUser(username = "teste@gmail.com")
    public void testGetAllFollowedOrFollowerAccounts() throws IOException {

        String body = new String(webTestClient
                .get()
                .uri("/accounts/"+userAccount.getIdentityKey()+"/followers")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .returnResult()
                .getResponseBody());

        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(body);
        assertEquals(jsonNode.get("size").asInt(),2);

        body = new String(webTestClient
                .get()
                .uri("/accounts/"+userAccount2.getIdentityKey()+"/followers")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .returnResult()
                .getResponseBody());

        jsonNode = objectMapper.readTree(body);
        assertEquals(jsonNode.get("size").asInt(),1);
    }
}
