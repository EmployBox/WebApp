package isel.ps.employbox.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.jayield.rapper.mapper.DataMapper;
import com.github.jayield.rapper.mapper.conditions.EqualAndCondition;
import com.github.jayield.rapper.unitofwork.UnitOfWork;
import isel.ps.employbox.exceptions.ResourceNotFoundException;
import isel.ps.employbox.model.entities.Job;
import isel.ps.employbox.model.entities.JobExperience;
import isel.ps.employbox.model.input.InJob;
import isel.ps.employbox.model.input.InJobExperience;
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
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.util.ArrayList;
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
public class JobControllerTests {
    @Rule
    public JUnitRestDocumentation restDocumentation = new JUnitRestDocumentation();
    @Autowired
    private ApplicationContext context;

    private static final Logger logger = LoggerFactory.getLogger(JobControllerTests.class);
    private WebTestClient webTestClient;
    private Long accountId;
    private Job job;
    private JobExperience jobExperience;

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
        DataMapper<Job, Long> jobMapper = getMapper(Job.class, unitOfWork);
        List<Job> jobs = jobMapper.find( new EqualAndCondition<>("title", "Great Job")).join();
        assertEquals(1, jobs.size());
        job = jobs.get(0);

        DataMapper<JobExperience, Long> jobExperienceMapper = getMapper(JobExperience.class, unitOfWork);
        List<JobExperience> jobExperiences = jobExperienceMapper.find( new EqualAndCondition<>("JOBID", job.getIdentityKey())).join();
        assertEquals(1, jobExperiences.size());
        jobExperience = jobExperiences.get(0);

        accountId = job.getAccount().getForeignKey();

