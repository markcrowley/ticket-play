package v1.ticket;

import java.util.Date;

/**
 * Resource for the API.  This is a presentation class for frontend work.
 */
public class TicketResource {
    private String id;
    private String link;
    public String creator;
    public String description;
    public Date created;
    public Date due;

    public TicketResource() {
    }

    public TicketResource(String id, String link, String creator, String description) {
        this.id = id;
        this.link = link;
        this.creator = creator;
        this.description = description;
    }

    public TicketResource(TicketData data, String link) {
        this.id = data.id.toString();
        this.link = link;
        this.creator = data.creator;
        this.description = data.description;
        this.created = new Date();
        this.due = new Date();
    }

    public String getId() {
        return id;
    }

    public String getLink() {
        return link;
    }

    public String getCreator() {
        return creator;
    }

    public String getDescription() {
        return description;
    }

    public Date getCreated() {
        return created;
    }

    public Date getDue() {
        return due;
    }
}
