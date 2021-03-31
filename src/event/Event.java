package event;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

//import transaction.Ticket;
import user.Client;
import user.Organiser;
import artist.Artist;

// dupa ce am pierdut aproximativ 4 ore sa ma documentez cum se face un builder am descoperit ca exista deja un model in lab 5 of

public abstract class Event
{
    private Date date;
    private List<Artist> artists;
    private final Organiser organiser;
    private String name;
    private String description;

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void addArtist(Artist artist) { artists.add(artist); }

    public List<Artist> getArtists() { return artists; }

    public Organiser getOrganiser() { return organiser; }

    @Override
    public String toString() {
        return "Event{" +
                "date=" + date +
                ", artists=" + artists +
                ", organiser=" + organiser +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                '}';
    }

    abstract Event copyEvent(Event event);

    Event(Event event)
    {
        Organiser organiser = new Organiser(event.organiser.getUsername());
        this.organiser = organiser;
        this.date = new Date(event.getDate().getTime());
        this.name = event.name;
        this.description = event.description;
        this.artists = new ArrayList<>();
        for(var artist : artists)
            this.artists.add(new Artist(artist));
    }

    Event(Builder<?> builder)
    {
        this.date = builder.date;
        this.artists = builder.artists;
        this.organiser = builder.organiser;
        this.name = builder.name;
        this.description = builder.description;
    }
    public abstract static class Builder<T extends Builder>
    {
        Date date;
        List<Artist> artists;
        Organiser organiser;
        int ticketsNo;
        String name;
        String description;
        abstract Event build();
        protected abstract T self();
        public T description(String description)
        {
            this.description = description;
            return self();
        }
        public T organiser(Organiser organiser)
        {
            this.organiser = organiser;
            return self();
        }
        public T date(Date date)
        {
            this.date = date;
            return self();
        }
        public T name(String name)
        {
            this.name = name;
            return self();
        }
        public T addArtist(Artist artist)
        {
            this.artists.add(artist);
            return self();
        }
    }
}