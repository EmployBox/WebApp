package isel.ps.employbox;

import com.github.jayield.rapper.DataRepository;
import com.github.jayield.rapper.utils.MapperRegistry;
import isel.ps.employbox.model.entities.*;
import isel.ps.employbox.model.entities.CurriculumChilds.AcademicBackground;
import isel.ps.employbox.model.entities.CurriculumChilds.CurriculumExperience;
import isel.ps.employbox.model.entities.CurriculumChilds.PreviousJobs;
import isel.ps.employbox.model.entities.CurriculumChilds.Project;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static isel.ps.employbox.model.entities.Follow.FollowKey;

@Configuration
public class DataRepositoryConfig {

    @Bean
    public DataRepository<Project, Long> projectBean(){
        return new DataRepository(MapperRegistry.getRepository(Project.class));
    }

    @Bean
    public DataRepository<PreviousJobs, Long> previousJobsBean(){
        return new DataRepository(MapperRegistry.getRepository(PreviousJobs.class));
    }

    @Bean
    public DataRepository<AcademicBackground, Long> academicBackgroundAcademicBean(){
        return new DataRepository(MapperRegistry.getRepository(AcademicBackground.class));
    }

    @Bean
    public DataRepository<CurriculumExperience, Long> curriculumExperienceRepoBean(){
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
    public DataRepository<Application, Long> applicationRepoBean(){
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
    public DataRepository<JobExperience, Long> jobExperienceRepoBean(){
        return new DataRepository(MapperRegistry.getRepository(JobExperience.class));
    }

}
