package isel.ps.employbox.controllers.curricula;


import com.github.jayield.rapper.DataRepository;
import com.github.jayield.rapper.utils.Pair;
import com.github.jayield.rapper.utils.UnitOfWork;
import isel.ps.employbox.model.entities.Curriculum;
import isel.ps.employbox.model.entities.UserAccount;
import isel.ps.employbox.model.entities.curricula.childs.AcademicBackground;
import isel.ps.employbox.model.entities.curricula.childs.Project;
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
public class ProjectControllerTests {
    private static final Logger logger = LoggerFactory.getLogger(AcademicBackgroundControllerTests.class);
    @Rule
    public JUnitRestDocumentation restDocumentation = new JUnitRestDocumentation();
    @Autowired
    private ApplicationContext context;
    private WebTestClient webTestClient;
    @Autowired
    private DataRepository<UserAccount, Long> userAccountRepo;
    @Autowired
    private DataRepository<Curriculum, Long> curriculumRepo;
    @Autowired
    private DataRepository<Project, Long> projectRepo;
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
        List<UserAccount> userAccounts = userAccountRepo.findWhere(unitOfWork, new Pair<>("name", "Bruno")).join();
        assertEquals(1, userAccounts.size());
        userAccount = userAccounts.get(0);

        List<Curriculum> curricula = curriculumRepo.findWhere(unitOfWork, new Pair<>("title", "Engenharia Civil")).join();
        assertEquals(1, curricula.size());
        curriculum = curricula.get(0);

        List<Project> projects = projectRepo.findWhere( unitOfWork, new Pair<>("description", "project one")).join();
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
        assertFalse(projectRepo.findById(unitOfWork, project.getIdentityKey()).join().isPresent());
        unitOfWork.commit().join();
    }
}
