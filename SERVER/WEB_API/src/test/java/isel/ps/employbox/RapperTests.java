package isel.ps.employbox;

import com.github.jayield.rapper.DataRepository;
import com.github.jayield.rapper.utils.MapperRegistry;
import isel.ps.employbox.model.entities.*;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class RapperTests {

    private final Logger logger = LoggerFactory.getLogger(RapperTests.class);

    @Test
    public void test() {
        MapperRegistry.getRepository(Company.class);
        MapperRegistry.getRepository(Follow.class);
        MapperRegistry.getRepository(Application.class);
        MapperRegistry.getRepository(Job.class);
        MapperRegistry.getRepository(Rating.class);
        MapperRegistry.getRepository(Comment.class);
        MapperRegistry.getRepository(Chat.class);
        MapperRegistry.getRepository(Message.class);
        MapperRegistry.getRepository(Account.class);
        MapperRegistry.getRepository(UserAccount.class);
        MapperRegistry.getRepository(Company.class);
        MapperRegistry.getRepository(JobExperience.class);
    }
}