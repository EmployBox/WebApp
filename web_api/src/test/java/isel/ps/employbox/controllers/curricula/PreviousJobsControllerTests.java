package isel.ps.employbox.controllers.curricula;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.jayield.rapper.mapper.DataMapper;
import com.github.jayield.rapper.mapper.conditions.EqualAndCondition;
import com.github.jayield.rapper.unitofwork.UnitOfWork;
import isel.ps.employbox.exceptions.ResourceNotFoundException;
import isel.ps.employbox.model.entities.Curriculum;
import isel.ps.employbox.model.entities.UserAccount;
import isel.ps.employbox.model.entities.curricula.childs.PreviousJobs;
import isel.ps.employbox.model.input.curricula.childs.InPreviousJobs;
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
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.springframework.restdocs.webtestclient.WebTestClientRestDocumentation.document;
import static org.springframework.restdocs.webtestclient.WebTestClientRestDocumentation.documentationConfiguration;
import static org.springframework.security.test.web.reactive.server.SecurityMockServerConfigurers.springSecurity;
import static org.springframework.web.reactive.function.client.ExchangeFilterFunctions.basicAuthentication;


@RunWith(SpringRunner.class)
@SpringBootTest
public class PreviousJobsControllerTests {
    private static final Logger logger = LoggerFactory.getLogger(CurriculumControllerTests.class);
    @Rule
    public JUnitRestDocumentation restDocumentation = new JUnitRestDocumentation();
    @Autowired
    private ApplicationContext context;

    private WebTestClient webTestClient;
    private UserAccount userAccount;
    private PreviousJobs previousJobs;
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


        DataMapper<Curriculum, Long> curriculumRepo = getMapper(Curriculum.class, unitOfWork);
        List<Curriculum> curricula = curriculumRepo.find( new EqualAndCondition<>("title", "Engenharia Civil")).join();
        assertEquals(1, curricula.size());
        curriculum = curricula.get(0);

        DataMapper<PreviousJobs, Long> commentsMapper = getMapper(PreviousJobs.class, unitOfWork);
        List<PreviousJobs> previousJobs = commentsMapper.find(new EqualAndCondition<>("COMPANYNAME", "ISEL")).join();
        assertEquals(1, previousJobs.size());
        this.previousJobs = previousJobs.get(0);

