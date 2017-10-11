package v1.ticket;

import com.fasterxml.jackson.databind.JsonNode;
import play.libs.Json;
import play.libs.concurrent.HttpExecutionContext;
import play.mvc.*;

import javax.inject.Inject;
import java.util.List;
import java.util.concurrent.CompletionStage;
import java.util.stream.Collectors;

@With(TicketAction.class)
public class TicketController extends Controller {

    private HttpExecutionContext ec;
    private TicketResourceHandler handler;

    @Inject
    public TicketController(HttpExecutionContext ec, TicketResourceHandler handler) {
        this.ec = ec;
        this.handler = handler;
    }

    public CompletionStage<Result> list() {
        return handler.find().thenApplyAsync(tickets -> {
            final List<TicketResource> ticketList = tickets.collect(Collectors.toList());
            return ok(Json.toJson(ticketList));
        }, ec.current());
    }

    public CompletionStage<Result> show(String id) {
        return handler.lookup(id).thenApplyAsync(optionalResource -> {
            return optionalResource.map(resource -> ok(Json.toJson(resource))).orElseGet(() -> notFound());
        }, ec.current());
    }

    public CompletionStage<Result> update(String id) {
        JsonNode json = request().body().asJson();
        TicketResource resource = Json.fromJson(json, TicketResource.class);
        return handler.update(id, resource).thenApplyAsync(optionalResource -> {
            return optionalResource.map(r -> ok(Json.toJson(r))).orElseGet(() -> notFound());
        }, ec.current());
    }

    public CompletionStage<Result> create() {
        JsonNode json = request().body().asJson();
        System.out.println(json.toString());
        final TicketResource resource = Json.fromJson(json, TicketResource.class);
        return handler.create(resource).thenApplyAsync(savedResource -> {
            return created(Json.toJson(savedResource));
        }, ec.current());
    }
}
