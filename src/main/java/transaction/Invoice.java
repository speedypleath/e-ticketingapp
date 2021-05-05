package transaction;

import java.util.Date;

public final class Invoice
{
    private final Date date;
    Invoice(Date date) {
        this.date = date;
    }

    public Date getDate() {
        return date;
    }
}
