package isel.ps.employbox;

import com.github.jayield.rapper.DataRepository;
import com.github.jayield.rapper.utils.MapperRegistry;
import isel.ps.employbox.model.entities.*;
import isel.ps.employbox.model.entities.curricula.childs.AcademicBackground;
import isel.ps.employbox.model.entities.curricula.childs.CurriculumExperience;
import isel.ps.employbox.model.entities.curricula.childs.PreviousJobs;
import isel.ps.employbox.model.entities.curricula.childs.Project;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static isel.ps.employbox.model.entities.Follow.FollowKey;

@Configuration
public class DataRepositoryConfig {

    @Bean
    public DataRepository<Project, Long> projectBean(){
        return MapperRegistry.getRepository(Project.class);
    }

    @Bean
    public DataRepository<PreviousJobs, Long> previousJobsBean(){
        return MapperRegistry.getRepository(PreviousJobs.class);
    }

    @Bean
    public DataRepository<AcademicBackground, Long> academicBackgroundAcademicBean(){
        return MapperRegistry.getRepository(AcademicBackground.class);
    }

    @Bean
    public DataRepository<CurriculumExperience, Long> curriculumExperienceRepoBean(){
        return MapperRegistry.getRepository(CurriculumExperience.class);
    }

    @Bean
    public DataRepository<Curriculum, Long> curriculumRepoBean(){
        return MapperRegistry.getRepository(Curriculum.class);
    }

    @Bean
    public DataRepository<Follow, FollowKey> followRepoBean(){
        return MapperRegistry.getRepository(Follow.class);
    }

    @Bean
    public DataRepository<Application, Long> applicationRepoBean(){
        return MapperRegistry.getRepository(Application.class);
    }

    @Bean
    public DataRepository<Job, Long> jobRepoBean(){
        return MapperRegistry.getRepository(Job.class);
    }

    @Bean
    public DataRepository<Rating,Rating.RatingKey> ratingRepoBean(){
        return MapperRegistry.getRepository(Rating.class);
    }

    @Bean
    public DataRepository<Comment,Long > commentRepoBean(){
        return MapperRegistry.getRepository(Comment.class);
    }

    @Bean
    public DataRepository<Chat, Long>  chatRepoBean(){
        return MapperRegistry.getRepository(Chat.class);
    }

    @Bean
    public DataRepository<Message, Long>  messageRepoBean(){
        return MapperRegistry.getRepository(Message.class);
    }

    @Bean
    public DataRepository<Account, Long>  accountRepoBean(){
        return MapperRegistry.getRepository(Account.class);
    }

    @Bean
    public DataRepository<UserAccount, Long>  userRepoBean(){
        return MapperRegistry.getRepository(UserAccount.class);
    }

    @Bean
    public DataRepository<Company, Long>  companyRepoBean(){
        return MapperRegistry.getRepository(Company.class);
    }

    @Bean
    public DataRepository<JobExperience, Long> jobExperienceRepoBean(){
        return MapperRegistry.getRepository(JobExperience.class);
    }
}
