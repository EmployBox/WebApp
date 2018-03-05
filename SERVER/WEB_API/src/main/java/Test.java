import model.Job;
import util.Streamable;

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
        Class type = Job.class;

        Field[] fields = allFieldsFor(type).filter(f ->
                        f.getType().isPrimitive() ||
                        f.getType().isAssignableFrom(String.class) ||
                        f.getType().isAssignableFrom(Date.class))
                .toArray(Field[]::new);

        System.out.println(Arrays.stream(fields)
                .map(Field::getName)
                .collect(Collectors.joining(", ", "select ", " from "+type.getSimpleName())));

        System.out.println(Arrays.stream(fields)
                .map(Field::getName)
                .collect(Collectors.joining(", ", "insert into " + type.getSimpleName()+ "(",
                        ") values ("+ Arrays.stream(fields).map(f -> "?").collect(Collectors.joining(","))+")")));

    }

    public static long test(IntConsumer action){
        long startTime = System.currentTimeMillis();
        IntStream.range(0,1000).forEach(action);
        return System.currentTimeMillis() - startTime;
    }
}
