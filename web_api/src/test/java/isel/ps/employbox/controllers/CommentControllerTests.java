package isel.ps.employbox.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.jayield.rapper.mapper.DataMapper;
import com.github.jayield.rapper.mapper.conditions.EqualAndCondition;
import com.github.jayield.rapper.unitofwork.UnitOfWork;
import isel.ps.employbox.controllers.curricula.CurriculumControllerTests;
import isel.ps.employbox.model.entities.Comment;
import isel.ps.employbox.model.entities.UserAccount;
import isel.ps.employbox.model.input.InComment;
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
import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.springframework.restdocs.webtestclient.WebTestClientRestDocumentation.document;
import static org.springframework.restdocs.webtestclient.WebTestClientRestDocumentation.documentationConfiguration;
import static org.springframework.security.test.web.reactive.server.SecurityMockServerConfigurers.springSecurity;
import static org.springframework.web.reactive.function.client.ExchangeFilterFunctions.basicAuthentication;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CommentControllerTests {
    private static final Logger logger = LoggerFactory.getLogger(CurriculumControllerTests.class);
    @Rule
    public JUnitRestDocumentation restDocumentation = new JUnitRestDocumentation();
    @Autowired
    private ApplicationContext context;

    private WebTestClient webTestClient;
    private UserAccount userAccount, userAccount2;
    private Comment comment;


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

        DataMapper<Comment, Long> commentsMapper = getMapper(Comment.class, unitOfWork);
        List<Comment> comments = commentsMapper.find(new EqualAndCondition<>("TEXT", "FIRST COMMENT")).join();
        assertEquals(1, comments.size());
        comment = comments.get(0);

        unitOfWork.commit().join();
    }

    @After
    public void after() {
        int openedConnections = UnitOfWork.getNumberOfOpenConnections().get();
        logger.info("OPENED CONNECTIONS - {}", openedConnections);
        assertEquals(0, openedConnections);
    }

    @Test
    public void testGetAllComments() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();

        //pedido de comentários efetuados á conta userAccount
        String body = new String(webTestClient
                .get()
                .uri("/accounts/"+userAccount.getIdentityKey()+"/comments")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .returnResult()
                .getResponseBody());

        JsonNode jsonNode = objectMapper.readTree(body);
        //esperados 0 comentários
        assertEquals(0, jsonNode.findValue("size").asInt());

        //pedido de comentários efetuados á conta userAccount
        body = new String(webTestClient
                .get()
                .uri("/accounts/"+userAccount2.getIdentityKey()+"/comments")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .returnResult()
                .getResponseBody());

        //esperados 1 cometário
        jsonNode = objectMapper.readTree(body);
        assertEquals(1, jsonNode.findValue("size").asInt());
    }

    @Test
    @WithMockUser(username = "teste@gmail.com")
    public void testGetComment(){
        webTestClient
                .get()
                .uri("/accounts/"+userAccount.getIdentityKey()+"/comments/"+ comment.getIdentityKey())
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .consumeWith(document("getAllComments"));
    }

    @Test
    @WithMockUser(username = "lol@hotmail.com")
    public void testCreateComment() throws Exception {
        InComment inComment = new InComment();
        inComment.setAccountIdFrom(userAccount2.getIdentityKey());
        inComment.setAccountIdTo(userAccount.getIdentityKey());
        inComment.setText("HAHAHA");

        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(inComment);
        UnitOfWork unitOfWork = new UnitOfWork();

        webTestClient
                .post()
                .uri(uriBuilder -> uriBuilder.path("/accounts/"+userAccount.getIdentityKey()+"/comments").build())
                .contentType(MediaType.APPLICATION_JSON)
                .syncBody(json)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .consumeWith(document("createComment"));

        DataMapper<Comment, Long> jobMapper = getMapper(Comment.class, unitOfWork);
        assertTrue(jobMapper.find( new EqualAndCondition<>("text", "HAHAHA")).join().size() != 0);
        unitOfWork.commit().join();
    }

    @Test
    @WithMockUser(username = "company1@gmail.com")
    public void testUpdateWrongComment() throws JsonProcessingException {
        InComment inComment = new InComment();
        inComment.setAccountIdFrom(userAccount.getIdentityKey());
        inComment.setAccountIdTo(userAccount2.getIdentityKey());
        inComment.setText("WRONG");

        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(inComment);

        webTestClient
                .put()
                .uri("/accounts/" + userAccount.getIdentityKey() + "/comments/" + comment.getIdentityKey())
                .contentType(MediaType.APPLICATION_JSON)
                .syncBody(json)
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody()
                .consumeWith(document("updateWrongComments"));
    }

    @Test
    @WithMockUser(username = "teste@gmail.com")
    public void testUpdateComment() throws JsonProcessingException {
        InComment inComment = new InComment();
        inComment.setAccountIdFrom(userAccount.getIdentityKey());
        inComment.setAccountIdTo(userAccount2.getIdentityKey());
        inComment.setCommmentId( comment.getIdentityKey());
        inComment.setText("RIGHT");
        inComment.setVersion( comment.getVersion());

        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(inComment);

        webTestClient
                .put()
                .uri("/accounts/" + userAccount2.getIdentityKey() + "/comments/" + comment.getIdentityKey())
                .contentType(MediaType.APPLICATION_JSON)
                .syncBody(json)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .consumeWith(document("updateWrongComments"));

        UnitOfWork unitOfWork = new UnitOfWork();
        DataMapper<Comment, Long> commentMapper = getMapper(Comment.class, unitOfWork);
        Comment testComment = commentMapper.findById( comment.getIdentityKey()).join().get();
        unitOfWork.commit().join();

        assertEquals(testComment.getText(), "RIGHT");
    }


    @Test
    @WithMockUser(username = "teste@gmail.com")
    public void testDeleteComment(){
        webTestClient
                .delete()
                .uri("/accounts/" + userAccount.getIdentityKey() + "/comments/" + comment.getIdentityKey())
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .consumeWith(document("deleteComment"));
        UnitOfWork unitOfWork = new UnitOfWork();
        DataMapper<Comment, Long> commentRepo = getMapper(Comment.class, unitOfWork);
        boolean cond = commentRepo.findById( comment.getIdentityKey()).join().isPresent();
        unitOfWork.commit().join();
        assertFalse(cond);

    }
}
