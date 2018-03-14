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
import java.util.HashMap;
import java.util.Map;

import static isel.ps.EmployBox.dal.dataMapping.utils.ConnectionManager.DBsPath.TESTDB;
import static isel.ps.EmployBox.dal.dataMapping.utils.ConnectionManager.getConnectionManager;
import static isel.ps.EmployBox.dal.dataMapping.utils.MapperRegistry.addEntry;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class UserMapperTests {

    private final Logger log = LoggerFactory.getLogger(UserMapperTests.class);
    private UserMapper mapper = new UserMapper();
    private Map<Long, User> identityMap = mapper.getIdentityMap();

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
        String email = "Test@gmail.com";
        String password = "1234";
        double rating = 4.0;
        String name = "Manel";
        String summary = "Sou um espetaculo";
        String photoUrl = "someurl";

        User user = User.create(email, password, rating, name, summary, photoUrl);

        mapper.insert(user);

        User dbUser = mapper.findForEmail(user.getEmail());
        assertTrue(dbUser != null);
        assertEquals(user.getEmail(), dbUser.getEmail());
        assertEquals(user.getName(), dbUser.getName());
        assertEquals(user.getSummary(), dbUser.getSummary());
        assertEquals(user.getPhotoUrl(), dbUser.getPhotoUrl());

        User mapUser = identityMap.get(dbUser.getIdentityKey());
        assertTrue(mapUser != null);
        assertEquals(mapUser.getIdentityKey(), dbUser.getIdentityKey());
        assertEquals(user.getEmail(), mapUser.getEmail());
        assertEquals(user.getName(), mapUser.getName());
        assertEquals(user.getSummary(), mapUser.getSummary());
        assertEquals(user.getPhotoUrl(), mapUser.getPhotoUrl());
    }

    @Test
    public void updateTest(){
        String email = "432@hotmail.com";
        String password = "43532";
        double rating = 4.0;
        String name = "Baril";
        String summary = "O Baril sou eu";
        String photoUrl = "yetAnotherURL";

        User user = mapper.findForEmail("123@gmail.com");

        User newUser = User.update(user, email, password, rating, name, summary, photoUrl, user.getOfferedJobs(), user.getCurriculums(),
                user.getApplications(), user.getChats(), user.getComments(), user.getRatings(), user.getFollowing());

        mapper.update(newUser);

        User dbUser = mapper.find(user.getIdentityKey());
        assertTrue(dbUser != null);
        assertEquals(email, dbUser.getEmail());
        assertEquals(name, dbUser.getName());
        assertEquals(summary, dbUser.getSummary());
        assertEquals(photoUrl, dbUser.getPhotoUrl());
    }

    @Test
    public void deleteTest(){
        User user = mapper.findForEmail("123@gmail.com");

        mapper.delete(user);

        User dbUser = mapper.find(user.getIdentityKey());
        assertTrue(dbUser == null);
    }

    @Test
    public void identityMapTests(){
        mapper = new UserMapper();
        String email = "123@gmail.com";

        assertTrue(
                identityMap.values()
                        .stream()
                        .noneMatch(user1 -> user1.getEmail().equals(email))
        );

        User user = mapper.findForEmail(email);
        assertTrue(identityMap.get(user.getIdentityKey()) != null);


    }
}
