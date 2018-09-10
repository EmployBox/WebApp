package isel.ps.employbox.controllers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.jayield.rapper.mapper.DataMapper;
import com.github.jayield.rapper.mapper.conditions.EqualAndCondition;
import com.github.jayield.rapper.unitofwork.UnitOfWork;
import isel.ps.employbox.controllers.curricula.CurriculumControllerTests;
import isel.ps.employbox.model.entities.Account;
import isel.ps.employbox.model.entities.Rating;
import isel.ps.employbox.model.entities.UserAccount;
import isel.ps.employbox.model.input.InRating;
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
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.io.IOException;
import java.util.List;

import static com.github.jayield.rapper.mapper.MapperRegistry.getMapper;
import static isel.ps.employbox.DataBaseUtils.prepareDB;
import static junit.framework.TestCase.*;
import static org.springframework.restdocs.webtestclient.WebTestClientRestDocumentation.document;
import static org.springframework.restdocs.webtestclient.WebTestClientRestDocumentation.documentationConfiguration;
import static org.springframework.security.test.web.reactive.server.SecurityMockServerConfigurers.springSecurity;
import static org.springframework.web.reactive.function.client.ExchangeFilterFunctions.basicAuthentication;

@RunWith(SpringRunner.class)
@SpringBootTest
public class RatingControllerTests {
    private static final Logger logger = LoggerFactory.getLogger(CurriculumControllerTests.class);
    @Rule
    public JUnitRestDocumentation restDocumentation = new JUnitRestDocumentation();
    @Autowired
    private ApplicationContext context;

    private WebTestClient webTestClient;
    private UserAccount userAccount, userAccount2;
    private Account company1;
    private Rating rating;


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
        List<UserAccount> userAccounts = userAccountMapper.find(new EqualAndCondition<>("name", "Bruno")).join();
        assertEquals(1, userAccounts.size());
        userAccount = userAccounts.get(0);

        userAccounts = userAccountMapper.find(new EqualAndCondition<>("name", "Maria")).join();
        assertEquals(1, userAccounts.size());
        userAccount2 = userAccounts.get(0);


        DataMapper<Account, Long> accountMapper = getMapper(Account.class, unitOfWork);

        List<Account> accounts = accountMapper.find(new EqualAndCondition<>("name", "company1")).join();
        assertEquals(1, accounts.size());
        company1 = accounts.get(0);

        DataMapper<Rating, Rating.RatingKey> commentsMapper = getMapper(Rating.class, unitOfWork);
        List<Rating> ratings = commentsMapper.find(new EqualAndCondition<>("accountIdFrom", userAccount.getIdentityKey())).join();
        assertEquals(1, ratings.size());
        rating = ratings.get(0);

