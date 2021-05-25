package models;

import utility.CSV;

import java.util.Objects;
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

    public Integer getCapacity() {
        return capacity;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Location location = (Location) o;
        return Objects.equals(id, location.id) && Objects.equals(name, location.name) && Objects.equals(address, location.address) && Objects.equals(capacity, location.capacity);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, address, capacity);
    }

    @Override
    public String toCSV() {
        return id.toString() + "," + name + "," + address + "," + capacity.toString() + '\n';
    }
}
