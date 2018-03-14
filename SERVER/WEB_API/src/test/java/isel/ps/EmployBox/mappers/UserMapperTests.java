package isel.ps.EmployBox.mappers;

import isel.ps.EmployBox.dal.dataMapping.mappers.*;
import isel.ps.EmployBox.dal.dataMapping.utils.ConnectionManager;
import isel.ps.EmployBox.dal.dataMapping.utils.UnitOfWork;
import isel.ps.EmployBox.dal.domainModel.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;

import static isel.ps.EmployBox.dal.dataMapping.utils.ConnectionManager.DBsPath.TESTDB;
import static isel.ps.EmployBox.dal.dataMapping.utils.ConnectionManager.getConnectionManager;
import static isel.ps.EmployBox.dal.dataMapping.utils.MapperRegistry.addEntry;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class UserMapperTests {

    private final Logger log = LoggerFactory.getLogger(UserMapperTests.class);
    private UserMapper mapper = new UserMapper();

    @Before
    public void start() {
        addEntry(User.class, mapper);
        addEntry(Job.class, new JobMapper());
        addEntry(Curriculum.class, new CurriculumMapper());
        addEntry(Application.class, new ApplicationMapper());
        addEntry(Chat.class, new ChatMapper());
        addEntry(Rating.class, new RatingMapper());
        addEntry(Comment.class, new CommentMapper());
        addEntry(Follows.class, new FollowMapper());
        ConnectionManager manager = getConnectionManager(TESTDB.toString());
        UnitOfWork.newCurrent(manager::getConnection);
    }

    @After
    public void finish() throws SQLException {
        log.info("rolling back changes");
        UnitOfWork.getCurrent().rollback();
        UnitOfWork.getCurrent().closeConnection();
    }

    @Test
    public void insertTest() {
        User user = User.create("Test@gmail.com", "1234", 0, "Manel", "Sou um espetaculo", "someurl");

        mapper.insert(user);

        User dbUser = mapper.findForEmail(user.getEmail());
        assertTrue(dbUser != null);
        assertEquals(user.getEmail(), dbUser.getEmail());
        //assertEquals(user.getRating(), dbUser.getRating());
        assertEquals(user.getName(), dbUser.getName());
        assertEquals(user.getSummary(), dbUser.getSummary());
        assertEquals(user.getPhotoUrl(), dbUser.getPhotoUrl());
    }

    @Test
    public void updateTest(){
        User user = mapper.findForEmail("123@gmail.com");

        User newUser = User.update(user, "432@hotmail.com", "43532", 4.0, "Baril", "O Baril sou eu", "yetAnotherURL", user.getOfferedJobs(), user.getCurriculums(),
                user.getApplications(), user.getChats(), user.getComments(), user.getRatings(), user.getFollowing());

        mapper.update(newUser);

        User dbUser = mapper.find(user.getIdentityKey());
        assertTrue(dbUser != null);
        assertEquals("432@hotmail.com", dbUser.getEmail());
        //assertEquals(user.getRating(), dbUser.getRating());
        assertEquals("Baril", dbUser.getName());
        assertEquals("O Baril sou eu", dbUser.getSummary());
        assertEquals("yetAnotherURL", dbUser.getPhotoUrl());
    }
}
