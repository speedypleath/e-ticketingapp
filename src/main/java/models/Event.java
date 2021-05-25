package models;

import utility.CSV;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

//import models.Ticket;

public abstract class Event implements CSV
{
    private final Long id;
    private final Date date;
    private final Set<Artist> artists;
    private final Organiser organiser;
    private final String name;
    private final String description;

    public Long getId() {
        return id;
    }

    public Date getDate() {
        return date;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public Set<Artist> getArtists() {
        return artists.stream()
                .map(artist -> new Artist(artist))
                .collect(Collectors.toSet());
    }

    public Organiser getOrganiser() { return new Organiser(organiser); }

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