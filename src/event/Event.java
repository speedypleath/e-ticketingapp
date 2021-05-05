package event;

import artist.Artist;
import user.Organiser;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

//import transaction.Ticket;

public abstract class Event
{
    private Long id;
    private Date date;
    private final List<Artist> artists;
    private final Organiser organiser;
    private String name;
    private String description;

    public Long getId() {
        return id;
    }

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

    public void removeArtist(Artist artist) {artists.remove(artist); }

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

    Event(Event event)
    {
        this.id = event.getId();
        this.organiser = new Organiser(event.organiser);
        this.date = new Date(event.getDate().getTime());
        this.name = event.name;
        this.description = event.description;
        this.artists = new ArrayList<>();
        for(var artist : event.artists)
            this.artists.add(new Artist(artist));
    }

    Event(Builder<?> builder)
    {
        this.id = UUID.randomUUID().getMostSignificantBits() & Long.MAX_VALUE;
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