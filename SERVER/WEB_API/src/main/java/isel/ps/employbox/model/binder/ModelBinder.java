package isel.ps.employbox.model.binder;

import isel.ps.employbox.model.output.HalCollection;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.ResourceSupport;
import org.springframework.hateoas.Resources;
import org.springframework.hateoas.core.EmbeddedWrapper;
import org.springframework.hateoas.core.EmbeddedWrappers;
import org.springframework.hateoas.core.Relation;

import java.util.ArrayList;
import java.util.stream.Stream;

public abstract class ModelBinder<T, O extends ResourceSupport, I> {
    abstract Stream<T> bindInput(Stream<I> list);
    abstract Resource<O> bindOutput(T object);
    abstract T bindInput(I object);

    public Resource<HalCollection> bindOutput(Stream<T> list, Class selfController, Object ... pathVariables) {
        EmbeddedWrappers wrappers = new EmbeddedWrappers(true);
        ArrayList<EmbeddedWrapper> embeddedChecklists = new ArrayList<>();
        list.map(this::bindOutput)
                .forEach(curr -> embeddedChecklists.add( wrappers.wrap( new CollectionItem( curr.getContent().getId() ))));

        return new Resource<>(
            new HalCollection(
                embeddedChecklists.size(),
                new Resources<>(embeddedChecklists),
                selfController,
                pathVariables
            )
        );
    }

    @Relation(value= "item", collectionRelation = "items")
    private class CollectionItem extends ResourceSupport {

        public CollectionItem(Link self){
            this.add(self);
        }
    }
}