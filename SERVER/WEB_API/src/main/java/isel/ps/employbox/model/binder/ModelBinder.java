package isel.ps.employbox.model.binder;

import isel.ps.employbox.model.output.CollectionItemSupplier;
import isel.ps.employbox.model.output.HalCollectionPage;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public interface ModelBinder<T, O, I> {
    Mono<O> bindOutput(CompletableFuture<T> object);

    T bindInput(I object);

    default Stream<T> bindInput(Stream<I> list) {
        return list.map(this::bindInput);
    }

    default Mono<HalCollectionPage> bindOutput(
            CompletableFuture<CollectionPage<T>> elementsPage,
            Class selfController,
            Object... parameters) {
        return Mono.fromFuture(
                elementsPage
                        .thenApply(
                                (CollectionPage<T> elements) -> {
                                    Stream<T> list = elements.getPageList();

                                    List<Object> items = list.map(curr-> ((CollectionItemSupplier)bindOutput(CompletableFuture.completedFuture(curr))
                                            .block())
                                            .getCollectionItemOutput())
                                            .collect(Collectors.toList());

                                    return new HalCollectionPage(
                                            elements,
                                            items,
                                            selfController,
                                            parameters
                                    );
                                }

                        )
        );
    }
}