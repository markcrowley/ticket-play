package v1.ticket;

import javax.persistence.*;

import java.util.Date;

/**
 * Ticket data returned from the database
 */
@Entity
@Table(name = "tickets")
public class TicketData {

    public TicketData() {
    }

    public TicketData(String creator, String description) {
        this.creator = creator;
        this.description = description;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public Long id;
    public String creator;
    public String description;
    public Date created;
    public Date due;
}
