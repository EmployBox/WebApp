package isel.ps.employbox.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.jayield.rapper.DataRepository;
import isel.ps.employbox.model.binder.JobBinder;
import isel.ps.employbox.model.entities.Job;
import isel.ps.employbox.model.entities.JobExperience;
import isel.ps.employbox.model.entities.UserAccount;
import isel.ps.employbox.model.input.InJob;
import isel.ps.employbox.model.input.InJobExperience;
import javafx.util.Pair;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.http.MediaType;
import org.springframework.restdocs.JUnitRestDocumentation;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static isel.ps.employbox.DataBaseUtils.prepareDB;
import static junit.framework.TestCase.*;
import static org.springframework.restdocs.webtestclient.WebTestClientRestDocumentation.document;
import static org.springframework.restdocs.webtestclient.WebTestClientRestDocumentation.documentationConfiguration;
import static org.springframework.security.test.web.reactive.server.SecurityMockServerConfigurers.springSecurity;
import static org.springframework.web.reactive.function.client.ExchangeFilterFunctions.basicAuthentication;

@RunWith(SpringRunner.class)
@SpringBootTest
public class JobControllerTests {
    @Rule
    public JUnitRestDocumentation restDocumentation = new JUnitRestDocumentation();
    @Autowired
    private ApplicationContext context;
    @Autowired
    private DataRepository<Job, Long> jobRepo;
    @Autowired
    private DataRepository<JobExperience, Long> jobExperienceRepo;
    private Connection con;
    private WebTestClient webTestClient;
    private long jobId;
    private long accountId;
    private Long jobExperienceId;

    @Before
    public void setUp() throws SQLException {
        con = prepareDB();
        webTestClient = WebTestClient.bindToApplicationContext(context)
                .apply(springSecurity())
                .configureClient()
                .filter(basicAuthentication())
                .filter(documentationConfiguration(restDocumentation))
                .build();

        List<Job> jobs = jobRepo.findWhere(new Pair<>("title", "Great Job")).join();
        assertEquals(1, jobs.size());
        Job job = jobs.get(0);

        List<JobExperience> jobExperiences = jobExperienceRepo.findWhere(new Pair<>("JOBID", job.getIdentityKey())).join();
        assertEquals(1, jobExperiences.size());
        JobExperience jobExperience = jobExperiences.get(0);

        accountId = job.getAccountId();
        jobId = job.getIdentityKey();
        jobExperienceId = jobExperience.getIdentityKey();
    }

    @After
    public void after() throws SQLException {
        con.close();
    }

    @Test
    public void testGetAllJobs(){
        webTestClient
                .get()
                .uri("/jobs")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .consumeWith(document("getAllJobs"));
    }

    @Test
    public void testGetJob(){
        webTestClient
                .get()
                .uri("/jobs/" + jobId)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .consumeWith(document("getJob"));
    }

    @Test
    public void testGetAllJobExperiences(){
        webTestClient
                .get()
                .uri("/jobs/" + jobId + "/experiences")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .consumeWith(document("getAllJobExperiences"));
    }

    @Test
    public void testGetJobExperience(){
        webTestClient
                .get()
                .uri("/jobs/" + jobId + "/experiences/" + jobExperienceId)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .consumeWith(document("getJobExperience"));
    }

    @Test
    @WithMockUser(username = "teste@gmail.com")
    public void testCreateJob() throws Exception {
        InJob inJob = new InJob();
        inJob.setAccountId(accountId);
        inJob.setTitle("Verrryyy gud job, come come");
        inJob.setWage(1);
        inJob.setOfferType("Looking for Worker");
        inJob.setDescription("Lavar o chão");

        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(inJob);

        webTestClient
                .post()
                .uri("/jobs")
                .contentType(MediaType.APPLICATION_JSON)
                .syncBody(json)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .consumeWith(document("createJob"));

        assertTrue(jobRepo.findWhere(new Pair<>("title", "Verrryyy gud job, come come")).join().size() != 0);
    }

    @Test
    @WithMockUser(username = "teste@gmail.com")
    public void testCreateJobExperience() throws Exception {
        InJobExperience inJobExperience = new InJobExperience();
        inJobExperience.setJobId(jobId);
        inJobExperience.setCompetences("C#");
        inJobExperience.setYears((short) 2);

        List<InJobExperience> list = new ArrayList<>();
        list.add(inJobExperience);

        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(list);

        webTestClient
                .post()
                .uri("/jobs/" + jobId + "/experiences")
                .contentType(MediaType.APPLICATION_JSON)
                .syncBody(json)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .consumeWith(document("createJobExperience"));

        assertEquals(1, jobExperienceRepo.findWhere(new Pair<>("jobId", jobId), new Pair<>("COMPETENCES", "C#")).join().size());
    }

    @Test
    @WithMockUser(username = "company1@gmail.com")
    public void testUpdateWrongJob() throws JsonProcessingException {
        InJob inJob = new InJob();
        inJob.setAccountId(accountId);
        inJob.setJobID(jobId);
        inJob.setWage(1);
        inJob.setOfferType("Looking for Worker");
        inJob.setDescription("Sou uma empresa simpatica");

        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(inJob);

        webTestClient
                .put()
                .uri("/jobs/" + jobId)
                .contentType(MediaType.APPLICATION_JSON)
                .syncBody(json)
                .exchange()
                .expectStatus().isUnauthorized()
                .expectBody()
                .consumeWith(document("updateWrongJob"));
    }

    @Test
    @WithMockUser(username = "teste@gmail.com")
    public void testUpdateJob() throws JsonProcessingException {
        InJob inJob = new InJob();
        inJob.setAccountId(accountId);
        inJob.setJobID(jobId);
        inJob.setWage(1);
        inJob.setOfferType("Looking for Worker");
        inJob.setDescription("Sou uma empresa simpatica");

        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(inJob);

        webTestClient
                .put()
                .uri("/jobs/" + jobId)
                .contentType(MediaType.APPLICATION_JSON)
                .syncBody(json)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .consumeWith(document("updateJob"));
    }

    @Test
    @WithAnonymousUser
    public void testDeleteJobWhenNotAuthenticated() {
        webTestClient
                .delete()
                .uri("/jobs/" + jobId)
                .exchange()
                .expectStatus().isUnauthorized()
                .expectBody()
                .consumeWith(document("deleteJobWhenNotAuthenticated"));
    }

    @Test
    @WithMockUser
    public void testDeleteWrongJob() {
        webTestClient
                .delete()
                .uri("/jobs/" + jobId)
                .exchange()
                .expectStatus().isUnauthorized()
                .expectBody()
                .consumeWith(document("deleteWrongJob"));
    }

    @Test
    @WithMockUser(username = "teste@gmail.com")
    public void testDeleteJob(){
        webTestClient
                .delete()
                .uri("/jobs/" + jobId)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .consumeWith(document("deleteJob"));

        assertFalse(jobRepo.findById(jobId).join().isPresent());
    }
}
