package v1.ticket;

/**
 * Resource for the API.  This is a presentation class for frontend work.
 */
public class TicketResource {
    private String id;
    private String link;
    private String title;
    private String body;

    public TicketResource() {
    }

    public TicketResource(String id, String link, String title, String body) {
        this.id = id;
        this.link = link;
        this.title = title;
        this.body = body;
    }

    public TicketResource(TicketData data, String link) {
        this.id = data.id.toString();
        this.link = link;
        this.title = data.title;
        this.body = data.body;
    }

    public String getId() {
        return id;
    }

    public String getLink() {
        return link;
    }

    public String getTitle() {
        return title;
    }

    public String getBody() {
        return body;
    }
}
