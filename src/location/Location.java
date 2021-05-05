package location;

import utility.CSV;

import java.util.UUID;

public class Location implements CSV
{
    private final Long id;
    private String name;
    private String address;
    private Integer capacity;

    public Location(){
        this.id = UUID.randomUUID().getMostSignificantBits() & Long.MAX_VALUE;
    }

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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    @Override
    public String toCSV() {
        return id.toString() + "," + name + "," + address + "," + capacity.toString() + '\n';
    }
}
