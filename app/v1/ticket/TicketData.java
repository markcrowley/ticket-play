package v1.ticket;

import javax.persistence.*;

import org.joda.time.DateTime;

import java.util.Date;

/**
 * Ticket data returned from the database
 */
@Entity
@Table(name = "tickets")
public class TicketData {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public Long id;
    public String creator;
    public String description;
    public Date created;
    public Date due;

    public TicketData() {
    }

    public TicketData(String creator, String description) {
        this.creator = creator;
        this.description = description;
        this.created = new Date();
        DateTime dueDateTime = new DateTime(this.created);
        this.due = dueDateTime.plusHours(24).toDate();
    }
}