        unitOfWork.commit().join();
    }

    @After
    public void after() {
        int openedConnections = UnitOfWork.getNumberOfOpenConnections().get();
        logger.info("OPENED CONNECTIONS - {}", openedConnections);
        assertEquals(0, openedConnections);
    }

    @Test
    public void testGetAllRatings() throws IOException {
        String body = new String(webTestClient
                .get()
                .uri("/accounts/"+userAccount2.getIdentityKey()+"/ratings")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .returnResult()
                .getResponseBody());

        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(body);
        assertEquals(1,jsonNode.get("size").asInt());
        assertEquals(userAccount.getIdentityKey(), Long.valueOf(jsonNode.get("_embedded").get("items").findValuesAsText("accountIDFrom").get(0)));

        UnitOfWork unitOfWork = new UnitOfWork();
        DataMapper<Rating, Rating.RatingKey> userAccountMapper = getMapper(Rating.class, unitOfWork);
        assertTrue(userAccountMapper.findById( new Rating.RatingKey(userAccount.getIdentityKey().longValue(), userAccount2.getIdentityKey().longValue())).join().isPresent());
        unitOfWork.commit().join();
    }

    @Test
    @WithMockUser(username = "teste@gmail.com")
    public void testGetRating() throws IOException {
        String body = new String (webTestClient
                .get()
                .uri(uriBuilder -> uriBuilder.path("/accounts/"+userAccount2.getIdentityKey()+"/ratings/single").build())
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .returnResult()
                .getResponseBody());
    }


    @Test
    @WithMockUser(username = "teste@gmail.com")
    public void testCreateRating() throws Exception {

        InRating inRating = new InRating();
        inRating.setAccountIdFrom(userAccount.getIdentityKey());
        inRating.setAccountIdTo(company1.getIdentityKey());
        inRating.setAssiduity(5.0);
        inRating.setCompetence(3.0);
        inRating.setAccountType("USR");

        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(inRating);
        UnitOfWork unitOfWork = new UnitOfWork();

        webTestClient
                .post()
                .uri(uriBuilder -> uriBuilder.path("/accounts/"+company1.getIdentityKey()+"/ratings").build())
                .contentType(MediaType.APPLICATION_JSON)
                .syncBody(json)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .consumeWith(document("createComment"));

        DataMapper<Rating, Rating.RatingKey> ratingMapper = getMapper(Rating.class, unitOfWork);
        assertTrue(ratingMapper.find( new EqualAndCondition<>("assiduity", 5.0)).join().size() != 0);
        unitOfWork.commit().join();
    }

    @Test
    @WithMockUser(username = "company1@gmail.com")
    public void testUpdateWrongRating() throws Exception {
        InRating inRating = new InRating();
        inRating.setVersion( rating.getVersion());
        inRating.setAccountIdFrom(userAccount.getIdentityKey());
        inRating.setAccountIdTo(userAccount2.getIdentityKey());
        inRating.setAssiduity(2.0);
        inRating.setCompetence(1.0);

        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(inRating);
        UnitOfWork unitOfWork = new UnitOfWork();

        webTestClient
                .put()
                .uri(uriBuilder -> uriBuilder.path("/accounts/"+userAccount2.getIdentityKey()+"/ratings").build())
                .contentType(MediaType.APPLICATION_JSON)
                .syncBody(json)
                .exchange()
                .expectStatus().isUnauthorized()
                .expectBody()
                .consumeWith(document("updateWrongRating"));

        unitOfWork.commit().join();
    }


    @Test
    @WithMockUser(username = "teste@gmail.com")
    public void testUpdateRating() throws Exception {
        InRating inRating = new InRating();
        inRating.setVersion( rating.getVersion());
        inRating.setAccountIdFrom(userAccount.getIdentityKey());
        inRating.setAccountIdTo(userAccount2.getIdentityKey());
        inRating.setAssiduity(2.0);
        inRating.setCompetence(1.0);
        inRating.setDemeanor(5.0);
        inRating.setPontuality(5.0);
        inRating.setAccountType("USR");

        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(inRating);
        UnitOfWork unitOfWork = new UnitOfWork();

        webTestClient
                .put()
                .uri(uriBuilder -> uriBuilder.path("/accounts/"+userAccount2.getIdentityKey()+"/ratings").build())
                .contentType(MediaType.APPLICATION_JSON)
                .syncBody(json)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .consumeWith(document("updateRating"));

        DataMapper<Rating, Rating.RatingKey> ratingMapper = getMapper(Rating.class, unitOfWork);
        DataMapper<Account, Long> accountMapper = getMapper(Account.class, unitOfWork);

        Rating rating = ratingMapper.find( new EqualAndCondition<>("accountIdFrom", userAccount.getIdentityKey()),
                new EqualAndCondition<>("accountIdTo", userAccount2.getIdentityKey())).join().get(0);

        assertEquals(2.0,rating.getAssiduity());
        assertEquals(1.0,rating.getCompetence());

        Account account = accountMapper.find(new EqualAndCondition<>("accountId", userAccount2.getIdentityKey())).join().get(0);
        // (1+2+5+5) / 4
        assertEquals(3.2, account.getRating());
        unitOfWork.commit().join();
    }

    @Test
    public void testDeleteWrongRating(){
        webTestClient
                .delete()
                .uri(uriBuilder -> uriBuilder.path("/accounts/" + userAccount2.getIdentityKey() + "/ratings").build())
                .exchange()
                .expectStatus().isUnauthorized()
                .expectBody()
                .consumeWith(document("deleteRating"));
    }


    @Test
    @WithMockUser(username = "teste@gmail.com")
    public void testDeleteRating(){
        webTestClient
                .delete()
                .uri(uriBuilder -> uriBuilder.path("/accounts/" + userAccount2.getIdentityKey() + "/ratings").build())
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .consumeWith(document("deleteRating"));
        UnitOfWork unitOfWork = new UnitOfWork();
        DataMapper<Rating, Rating.RatingKey> ratingRepo = getMapper(Rating.class, unitOfWork);
        assertFalse(ratingRepo.findById( new Rating.RatingKey(rating.getIdentityKey().getAccountIdFrom(), rating.getIdentityKey().getAccountIdTo())).join().isPresent());
        unitOfWork.commit().join();
    }
}
