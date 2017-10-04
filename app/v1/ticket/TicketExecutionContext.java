package v1.ticket;

import akka.actor.ActorSystem;
import play.libs.concurrent.CustomExecutionContext;

import javax.inject.Inject;

/**
 * Custom execution context wired to "ticket.repository" thread pool
 */
public class TicketExecutionContext extends CustomExecutionContext {

    @Inject
    public TicketExecutionContext(ActorSystem actorSystem) {
        super(actorSystem, "ticket.repository");
    }
}
