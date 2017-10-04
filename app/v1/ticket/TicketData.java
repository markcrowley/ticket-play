package v1.ticket;

import javax.persistence.*;

/**
 * Data returned from the database
 */
@Entity
@Table(name = "tickets")
public class TicketData {

    public TicketData() {
    }

    public TicketData(String title, String body) {
        this.title = title;
        this.body = body;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public Long id;
    public String title;
    public String body;
}
