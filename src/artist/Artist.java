package artist;

public class Artist
{
    private String name;
    private String pseudonym;

    public Artist(Artist artist) {
        this.name = artist.name;
        this.pseudonym = artist.pseudonym;
    }
}
