package isel.ps.employbox.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.jayield.rapper.DataRepository;
import com.github.jayield.rapper.utils.Pair;
import isel.ps.employbox.exceptions.ResourceNotFoundException;
import isel.ps.employbox.model.entities.Curriculum;
import isel.ps.employbox.model.entities.UserAccount;
import isel.ps.employbox.model.input.InCurriculum;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.http.MediaType;
import org.springframework.restdocs.JUnitRestDocumentation;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.sql.SQLException;
import java.util.List;

import static isel.ps.employbox.DataBaseUtils.prepareDB;
import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertFalse;
import static junit.framework.TestCase.assertTrue;
import static org.springframework.restdocs.webtestclient.WebTestClientRestDocumentation.document;
import static org.springframework.restdocs.webtestclient.WebTestClientRestDocumentation.documentationConfiguration;
import static org.springframework.security.test.web.reactive.server.SecurityMockServerConfigurers.springSecurity;
import static org.springframework.web.reactive.function.client.ExchangeFilterFunctions.basicAuthentication;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CurriculumControllerTests {
    @Rule
    public JUnitRestDocumentation restDocumentation = new JUnitRestDocumentation();
    @Autowired
    private ApplicationContext context;
    @Autowired
    private DataRepository<UserAccount, Long> userAccountRepo;
    @Autowired
    private DataRepository<Curriculum, Long> curriculumRepo;
    private WebTestClient webTestClient;
    private UserAccount userAccount;
    private Curriculum curriculum;

    @Before
    public void setUp() throws SQLException {
        prepareDB();
        webTestClient = WebTestClient.bindToApplicationContext(context)
                .apply(springSecurity())
                .configureClient()
                .filter(basicAuthentication())
                .filter(documentationConfiguration(restDocumentation))
                .build();

        List<UserAccount> userAccounts = userAccountRepo.findWhere(new Pair<>("name", "Bruno")).join();
        assertEquals(1, userAccounts.size());
        userAccount = userAccounts.get(0);

        List<Curriculum> curricula = curriculumRepo.findWhere(new Pair<>("title", "Engenharia Civil")).join();
        assertEquals(1, curricula.size());
        curriculum = curricula.get(0);
    }

    @Test
    public void testGetCurricula(){
        webTestClient
                .get()
                .uri("/accounts/users/" + userAccount.getIdentityKey() + "/curricula")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .consumeWith(document("getCurricula"));
    }

    @Test
    public void testGetCurriculum(){
        webTestClient
                .get()
                .uri("/accounts/users/" + userAccount.getIdentityKey() + "/curricula/" + curriculum.getIdentityKey())
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .consumeWith(document("getCurriculum"));
    }

    @Test
    @WithMockUser(username = "teste@gmail.com")
    public void testCreateCurriculum() throws Exception {
        InCurriculum inCurriculum = new InCurriculum();
        inCurriculum.setAccountId(userAccount.getIdentityKey());
        inCurriculum.setTitle("Verrryyy gud curriculum");

        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(inCurriculum);

        webTestClient
                .post()
                .uri("/accounts/users/" + userAccount.getIdentityKey() + "/curricula")
                .contentType(MediaType.APPLICATION_JSON)
                .syncBody(json)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .consumeWith(document("createCurriculum"));

        assertEquals(1, curriculumRepo.findWhere(new Pair<>("title", "Verrryyy gud curriculum")).join().size());
    }

    @Test
    @WithMockUser(username = "company1@gmail.com")
    public void testUpdateWrongCurriculum() throws JsonProcessingException {
        InCurriculum inCurriculum = new InCurriculum();
        inCurriculum.setCurriculumId(curriculum.getIdentityKey());
        inCurriculum.setAccountId(userAccount.getIdentityKey());
        inCurriculum.setTitle("Update");

        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(inCurriculum);

        webTestClient
                .put()
                .uri("/accounts/users/" + userAccount.getIdentityKey() + "/curricula/" + curriculum.getIdentityKey())
                .contentType(MediaType.APPLICATION_JSON)
                .syncBody(json)
                .exchange()
                .expectStatus().isUnauthorized()
                .expectBody()
                .consumeWith(document("updateWrongCurriculum"));
    }

    @Test
    @WithMockUser(username = "teste@gmail.com")
    public void testUpdateCurriculum() throws JsonProcessingException {
        InCurriculum inCurriculum = new InCurriculum();
        inCurriculum.setCurriculumId(curriculum.getIdentityKey());
        inCurriculum.setAccountId(userAccount.getIdentityKey());
        inCurriculum.setTitle("Qualquer coisa");
        inCurriculum.setVersion(curriculum.getVersion());

        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(inCurriculum);

        webTestClient
                .put()
                .uri("/accounts/users/" + userAccount.getIdentityKey() + "/curricula/" + curriculum.getIdentityKey())
                .contentType(MediaType.APPLICATION_JSON)
                .syncBody(json)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .consumeWith(document("updateCurriculum"));

        Curriculum updatedCurriculum = curriculumRepo.findById(curriculum.getIdentityKey()).join().orElseThrow(() -> new ResourceNotFoundException("Curriculum not found"));

        assertEquals("Qualquer coisa", updatedCurriculum.getTitle());
    }

    @Test
    @WithMockUser
    public void testDeleteWrongCurriculum() {
        webTestClient
                .delete()
                .uri("/accounts/users/" + userAccount.getIdentityKey() + "/curricula/" + curriculum.getIdentityKey())
                .exchange()
                .expectStatus().isUnauthorized()
                .expectBody()
                .consumeWith(document("deleteWrongCurriculum"));
    }

    @Test
    @WithMockUser(username = "teste@gmail.com")
    public void testDeleteCurriculum(){
        webTestClient
                .delete()
                .uri("/accounts/users/" + userAccount.getIdentityKey() + "/curricula/" + curriculum.getIdentityKey())
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .consumeWith(document("deleteCurriculum"));

        assertFalse(curriculumRepo.findById(curriculum.getIdentityKey()).join().isPresent());
    }
}
