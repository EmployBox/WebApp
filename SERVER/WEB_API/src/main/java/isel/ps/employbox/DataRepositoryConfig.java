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

import static com.github.jayield.rapper.utils.MapperRegistry.getRepository;
import static isel.ps.employbox.model.entities.Follow.FollowKey;

@Configuration
public class DataRepositoryConfig {

    @Bean
    public DataRepository<Project, Long> projectBean(){
        return getRepository(Project.class);
    }

    @Bean
    public DataRepository<PreviousJobs, Long> previousJobsBean(){
        return getRepository(PreviousJobs.class);
    }

    @Bean
    public DataRepository<AcademicBackground, Long> academicBackgroundAcademicBean(){
        return getRepository(AcademicBackground.class);
    }

    @Bean
    public DataRepository<CurriculumExperience, Long> curriculumExperienceRepoBean(){
        return getRepository(CurriculumExperience.class);
    }

    @Bean
    public DataRepository<Curriculum, Long> curriculumRepoBean(){
        return getRepository(Curriculum.class);
    }

    @Bean
    public DataRepository<Follow, FollowKey> followRepoBean(){
        return getRepository(Follow.class);
    }

    @Bean
    public DataRepository<Application, Long> applicationRepoBean(){
        return getRepository(Application.class);
    }

    @Bean
    public DataRepository<Job, Long> jobRepoBean(){
        return getRepository(Job.class);
    }

    @Bean
    public DataRepository<Rating,Rating.RatingKey> ratingRepoBean(){
        return getRepository(Rating.class);
    }

    @Bean
    public DataRepository<Comment,Long > commentRepoBean(){
        return getRepository(Comment.class);
    }

    @Bean
    public DataRepository<Chat, Long>  chatRepoBean(){
        return getRepository(Chat.class);
    }

    @Bean
    public DataRepository<Message, Long>  messageRepoBean(){
        return getRepository(Message.class);
    }

    @Bean
    public DataRepository<Account, Long>  accountRepoBean(){
        return getRepository(Account.class);
    }

    @Bean
    public DataRepository<UserAccount, Long>  userRepoBean(){
        return getRepository(UserAccount.class);
    }

    @Bean
    public DataRepository<Company, Long>  companyRepoBean(){
        return getRepository(Company.class);
    }

    @Bean
    public DataRepository<JobExperience, Long> jobExperienceRepoBean(){
        return getRepository(JobExperience.class);
    }
}
