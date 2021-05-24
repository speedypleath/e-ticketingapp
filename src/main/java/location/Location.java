package location;

import utility.CSV;

import java.util.UUID;

public final class Location implements CSV
{
    private final Long id;
    private final String name;
    private final String address;
    private final Integer capacity;

    public Location(String name, String address, Integer capacity){
        this.id = UUID.randomUUID().getMostSignificantBits() & Long.MAX_VALUE;
        this.name = name;
        this.address = address;
        this.capacity = capacity;
    }

    public Location(Long id, String name, String address, Integer capacity){
        this.id = id;
        this.name = name;
        this.address = address;
        this.capacity = capacity;
    }

    public Location(Location location) {
        this.id = location.getId();
        this.name = location.getName();
        this.address = location.getAddress();
        this.capacity = location.getCapacity();
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public int getCapacity() {
        return capacity;
    }

    @Override
    public String toCSV() {
        return id.toString() + "," + name + "," + address + "," + capacity.toString() + '\n';
    }
}
