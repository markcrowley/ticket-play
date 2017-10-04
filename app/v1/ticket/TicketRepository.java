package v1.ticket;

import java.util.Optional;
import java.util.concurrent.CompletionStage;
import java.util.stream.Stream;

public interface TicketRepository {

    CompletionStage<Stream<TicketData>> list();

    CompletionStage<TicketData> create(TicketData ticketData);

    CompletionStage<Optional<TicketData>> get(Long id);

    CompletionStage<Optional<TicketData>> update(Long id, TicketData ticketData);
}
