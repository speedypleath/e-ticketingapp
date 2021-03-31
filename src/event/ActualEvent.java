package event;

import location.Location;

public class ActualEvent extends Event
{
    private Location location;

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public ActualEvent(ActualEvent event)
    {
        super(event);
    }

    private ActualEvent(Builder builder)
    {
        super(builder);
        this.location = builder.location;
    }

    @Override
    ActualEvent copyEvent(Event event) {
        return new ActualEvent((ActualEvent) event);
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
}
