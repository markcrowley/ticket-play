package it;

import com.fasterxml.jackson.databind.JsonNode;
import org.junit.Test;
import play.Application;
import play.inject.guice.GuiceApplicationBuilder;
import play.libs.Json;
import play.mvc.Http;
import play.mvc.Result;
import play.test.WithApplication;
import v1.ticket.TicketData;
import v1.ticket.TicketRepository;
import v1.ticket.TicketResource;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;
import static play.test.Helpers.*;

public class IntegrationTest extends WithApplication {

    @Override
    protected Application provideApplication() {
        return new GuiceApplicationBuilder().build();
    }

    @Test
    public void testList() {
        TicketRepository repository = app.injector().instanceOf(TicketRepository.class);
        repository.create(new TicketData("title", "body"));

        Http.RequestBuilder request = new Http.RequestBuilder().method(GET).uri("/v1/tickets");

        Result result = route(app, request);
        final String body = contentAsString(result);
        assertThat(body, containsString("body"));
    }

    @Test
    public void testTimeoutOnUpdate() {
        TicketRepository repository = app.injector().instanceOf(TicketRepository.class);
        repository.create(new TicketData("title", "body"));

        JsonNode json = Json
                .toJson(new TicketResource("1", "http://localhost:9000/v1/tickets/1", "some title", "somebody"));

        Http.RequestBuilder request = new Http.RequestBuilder().method(POST).bodyJson(json).uri("/v1/tickets/1");

        Result result = route(app, request);
        assertThat(result.status(), equalTo(GATEWAY_TIMEOUT));
    }

    @Test
    public void testCircuitBreakerOnShow() {
        TicketRepository repository = app.injector().instanceOf(TicketRepository.class);
        repository.create(new TicketData("title", "body"));

        Http.RequestBuilder request = new Http.RequestBuilder().method(GET).uri("/v1/tickets/1");

        Result result = route(app, request);
        assertThat(result.status(), equalTo(SERVICE_UNAVAILABLE));
    }
}