        unitOfWork.commit().join();
    }

    @After
    public void after() {
        int openedConnections = UnitOfWork.getNumberOfOpenConnections().get();
        logger.info("OPENED CONNECTIONS - {}", openedConnections);
        assertEquals(0, openedConnections);
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
                .uri("/jobs/" + job.getIdentityKey())
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .consumeWith(document("getJob"));
    }

    @Test
    public void testGetAllJobExperiences(){
        webTestClient
                .get()
                .uri("/jobs/" + job.getIdentityKey() + "/experiences")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .consumeWith(document("getAllJobExperiences"));
    }

    @Test
    public void testGetJobExperience(){
        webTestClient
                .get()
                .uri("/jobs/" + job.getIdentityKey() + "/experiences/" + jobExperience.getIdentityKey())
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
        UnitOfWork unitOfWork = new UnitOfWork();

        webTestClient
                .post()
                .uri("/jobs")
                .contentType(MediaType.APPLICATION_JSON)
                .syncBody(json)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .consumeWith(document("createJob"));
        DataMapper<Job, Long> jobMapper = getMapper(Job.class, unitOfWork);
        assertTrue(jobMapper.find( new EqualAndCondition<>("title", "Verrryyy gud job, come come")).join().size() != 0);
        unitOfWork.commit().join();
    }

    @Test
    @WithMockUser(username = "teste@gmail.com")
    public void testCreateJobExperience() throws Exception {
        InJobExperience inJobExperience = new InJobExperience();
        inJobExperience.setJobId(job.getIdentityKey());
        inJobExperience.setCompetences("C#");
        inJobExperience.setYears((short) 2);

        List<InJobExperience> list = new ArrayList<>();
        list.add(inJobExperience);

        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(list);
        UnitOfWork unitOfWork = new UnitOfWork();
        webTestClient
                .post()
                .uri("/jobs/" + job.getIdentityKey() + "/experiences")
                .contentType(MediaType.APPLICATION_JSON)
                .syncBody(json)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .consumeWith(document("createJobExperience"));
        DataMapper<JobExperience, Long> jobExperienceMapper = getMapper(JobExperience.class, unitOfWork);
        assertEquals(1, jobExperienceMapper.find( new EqualAndCondition<>("jobId", job.getIdentityKey()), new EqualAndCondition<>("COMPETENCES", "C#")).join().size());
        unitOfWork.commit().join();
    }

    @Test
    @WithMockUser(username = "company1@gmail.com")
    public void testUpdateWrongJob() throws JsonProcessingException {
        InJob inJob = new InJob();
        inJob.setAccountId(accountId);
        inJob.setJobID(job.getIdentityKey());
        inJob.setWage(1);
        inJob.setOfferType("Looking for Worker");
        inJob.setDescription("Sou uma empresa simpatica");

        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(inJob);

        webTestClient
                .put()
                .uri("/jobs/" + job.getIdentityKey())
                .contentType(MediaType.APPLICATION_JSON)
                .syncBody(json)
                .exchange()
                .expectStatus().isUnauthorized()
                .expectBody()
                .consumeWith(document("updateWrongJob"));
    }

    @Test
    @WithMockUser(username = "company1@gmail.com")
    public void testUpdateWrongJobExperience() throws JsonProcessingException {
        InJobExperience inJobExperience = new InJobExperience();
        inJobExperience.setJobExperienceId(jobExperience.getIdentityKey());
        inJobExperience.setJobId(job.getIdentityKey());
        inJobExperience.setCompetences("C#");
        inJobExperience.setYears((short) 2);

        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(inJobExperience);

        webTestClient
                .put()
                .uri("/jobs/" + job.getIdentityKey() + "/experiences/" + jobExperience.getIdentityKey())
                .contentType(MediaType.APPLICATION_JSON)
                .syncBody(json)
                .exchange()
                .expectStatus().isUnauthorized()
                .expectBody()
                .consumeWith(document("updateWrongJobExperience"));
    }

    @Test
    @WithMockUser(username = "teste@gmail.com")
    public void testUpdateJob() throws JsonProcessingException {
        InJob inJob = new InJob();
        inJob.setAccountId(accountId);
        inJob.setJobID(job.getIdentityKey());
        inJob.setTitle("Qualquer coisa");
        inJob.setWage(1);
        inJob.setOfferType("Looking for Worker");
        inJob.setDescription("Sou uma empresa simpatica");
        inJob.setVersion(job.getVersion());

        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(inJob);
        UnitOfWork unitOfWork = new UnitOfWork();

        webTestClient
                .put()
                .uri("/jobs/" + job.getIdentityKey())
                .contentType(MediaType.APPLICATION_JSON)
                .syncBody(json)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .consumeWith(document("updateJob"));
        DataMapper<Job, Long> jobMapper = getMapper(Job.class, unitOfWork);
        Job updatedJob = jobMapper.findById( job.getIdentityKey()).join().orElseThrow(() -> new ResourceNotFoundException("Job not found"));
        unitOfWork.commit().join();

        assertEquals("Looking for Worker", updatedJob.getOfferType());
        assertEquals("Sou uma empresa simpatica", updatedJob.getDescription());
        assertEquals(1, updatedJob.getWage());
        assertEquals("Qualquer coisa", updatedJob.getTitle());
    }

    @Test
    @WithMockUser(username = "teste@gmail.com")
    public void testUpdateJobExperience() throws JsonProcessingException {
        InJobExperience inJobExperience = new InJobExperience();
        inJobExperience.setJobExperienceId(jobExperience.getIdentityKey());
        inJobExperience.setJobId(job.getIdentityKey());
        inJobExperience.setCompetences("C#");
        inJobExperience.setYears((short) 2);
        inJobExperience.setVersion(jobExperience.getVersion());

        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(inJobExperience);
        UnitOfWork unitOfWork = new UnitOfWork();
        webTestClient
                .put()
                .uri("/jobs/" + job.getIdentityKey() + "/experiences/" + jobExperience.getIdentityKey())
                .contentType(MediaType.APPLICATION_JSON)
                .syncBody(json)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .consumeWith(document("updateJobExperience"));

        DataMapper<JobExperience, Long> jobExperienceMapper = getMapper(JobExperience.class, unitOfWork);
        JobExperience updatedJobExperience = jobExperienceMapper.findById( jobExperience.getIdentityKey()).join().orElseThrow(() -> new ResourceNotFoundException("JobExperience not found"));
        unitOfWork.commit().join();

        assertEquals((short) 2, updatedJobExperience.getYears());
        assertEquals("C#", updatedJobExperience.getCompetences());
    }

    @Test
    @WithAnonymousUser
    public void testDeleteJobWhenNotAuthenticated() {
        webTestClient
                .delete()
                .uri("/jobs/" + job.getIdentityKey())
                .exchange()
                .expectStatus().isNotFound()
                .expectBody()
                .consumeWith(document("deleteJobWhenNotAuthenticated"));
    }

    @Test
    @WithMockUser
    public void testDeleteWrongJob() {
        webTestClient
                .delete()
                .uri("/jobs/" + job.getIdentityKey())
                .exchange()
                .expectStatus().isNotFound()
                .expectBody()
                .consumeWith(document("deleteWrongJob"));
    }

    @Test
    @WithMockUser(username = "teste@gmail.com")
    public void testDeleteJob(){
        webTestClient
                .delete()
                .uri("/jobs/" + job.getIdentityKey())
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .consumeWith(document("deleteJob"));
        UnitOfWork unitOfWork = new UnitOfWork();
        DataMapper<Job, Long> jobMapper = getMapper(Job.class, unitOfWork);
        assertFalse(jobMapper.findById( job.getIdentityKey()).join().isPresent());
        unitOfWork.commit().join();
    }
}
