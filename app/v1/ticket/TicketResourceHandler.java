package v1.ticket;

import com.palominolabs.http.url.UrlBuilder;
import play.libs.concurrent.HttpExecutionContext;
import play.mvc.Http;

import javax.inject.Inject;
import java.nio.charset.CharacterCodingException;
import java.util.Optional;
import java.util.concurrent.CompletionStage;
import java.util.stream.Stream;

/**
 * Handles presentation of Post resources, which map to JSON.
 */
public class TicketResourceHandler {

    private final TicketRepository repository;
    private final HttpExecutionContext ec;

    @Inject
    public TicketResourceHandler(TicketRepository repository, HttpExecutionContext ec) {
        this.repository = repository;
        this.ec = ec;
    }

    public CompletionStage<Stream<TicketResource>> find() {
        return repository.list().thenApplyAsync(postDataStream -> {
            return postDataStream.map(data -> new TicketResource(data, link(data)));
        }, ec.current());
    }

    public CompletionStage<TicketResource> create(TicketResource resource) {
        final TicketData data = new TicketData(resource.getTitle(), resource.getBody());
        return repository.create(data).thenApplyAsync(savedData -> {
            return new TicketResource(savedData, link(savedData));
        }, ec.current());
    }

    public CompletionStage<Optional<TicketResource>> lookup(String id) {
        return repository.get(Long.parseLong(id)).thenApplyAsync(optionalData -> {
            return optionalData.map(data -> new TicketResource(data, link(data)));
        }, ec.current());
    }

    public CompletionStage<Optional<TicketResource>> update(String id, TicketResource resource) {
        final TicketData data = new TicketData(resource.getTitle(), resource.getBody());
        return repository.update(Long.parseLong(id), data).thenApplyAsync(optionalData -> {
            return optionalData.map(op -> new TicketResource(op, link(op)));
        }, ec.current());
    }

    private String link(TicketData data) {
        // Make a point of using request context here, even if it's a bit strange
        final Http.Request request = Http.Context.current().request();
        final String[] hostPort = request.host().split(":");
        String host = hostPort[0];
        int port = (hostPort.length == 2) ? Integer.parseInt(hostPort[1]) : -1;
        final String scheme = request.secure() ? "https" : "http";
        try {
            return UrlBuilder.forHost(scheme, host, port).pathSegments("v1", "posts", data.id.toString()).toUrlString();
        } catch (CharacterCodingException e) {
            throw new IllegalStateException(e);
        }
    }
}
