package artist;

import utility.CSV;

import java.util.Map;
import java.util.Objects;
import java.util.UUID;

public final class Artist implements CSV
{
    private final Long id;
    private final String name;
    private final String pseudonym;

    public Artist(Long id,String name, String pseudonym)
    {
        this.id = id;
        this.name = name;
        this.pseudonym = pseudonym;
    }
    public Artist(String name, String pseudonym)
    {
        this.id = UUID.randomUUID().getMostSignificantBits() & Long.MAX_VALUE;
        this.name = name;
        this.pseudonym = pseudonym;
    }

    public Long getId() { return  id; }

    public String getName() {
        return name;
    }

    public String getPseudonym() {
        return pseudonym;
    }

    public Artist(Artist artist) {
        this.id = artist.id;
        this.name = artist.name;
        this.pseudonym = artist.pseudonym;
    }

    public void addToMap(Map<Long,Artist> artists)
    {
        artists.put(this.id, this);
    }

    @Override
    public String toString() {
        return "Artist{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", pseudonym='" + pseudonym + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Artist artist = (Artist) o;
        return artist.getId().equals(this.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, pseudonym);
    }

    public String toCSV() {
        return id.toString()+ ',' + name + ',' + pseudonym + '\n';
    }
}
