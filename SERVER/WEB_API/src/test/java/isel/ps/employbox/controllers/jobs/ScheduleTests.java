package isel.ps.employbox.controllers.jobs;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.jayield.rapper.mapper.DataMapper;
import com.github.jayield.rapper.mapper.conditions.EqualAndCondition;
import com.github.jayield.rapper.unitofwork.UnitOfWork;
import isel.ps.employbox.model.entities.jobs.Job;
import isel.ps.employbox.model.entities.jobs.Schedule;
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
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.io.IOException;
import java.util.List;

import static com.github.jayield.rapper.mapper.MapperRegistry.getMapper;
import static isel.ps.employbox.DataBaseUtils.prepareDB;
import static junit.framework.TestCase.assertEquals;
import static org.springframework.restdocs.webtestclient.WebTestClientRestDocumentation.documentationConfiguration;
import static org.springframework.security.test.web.reactive.server.SecurityMockServerConfigurers.springSecurity;
import static org.springframework.web.reactive.function.client.ExchangeFilterFunctions.basicAuthentication;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ScheduleTests {

    @Rule
    public JUnitRestDocumentation restDocumentation = new JUnitRestDocumentation();
    @Autowired
    private ApplicationContext context;

    private static final Logger logger = LoggerFactory.getLogger(JobControllerTests.class);
    private WebTestClient webTestClient;
    private Long accountId;
    private Job job;
    private Schedule schedule;

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
        DataMapper<Schedule, Long> scheduleMapper = getMapper(Schedule.class, unitOfWork);


        List<Job> jobs = jobMapper.find( new EqualAndCondition<>("title", "Great Job")).join();
        assertEquals(1, jobs.size());
        job = jobs.get(0);

        List<Schedule> schedules = scheduleMapper.find(new EqualAndCondition<>("jobId", job.getIdentityKey())).join();
        assertEquals(1, schedules.size());
        schedule = schedules.get(0);

        accountId = job.getAccount().getForeignKey();

        unitOfWork.commit().join();
    }

    @After
    public void after() {
        int openedConnections = UnitOfWork.getNumberOfOpenConnections().get();
        logger.info("OPENED CONNECTIONS - {}", openedConnections);
        assertEquals(0, openedConnections);
    }
    //todo next tests
    @Test
    public void testGetAllSchedules() throws IOException {
        String body = new String( webTestClient
                .get()
                .uri("/jobs/"+job.getIdentityKey()+"/schedules")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .returnResult()
                .getResponseBody());

        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(body);
    }
}
