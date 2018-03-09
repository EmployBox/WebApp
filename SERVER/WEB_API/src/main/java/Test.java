import dataMapping.mappers.*;
import dataMapping.utils.MapperSettings;
import model.Job;
import util.Streamable;

import java.io.*;
import java.lang.reflect.Field;
import java.sql.Date;
import java.util.Arrays;
import java.util.function.Consumer;
import java.util.function.IntConsumer;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static util.ReflectionUtils.allFieldsFor;

public class Test {
    public static void main(String[] args) {
        AbstractMapper[] mappers = {
                new AcademicBackgroundMapper(),
                new ApplicationMapper(),
                new ChatMapper(),
                new CommentMapper(),
                new CommentMapper(),
                new CompanyMapper(),
                new CurriculumExperienceMapper(),
                new CurriculumMapper(),
                new FollowMapper(),
                new JobExperienceMapper(),
                new JobMapper(),
                new LocalMapper(),
                new MessageMapper(),
                new ModeratorMapper(),
                new PreviousJobsMapper(),
                new ProjectMapper(),
                new RatingMapper(),
                new UserMapper()
        };

        StringBuilder sb = new StringBuilder();
        for(AbstractMapper m : mappers){
            sb.append(allFieldsFor(m.getClass())
                    .filter(f -> f.getType().isAssignableFrom(MapperSettings.class))
                    .map(f -> {
                        try {
                            f.setAccessible(true);
                            return (MapperSettings)f.get(m);
                        } catch (IllegalAccessException e) {
                            throw new RuntimeException(e);
                        }
                    })
                    .map(MapperSettings::toString)
                    .collect(Collectors.joining(",\n\t", "{" +m.getClass().getSimpleName()+":\n\t", "}\n")));
        }
        try(PrintStream out = new PrintStream(new FileOutputStream("test.txt"))){
            out.print(sb);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
