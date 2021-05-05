package event;

import artist.Artist;
import user.Organiser;
import utility.CSV;

import java.util.*;

//import transaction.Ticket;

public abstract class Event implements CSV
{
    private Long id;
    private Date date;
    private final Set<Artist> artists;
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

    public Set<Artist> getArtists() { return artists; }

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
        this.artists = new HashSet<>();
        for(var artist : event.artists)
            this.artists.add(new Artist(artist));
    }

    Event(Builder<?> builder)
    {
        this.id = builder.id != null ? builder.id : UUID.randomUUID().getMostSignificantBits() & Long.MAX_VALUE;
        this.date = builder.date;
        this.artists = builder.artists;
        this.organiser = builder.organiser;
        this.name = builder.name;
        this.description = builder.description;
    }
    public abstract static class Builder<T extends Builder>
    {
        Date date;
        Set<Artist> artists = new HashSet<>();
        Organiser organiser;
        String name;
        String description;
        Long id;
        abstract Event build();
        protected abstract T self();
        public T id(Long id)
        {
            this.id = id;
            return self();
        }
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

    @Override
    public String toCSV() {
        return id + "," + name + "," + description + "," + date.toString() + "," + organiser.getUsername()
                + "," + artists.stream()
                .map(artist -> artist.getId().toString())
                .reduce("", ((s, artist) -> s + "/" + artist))
                .substring(1);
    }
}