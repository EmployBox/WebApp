package isel.ps.employbox.model.binder;

import isel.ps.employbox.model.output.HalCollection;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.ResourceSupport;
import org.springframework.hateoas.Resources;
import org.springframework.hateoas.core.EmbeddedWrapper;
import org.springframework.hateoas.core.EmbeddedWrappers;
import org.springframework.hateoas.core.Relation;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Stream;

public interface ModelBinder<T, O extends ResourceSupport, I> {
    Mono<O> bindOutput(CompletableFuture<T> object);
    T bindInput(I object);

    default Stream<T> bindInput(Stream<I> list){
        return list.map(this::bindInput);
    }

    /*
    default Mono<HalCollection> bindOutput(CompletableFuture<Stream<T>> listCompletableFuture, Class selfController, Object ... parameters) {
        return Mono.fromFuture(
                listCompletableFuture
                        .thenApply(
                                (stream) -> {
                                    EmbeddedWrappers wrappers = new EmbeddedWrappers(true);
                                    ArrayList<EmbeddedWrapper> embeddedPropertieList = new ArrayList<>();
                                    stream.map(curr -> bindOutput(CompletableFuture.completedFuture(curr)).block())
                                            .forEach(curr -> embeddedPropertieList.add(wrappers.wrap(new CollectionItem(curr.getId()))));

                                    return new HalCollection(
                                            embeddedPropertieList.size(),
                                            new Resources<>(embeddedPropertieList),
                                            selfController,
                                            parameters);
                                }
                        )
        );
    }*/

    default Mono<HalCollection> bindOutput(CompletableFuture<Stream<T>> listCompletableFuture, Class selfController, Object ... parameters) {
        EmbeddedWrappers wrappers = new EmbeddedWrappers(true);
        ArrayList<EmbeddedWrapper> embeddedChecklists = new ArrayList<>();
        Stream<T> list = listCompletableFuture.join();

        list.map(curr-> bindOutput( CompletableFuture.completedFuture(curr)).block())
                .forEach(curr -> embeddedChecklists.add( wrappers.wrap(new CollectionItem(curr.getId()))));

        HalCollection ret = new HalCollection(
                embeddedChecklists.size(),
                new Resources<>(embeddedChecklists),
                selfController,
                parameters
        );
        return Mono.fromFuture( CompletableFuture.completedFuture(ret));
    }

    @Relation(value= "item", collectionRelation = "items")
    class CollectionItem extends ResourceSupport {

        public CollectionItem(Link self){
            this.add(self);
        }
    }
}