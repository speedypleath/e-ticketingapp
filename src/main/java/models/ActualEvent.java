package models;

public final class ActualEvent extends Event
{
    private final Location location;

    public Location getLocation() {
        return new Location(location);
    }

    public ActualEvent(ActualEvent event)
    {
        super(event);
        this.location = new Location(event.location);
    }

    private ActualEvent(Builder builder)
    {
        super(builder);
        this.location = builder.location;
    }
    
    public static class Builder extends Event.Builder<Builder> {
        Location location;
        public Builder location(Location location)
        {
            this.location = location;
            return this;
        }
        @Override
        public ActualEvent build()
        {
            if(organiser != null && name != null && location != null)
                return new ActualEvent(this);
            else throw new IllegalStateException("Invalid data.");
        }
        @Override
        protected Builder self() {
            return this;
        }
    }

    @Override
    public String toCSV() {
        return super.toCSV() + ",live," + location.getId() + '\n';
    }
}
