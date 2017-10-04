package v1.ticket;

import net.jodah.failsafe.CircuitBreaker;
import net.jodah.failsafe.Failsafe;
import play.db.jpa.JPAApi;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.sql.SQLException;
import java.util.Optional;
import java.util.concurrent.CompletionStage;
import java.util.function.Function;
import java.util.stream.Stream;

import static java.util.concurrent.CompletableFuture.supplyAsync;

/**
 * A repository that provides a non-blocking API with a custom execution context
 * and circuit breaker.
 */
@Singleton
public class JPATicketRepository implements TicketRepository {

    private final JPAApi jpaApi;
    private final TicketExecutionContext ec;
    private final CircuitBreaker circuitBreaker = new CircuitBreaker().withFailureThreshold(1).withSuccessThreshold(3);

    @Inject
    public JPATicketRepository(JPAApi api, TicketExecutionContext ec) {
        this.jpaApi = api;
        this.ec = ec;
    }

    @Override
    public CompletionStage<Stream<TicketData>> list() {
        return supplyAsync(() -> wrap(em -> select(em)), ec);
    }

    @Override
    public CompletionStage<TicketData> create(TicketData ticketData) {
        return supplyAsync(() -> wrap(em -> insert(em, ticketData)), ec);
    }

    @Override
    public CompletionStage<Optional<TicketData>> get(Long id) {
        return supplyAsync(() -> wrap(em -> Failsafe.with(circuitBreaker).get(() -> lookup(em, id))), ec);
    }

    @Override
    public CompletionStage<Optional<TicketData>> update(Long id, TicketData ticketData) {
        return supplyAsync(() -> wrap(em -> Failsafe.with(circuitBreaker).get(() -> modify(em, id, ticketData))), ec);
    }

    private <T> T wrap(Function<EntityManager, T> function) {
        return jpaApi.withTransaction(function);
    }

    private Optional<TicketData> lookup(EntityManager em, Long id) throws SQLException {
        throw new SQLException("Call this to cause the circuit breaker to trip");
        //return Optional.ofNullable(em.find(TicketData.class, id));
    }

    private Stream<TicketData> select(EntityManager em) {
        TypedQuery<TicketData> query = em.createQuery("SELECT p FROM TicketData p", TicketData.class);
        return query.getResultList().stream();
    }

    private Optional<TicketData> modify(EntityManager em, Long id, TicketData ticketData) throws InterruptedException {
        final TicketData data = em.find(TicketData.class, id);
        if (data != null) {
            data.title = ticketData.title;
            data.body = ticketData.body;
        }
        Thread.sleep(10000L);
        return Optional.ofNullable(data);
    }

    private TicketData insert(EntityManager em, TicketData ticketData) {
        return em.merge(ticketData);
    }
}
