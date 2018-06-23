package isel.ps.employbox.controllers.curricula;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.jayield.rapper.DataRepository;
import com.github.jayield.rapper.utils.Pair;
import isel.ps.employbox.exceptions.ResourceNotFoundException;
import isel.ps.employbox.model.entities.Curriculum;
import isel.ps.employbox.model.entities.UserAccount;
import isel.ps.employbox.model.entities.curricula.childs.AcademicBackground;
import isel.ps.employbox.model.input.curricula.childs.InAcademicBackground;
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
import static org.springframework.restdocs.webtestclient.WebTestClientRestDocumentation.document;
import static org.springframework.restdocs.webtestclient.WebTestClientRestDocumentation.documentationConfiguration;
import static org.springframework.security.test.web.reactive.server.SecurityMockServerConfigurers.springSecurity;
import static org.springframework.web.reactive.function.client.ExchangeFilterFunctions.basicAuthentication;

@RunWith(SpringRunner.class)
@SpringBootTest
public class AcademicBackgroundControllerTests {
    @Rule
    public JUnitRestDocumentation restDocumentation = new JUnitRestDocumentation();
    @Autowired
    private ApplicationContext context;
    @Autowired
    private DataRepository<UserAccount, Long> userAccountRepo;
    @Autowired
    private DataRepository<Curriculum, Long> curriculumRepo;
    @Autowired
    private DataRepository<AcademicBackground, Long> academicBackgroundRepo;
    private WebTestClient webTestClient;
    private UserAccount userAccount;
    private Curriculum curriculum;
    private AcademicBackground academicBackground;

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

        List<AcademicBackground> academicBackgrounds = academicBackgroundRepo.findWhere(new Pair<>("institution", "ISEL")).join();
        assertEquals(1, academicBackgrounds.size());
        academicBackground = academicBackgrounds.get(0);
    }

    @Test
    public void testGetAllAcademicBackgrounds(){
        webTestClient
                .get()
                .uri("/accounts/users/" + userAccount.getIdentityKey() + "/curricula/" + curriculum.getIdentityKey() + "/academic")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .consumeWith(document("getAllAcademicBackground"));
    }

    @Test
    public void testGetAcademicBackground(){
        webTestClient
                .get()
                .uri("/accounts/users/" + userAccount.getIdentityKey() + "/curricula/" + curriculum.getIdentityKey() + "/academic/" + academicBackground.getIdentityKey())
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .consumeWith(document("getAcademicBackground"));
    }

    @Test
    @WithMockUser(username = "teste@gmail.com")
    public void testCreateAcademicBackground() throws Exception {
        InAcademicBackground inAcademicBackground = new InAcademicBackground();
        inAcademicBackground.setCurriculumId(curriculum.getIdentityKey());
        inAcademicBackground.setAccountId(userAccount.getIdentityKey());
        inAcademicBackground.setDegreeObtained("bachelor");

        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(inAcademicBackground);

        webTestClient
                .post()
                .uri("/accounts/users/" + userAccount.getIdentityKey() + "/curricula/" + curriculum.getIdentityKey() + "/academic")
                .contentType(MediaType.APPLICATION_JSON)
                .syncBody(json)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .consumeWith(document("createAcademicBackground"));

        assertEquals(1, academicBackgroundRepo.findWhere(new Pair<>("degreeObtained", "bachelor")).join().size());
    }

    @Test
    @WithMockUser(username = "company1@gmail.com")
    public void testUpdateWrongAcademicBackground() throws JsonProcessingException {
        InAcademicBackground inAcademicBackground = new InAcademicBackground();
        inAcademicBackground.setId(academicBackground.getIdentityKey());
        inAcademicBackground.setCurriculumId(curriculum.getIdentityKey());
        inAcademicBackground.setAccountId(userAccount.getIdentityKey());
        inAcademicBackground.setVersion(academicBackground.getVersion());

        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(inAcademicBackground);

        webTestClient
                .put()
                .uri("/accounts/users/" + userAccount.getIdentityKey() + "/curricula/" + curriculum.getIdentityKey() + "/academic/" + academicBackground.getIdentityKey())
                .contentType(MediaType.APPLICATION_JSON)
                .syncBody(json)
                .exchange()
                .expectStatus().isUnauthorized()
                .expectBody()
                .consumeWith(document("updateWrongAcademicBackground"));
    }

    @Test
    @WithMockUser(username = "teste@gmail.com")
    public void testUpdateAcademicBackground() throws JsonProcessingException {
        InAcademicBackground inAcademicBackground = new InAcademicBackground();
        inAcademicBackground.setId(academicBackground.getIdentityKey());
        inAcademicBackground.setCurriculumId(curriculum.getIdentityKey());
        inAcademicBackground.setAccountId(userAccount.getIdentityKey());
        inAcademicBackground.setVersion(academicBackground.getVersion());
        inAcademicBackground.setInstitution("ISEL 2.0");

        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(inAcademicBackground);

        webTestClient
                .put()
                .uri("/accounts/users/" + userAccount.getIdentityKey() + "/curricula/" + curriculum.getIdentityKey() + "/academic/" + academicBackground.getIdentityKey())
                .contentType(MediaType.APPLICATION_JSON)
                .syncBody(json)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .consumeWith(document("updateAcademicBackground"));

        AcademicBackground updatedAcademicBackground = academicBackgroundRepo
                .findById(academicBackground.getIdentityKey()).join().orElseThrow(() -> new ResourceNotFoundException("AcademicBackground not found"));

        assertEquals("ISEL 2.0", updatedAcademicBackground.getInstitution());
    }

    @Test
    @WithMockUser
    public void testDeleteWrongAcademicBackground() {
        webTestClient
                .delete()
                .uri("/accounts/users/" + userAccount.getIdentityKey() + "/curricula/" + curriculum.getIdentityKey() + "/academic/" + academicBackground.getIdentityKey())
                .exchange()
                .expectStatus().isUnauthorized()
                .expectBody()
                .consumeWith(document("deleteWrongAcademicBackground"));
    }

    @Test
    @WithMockUser(username = "teste@gmail.com")
    public void testDeleteAcademicBackground(){
        webTestClient
                .delete()
                .uri("/accounts/users/" + userAccount.getIdentityKey() + "/curricula/" + curriculum.getIdentityKey() + "/academic/" + academicBackground.getIdentityKey())
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .consumeWith(document("deleteAcademicBackground"));

        assertFalse(academicBackgroundRepo.findById(academicBackground.getIdentityKey()).join().isPresent());
    }
}
