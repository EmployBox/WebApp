package isel.ps.employbox.model.binder;

import isel.ps.employbox.model.entities.Curriculum;
import isel.ps.employbox.model.input.InCurriculum;
import isel.ps.employbox.model.output.OutCurriculum;
import org.springframework.hateoas.Resource;
import org.springframework.stereotype.Component;

import java.util.stream.Stream;

@Component
public class CurriculumBinder extends ModelBinder<Curriculum, OutCurriculum, InCurriculum > {


    @Override
    public Stream<Curriculum> bindInput(Stream<InCurriculum> list) {
        return list.map(curr-> new Curriculum(curr.getId(), curr.getTitle()));
    }

    @Override
    public Resource<OutCurriculum> bindOutput(Curriculum object) {
        return null;
    }

    @Override
    public Curriculum bindInput(InCurriculum obj) {
        return new Curriculum(obj.getId(), obj.getTitle());
    }
}
