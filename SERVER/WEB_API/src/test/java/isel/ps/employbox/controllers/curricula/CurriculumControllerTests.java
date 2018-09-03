package isel.ps.employbox.controllers.curricula;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.jayield.rapper.mapper.DataMapper;
import com.github.jayield.rapper.mapper.conditions.EqualAndCondition;
import com.github.jayield.rapper.unitofwork.UnitOfWork;
import isel.ps.employbox.exceptions.ResourceNotFoundException;
import isel.ps.employbox.model.entities.Curriculum;
import isel.ps.employbox.model.entities.UserAccount;
import isel.ps.employbox.model.entities.curricula.childs.Project;
import isel.ps.employbox.model.input.curricula.childs.InCurriculum;
import isel.ps.employbox.model.input.curricula.childs.InProject;
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

import java.util.ArrayList;
import java.util.List;

import static com.github.jayield.rapper.mapper.MapperRegistry.getMapper;
import static isel.ps.employbox.DataBaseUtils.prepareDB;
import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertFalse;
import static org.springframework.restdocs.webtestclient.WebTestClientRestDocumentation.document;
import static org.springframework.restdocs.webtestclient.WebTestClientRestDocumentation.documentationConfiguration;
import static org.springframework.security.test.web.reactive.server.SecurityMockServerConfigurers.springSecurity;
import static org.springframework.web.reactive.function.client.ExchangeFilterFunctions.basicAuthentication;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CurriculumControllerTests {
    private static final Logger logger = LoggerFactory.getLogger(CurriculumControllerTests.class);
    @Rule
    public JUnitRestDocumentation restDocumentation = new JUnitRestDocumentation();
    @Autowired
    private ApplicationContext context;

    private WebTestClient webTestClient;
    private UserAccount userAccount;
    private Curriculum curriculum;

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

        DataMapper<Curriculum, Long> curriculumMapper = getMapper(Curriculum.class, unitOfWork);
        List<Curriculum> curricula = curriculumMapper.find(new EqualAndCondition<>("title", "Engenharia Civil")).join();
        assertEquals(1, curricula.size());
        curriculum = curricula.get(0);

        unitOfWork.commit().join();
    }

    @After
    public void after() {
        int openedConnections = UnitOfWork.getNumberOfOpenConnections().get();
        logger.info("OPENED CONNECTIONS - {}", openedConnections);
        assertEquals(0, openedConnections);
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

        InProject inProject = new InProject();
        inProject.setDescription("ello");
        inProject.setName("proj");
        inProject.setAccountId(userAccount.getIdentityKey());

        List<InProject> projects = new ArrayList<>();
        projects.add(inProject);
        inCurriculum.setProjects(projects);

        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(inCurriculum);
        UnitOfWork unitOfWork = new UnitOfWork();
        DataMapper<Curriculum, Long> curriculumMapper = getMapper(Curriculum.class, unitOfWork);
        DataMapper<Project, Long> projectMapper = getMapper(Project.class, unitOfWork);
        webTestClient
                .post()
                .uri("/accounts/users/" + userAccount.getIdentityKey() + "/curricula")
                .contentType(MediaType.APPLICATION_JSON)
                .syncBody(json)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .consumeWith(document("createCurriculum"));

        assertEquals(1, curriculumMapper.find(new EqualAndCondition<>("title", "Verrryyy gud curriculum")).join().size());
        assertEquals(1, projectMapper.find(new EqualAndCondition<>("name", "proj")).join().size());
        unitOfWork.commit().join();
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
        UnitOfWork unitOfWork = new UnitOfWork();
        webTestClient
                .put()
                .uri("/accounts/users/" + userAccount.getIdentityKey() + "/curricula/" + curriculum.getIdentityKey())
                .contentType(MediaType.APPLICATION_JSON)
                .syncBody(json)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .consumeWith(document("updateCurriculum"));
        DataMapper<Curriculum, Long> curriculumMapper = getMapper(Curriculum.class, unitOfWork);
        Curriculum updatedCurriculum = curriculumMapper.findById( curriculum.getIdentityKey()).join().orElseThrow(() -> new ResourceNotFoundException("Curriculum not found"));
        unitOfWork.commit().join();

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
        UnitOfWork unitOfWork = new UnitOfWork();
        DataMapper<Curriculum, Long> curriculumMapper = getMapper(Curriculum.class, unitOfWork);
        assertFalse(curriculumMapper.findById( curriculum.getIdentityKey()).join().isPresent());
        unitOfWork.commit().join();
    }
}
