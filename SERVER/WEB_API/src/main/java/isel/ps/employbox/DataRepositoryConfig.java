package isel.ps.employbox;

import com.github.jayield.rapper.DataRepository;
import com.github.jayield.rapper.utils.MapperRegistry;
import isel.ps.employbox.model.entities.*;
import isel.ps.employbox.model.entities.Application.ApplicationKeys;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static isel.ps.employbox.model.entities.Follow.FollowKey;

@Configuration
public class DataRepositoryConfig {

    @Bean
    public DataRepository<CurriculumExperience, CurriculumExperience.CurriculumExperienceKey> curriculumExperienceRepoBean(){
        return new DataRepository(MapperRegistry.getRepository(CurriculumExperience.class));
    }

    @Bean
    public DataRepository<Curriculum, Long> curriculumRepoBean(){
        return new DataRepository(MapperRegistry.getRepository(Curriculum.class));
    }

    @Bean
    public DataRepository<Follow, FollowKey> followRepoBean(){
        return new DataRepository(MapperRegistry.getRepository(Follow.class));
    }

    @Bean
    public DataRepository<Application, ApplicationKeys> applicationRepoBean(){
        return new DataRepository(MapperRegistry.getRepository(Application.class));
    }

    @Bean
    public DataRepository<Job, Long> jobRepoBean(){
        return new DataRepository(MapperRegistry.getRepository(Job.class));
    }

    @Bean
    public DataRepository<Rating,Rating.RatingKey> ratingRepoBean(){
        return new DataRepository(MapperRegistry.getRepository(Rating.class));
    }

    @Bean
    public DataRepository<Comment,Long > commentRepoBean(){
        return new DataRepository(MapperRegistry.getRepository(Comment.class));
    }

    @Bean
    public DataRepository<Chat, Long>  chatRepoBean(){
        return new DataRepository(MapperRegistry.getRepository(Chat.class));
    }

    @Bean
    public DataRepository<Message, Long>  messageRepoBean(){
        return new DataRepository(MapperRegistry.getRepository(Message.class));
    }

    @Bean
    public DataRepository<Account, Long>  accountRepoBean(){
        return new DataRepository(MapperRegistry.getRepository(Account.class));
    }

    @Bean
    public DataRepository<User, Long>  userRepoBean(){
        return new DataRepository(MapperRegistry.getRepository(User.class));
    }

    @Bean
    public DataRepository<Company, Long>  companyRepoBean(){
        return new DataRepository(MapperRegistry.getRepository(Company.class));
    }

    @Bean
    public DataRepository<JobExperience, JobExperience.JobExperienceKey> jobExperienceRepoBean(){
        return new DataRepository(MapperRegistry.getRepository(JobExperience.class));
    }

}