        unitOfWork.commit().join();
    }

    @After
    public void after() {
        int openedConnections = UnitOfWork.getNumberOfOpenConnections().get();
        logger.info("OPENED CONNECTIONS - {}", openedConnections);
        assertEquals(0, openedConnections);
    }

    @Test
    public void testGetAllPreviousJobs(){
        webTestClient
                .get()
                .uri("/accounts/users/"+userAccount.getIdentityKey()+ "/curricula/"+ curriculum.getIdentityKey()+"/previousJobs")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .consumeWith(document("getAllPreviousJobs"));
    }

    @Test
    public void testGetAPreviousJob(){
        webTestClient
                .get()
                .uri("/accounts/users/"+userAccount.getIdentityKey()+ "/curricula/"+ curriculum.getIdentityKey()+"/previousJobs/"+ previousJobs.getIdentityKey())
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .consumeWith(document("getAllPreviousJobs"));
    }

    @Test
    @WithMockUser(username = "teste@gmail.com")
    public void testCreatePreviousJob() throws Exception {
        InPreviousJobs inPreviousJobs = new InPreviousJobs();
        inPreviousJobs.setCurriculumId(curriculum.getIdentityKey());
        inPreviousJobs.setAccountId(userAccount.getIdentityKey());
        inPreviousJobs.setCompanyName("old company");

        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(inPreviousJobs);
        UnitOfWork unitOfWork = new UnitOfWork();
        DataMapper<PreviousJobs, Long> projectMapper = getMapper(PreviousJobs.class,unitOfWork);
        webTestClient
                .post()
                .uri("/accounts/users/" + userAccount.getIdentityKey() + "/curricula/" + curriculum.getIdentityKey() + "/previousJobs")
                .contentType(MediaType.APPLICATION_JSON)
                .syncBody(json)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .consumeWith(document("createProject"));

        assertEquals(1, projectMapper.find( new EqualAndCondition<>("companyName", "old company")).join().size());
        unitOfWork.commit().join();
    }


    @Test
    @WithMockUser(username = "company1@gmail.com")
    public void testUpdateWrongPreviousJob() throws JsonProcessingException {
        InPreviousJobs inPreviousJobs = new InPreviousJobs();
        inPreviousJobs.setPreviousJobId(previousJobs.getIdentityKey());
        inPreviousJobs.setCurriculumId(previousJobs.getCurriculumId());
        inPreviousJobs.setAccountId(userAccount.getIdentityKey());
        inPreviousJobs.setVersion(previousJobs.getVersion());

        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(inPreviousJobs);

        webTestClient
                .put()
                .uri("/accounts/users/" + userAccount.getIdentityKey() + "/curricula/" + curriculum.getIdentityKey() + "/previousJobs/" + previousJobs.getIdentityKey())
                .contentType(MediaType.APPLICATION_JSON)
                .syncBody(json)
                .exchange()
                .expectStatus().isUnauthorized()
                .expectBody()
                .consumeWith(document("updateWrongPreviousJob"));
    }


    @Test
    @WithMockUser(username = "teste@gmail.com")
    public void testUpdatePreviousJob() throws JsonProcessingException {
        InPreviousJobs inPreviousJobs = new InPreviousJobs();
        inPreviousJobs.setPreviousJobId(previousJobs.getIdentityKey());
        inPreviousJobs.setCurriculumId(previousJobs.getCurriculumId());
        inPreviousJobs.setAccountId(userAccount.getIdentityKey());
        inPreviousJobs.setVersion(previousJobs.getVersion());
        inPreviousJobs.setCompanyName("new company");

        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(inPreviousJobs);

        webTestClient
                .put()
                .uri("/accounts/users/" + userAccount.getIdentityKey() + "/curricula/" + curriculum.getIdentityKey() + "/previousJobs/" + previousJobs.getIdentityKey())
                .contentType(MediaType.APPLICATION_JSON)
                .syncBody(json)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .consumeWith(document("updateWrongPreviousJob"));

        UnitOfWork unitOfWork = new UnitOfWork();
        DataMapper<PreviousJobs, Long> previousJobMapper = getMapper(PreviousJobs.class, unitOfWork);
        PreviousJobs updatePreviousJob = previousJobMapper.findById(previousJobs.getIdentityKey()).join().orElseThrow(() -> new ResourceNotFoundException("Previous job not found"));

        unitOfWork.commit().join();
        assertEquals("new company", updatePreviousJob.getCompanyName());
        assertNotEquals( previousJobs.getVersion(), updatePreviousJob.getVersion());
    }


    @Test
    @WithMockUser
    public void testDeleteWrongPreviousJob() {
        webTestClient
                .delete()
                .uri("/accounts/users/" + userAccount.getIdentityKey() + "/curricula/" + curriculum.getIdentityKey() + "/previousJobs/" + previousJobs.getIdentityKey())
                .exchange()
                .expectStatus().isUnauthorized()
                .expectBody()
                .consumeWith(document("deleteWrongPreviousjobs"));
    }

    @Test
    @WithMockUser(username = "teste@gmail.com")
    public void testDeletePreviousJob(){
        webTestClient
                .delete()
                .uri("/accounts/users/" + userAccount.getIdentityKey() + "/curricula/" + curriculum.getIdentityKey() + "/previousJobs/" + previousJobs.getIdentityKey())
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .consumeWith(document("deletePreviousjobs"));
        UnitOfWork unitOfWork = new UnitOfWork();
        DataMapper<PreviousJobs, Long> projectRepo = getMapper(PreviousJobs.class, unitOfWork);
        assertFalse(projectRepo.findById( previousJobs.getIdentityKey()).join().isPresent());
        unitOfWork.commit().join();
    }


}