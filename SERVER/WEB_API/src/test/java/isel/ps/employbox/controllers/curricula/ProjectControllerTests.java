package isel.ps.employbox.controllers.curricula;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.jayield.rapper.mapper.DataMapper;
import com.github.jayield.rapper.unitofwork.UnitOfWork;
import com.github.jayield.rapper.utils.Pair;
import isel.ps.employbox.exceptions.ResourceNotFoundException;
import isel.ps.employbox.model.entities.Curriculum;
import isel.ps.employbox.model.entities.UserAccount;
import isel.ps.employbox.model.entities.curricula.childs.Project;
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

import java.util.List;

import static com.github.jayield.rapper.mapper.MapperRegistry.getMapper;
import static isel.ps.employbox.DataBaseUtils.prepareDB;
import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.springframework.restdocs.webtestclient.WebTestClientRestDocumentation.document;
import static org.springframework.restdocs.webtestclient.WebTestClientRestDocumentation.documentationConfiguration;
import static org.springframework.security.test.web.reactive.server.SecurityMockServerConfigurers.springSecurity;
import static org.springframework.web.reactive.function.client.ExchangeFilterFunctions.basicAuthentication;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ProjectControllerTests {
    private static final Logger logger = LoggerFactory.getLogger(AcademicBackgroundControllerTests.class);
    @Rule
    public JUnitRestDocumentation restDocumentation = new JUnitRestDocumentation();
    @Autowired
    private ApplicationContext context;
    private WebTestClient webTestClient;
    private UserAccount userAccount;
    private Curriculum curriculum;
    private Project project;

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
        DataMapper<UserAccount, Long> userAccountRepo = getMapper(UserAccount.class, unitOfWork);
        List<UserAccount> userAccounts = userAccountRepo.findWhere( new Pair<>("name", "Bruno")).join();
        assertEquals(1, userAccounts.size());
        userAccount = userAccounts.get(0);

        DataMapper<Curriculum, Long> curriculumRepo = getMapper(Curriculum.class, unitOfWork);
        List<Curriculum> curricula = curriculumRepo.findWhere( new Pair<>("title", "Engenharia Civil")).join();
        assertEquals(1, curricula.size());
        curriculum = curricula.get(0);

        DataMapper<Project, Long> projectRepo = getMapper(Project.class, unitOfWork);
        List<Project> projects = projectRepo.findWhere( new Pair<>("description", "project one")).join();
        assertEquals(1, projects.size());
        project = projects.get(0);
        unitOfWork.commit().join();
    }

    @After
    public void after() {
        int openedConnections = UnitOfWork.numberOfOpenConnections.get();
        logger.info("OPENED CONNECTIONS - {}", openedConnections);
        assertEquals(0, openedConnections);
    }

    @Test
    public void testGetAllProjects(){
        webTestClient
                .get()
                .uri("/accounts/users/" + userAccount.getIdentityKey() + "/curricula/" + curriculum.getIdentityKey() + "/projects")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .consumeWith(document("getAllProjects"));
    }

    @Test
    public void testGetProject(){
        webTestClient
                .get()
                .uri("/accounts/users/" + userAccount.getIdentityKey() + "/curricula/" + curriculum.getIdentityKey() + "/projects/" + project.getIdentityKey())
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .consumeWith(document("getProject"));
    }

    @Test
    @WithMockUser(username = "teste@gmail.com")
    public void testCreateProject() throws Exception {
        InProject inProject = new InProject();
        inProject.setCurriculumId(curriculum.getIdentityKey());
        inProject.setAccountId(userAccount.getIdentityKey());
        inProject.setName("first project");
        inProject.setDescription("project description");

        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(inProject);
        UnitOfWork unitOfWork = new UnitOfWork();
        DataMapper<Project, Long> projectMapper = getMapper(Project.class,unitOfWork);
        webTestClient
                .post()
                .uri("/accounts/users/" + userAccount.getIdentityKey() + "/curricula/" + curriculum.getIdentityKey() + "/projects")
                .contentType(MediaType.APPLICATION_JSON)
                .syncBody(json)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .consumeWith(document("createProject"));

        assertEquals(1, projectMapper.findWhere( new Pair<>("description", "project description")).join().size());
        assertEquals(1, projectMapper.findWhere( new Pair<>("name", "first project")).join().size());
        unitOfWork.commit().join();
    }

    @Test
    @WithMockUser(username = "company1@gmail.com")
    public void testUpdateWrongProject() throws JsonProcessingException {
        InProject inProject = new InProject();
        inProject.setProjectId(project.getIdentityKey());
        inProject.setCurriculumId(curriculum.getIdentityKey());
        inProject.setAccountId(userAccount.getIdentityKey());
        inProject.setVersion(project.getVersion());

        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(inProject);

        webTestClient
                .put()
                .uri("/accounts/users/" + userAccount.getIdentityKey() + "/curricula/" + curriculum.getIdentityKey() + "/projects/" + project.getIdentityKey())
                .contentType(MediaType.APPLICATION_JSON)
                .syncBody(json)
                .exchange()
                .expectStatus().isUnauthorized()
                .expectBody()
                .consumeWith(document("updateWrongAcademicBackground"));
    }

    @Test
    @WithMockUser(username = "teste@gmail.com")
    public void testUpdateProject() throws JsonProcessingException {
        InProject inProject = new InProject();
        inProject.setProjectId(project.getIdentityKey());
        inProject.setCurriculumId(curriculum.getIdentityKey());
        inProject.setAccountId(userAccount.getIdentityKey());
        inProject.setDescription("updated project");
        inProject.setVersion(project.getVersion());

        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(inProject);

        webTestClient
                .put()
                .uri("/accounts/users/" + userAccount.getIdentityKey() + "/curricula/" + curriculum.getIdentityKey() + "/projects/" + project.getIdentityKey())
                .contentType(MediaType.APPLICATION_JSON)
                .syncBody(json)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .consumeWith(document("updateWrongAcademicBackground"));


        UnitOfWork unitOfWork = new UnitOfWork();
        DataMapper<Project, Long> projectMapper = getMapper(Project.class, unitOfWork);
        Project updatedProject = projectMapper.findById(project.getIdentityKey()).join().orElseThrow(() -> new ResourceNotFoundException("Curriculum not found"));

        unitOfWork.commit().join();
        assertEquals("updated project", updatedProject.getDescription());
        assertNotEquals( project.getVersion(), updatedProject.getVersion());
    }

    @Test
    @WithMockUser
    public void testDeleteWrongProject() {
        webTestClient
                .delete()
                .uri("/accounts/users/" + userAccount.getIdentityKey() + "/curricula/" + curriculum.getIdentityKey() + "/projects/" + project.getIdentityKey())
                .exchange()
                .expectStatus().isUnauthorized()
                .expectBody()
                .consumeWith(document("deleteWrongProject"));
    }

    @Test
    @WithMockUser(username = "teste@gmail.com")
    public void testDeleteProject(){
        webTestClient
                .delete()
                .uri("/accounts/users/" + userAccount.getIdentityKey() + "/curricula/" + curriculum.getIdentityKey() + "/projects/" + project.getIdentityKey())
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .consumeWith(document("deleteProject"));
        UnitOfWork unitOfWork = new UnitOfWork();
        DataMapper<Project, Long> projectRepo = getMapper(Project.class, unitOfWork);
        assertFalse(projectRepo.findById( project.getIdentityKey()).join().isPresent());
        unitOfWork.commit().join();
    }
}
