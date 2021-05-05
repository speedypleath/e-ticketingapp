package artist;

import utility.CSV;

import java.util.Map;
import java.util.UUID;

public class Artist implements CSV
{
    private final Long id;
    private String name;
    private String pseudonym;

    public Artist(){
        this.id = UUID.randomUUID().getMostSignificantBits() & Long.MAX_VALUE;
    }

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

    public void setName(String name) {
        this.name = name;
    }

    public String getPseudonym() {
        return pseudonym;
    }

    public void setPseudonym(String pseudonym) {
        this.pseudonym = pseudonym;
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

    public String toCSV() {
        return id.toString()+ ',' + name + ',' + pseudonym + '\n';
    }
}
