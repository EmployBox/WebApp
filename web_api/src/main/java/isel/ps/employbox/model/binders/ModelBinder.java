package isel.ps.employbox.model.binders;

import com.github.jayield.rapper.DomainObject;
import com.github.jayield.rapper.utils.CollectionUtils;
import isel.ps.employbox.model.output.Collections.HalCollectionPage;
import isel.ps.employbox.model.output.OutputDto;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public interface ModelBinder<T extends DomainObject, O extends OutputDto, I> {
    CompletableFuture<O> bindOutput(T object);
    T bindInput(I object);

    default Stream<T> bindInput(Stream<I> list) {
        return list.map(this::bindInput);
    }

    default CompletableFuture<HalCollectionPage<T>> bindOutput(CollectionPage<T> elementsPage, Class selfController, Object... parameters) {
        List<CompletableFuture<Object>> items = elementsPage.getPageList()
                .stream()
                .map(t -> bindOutput(t).thenApply(OutputDto::getCollectionItemOutput))
                .collect(Collectors.toList());

        return CollectionUtils.listToCompletableFuture(items)
                .thenApply(objects -> {System.out.println(objects); return objects;})
                .thenApply(objects -> new HalCollectionPage<>(elementsPage, objects, selfController, parameters));
    }
}