package isel.ps.employbox.model.binder;

import isel.ps.employbox.model.input.InCurriculum;
import isel.ps.employbox.model.output.OutCurriculum;
import isel.ps.employbox.model.ModelBinder;
import isel.ps.employbox.model.entities.Curriculum;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class CurriculumBinder implements ModelBinder<Curriculum, OutCurriculum, InCurriculum, String> {

    @Override
    public List<OutCurriculum> bindOutput(List<Curriculum> list) {
        return null;
    }

    @Override
    public List<Curriculum> bindInput(List<InCurriculum> list) {
        return list
                .stream()
                .map(curr-> new Curriculum(curr.getId(), curr.getTitle()))
                .collect(Collectors.toList());
    }

    @Override
    public OutCurriculum bindOutput(Curriculum object) {
        return null;
    }

    @Override
    public Curriculum bindInput(InCurriculum obj) {
        return new Curriculum(obj.getId(), obj.getTitle());
    }
}
