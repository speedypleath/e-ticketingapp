package event;

public final class VirtualEvent extends Event
{
    private final String inviteLink;

    public String getInviteLink() {
        return inviteLink;
    }

    public VirtualEvent(VirtualEvent event) {
        super(event);
        this.inviteLink = event.inviteLink;
    }

    public VirtualEvent(Builder builder)
    {
        super(builder);
        this.inviteLink = builder.inviteLink;
    }

    @Override
    public String toString() {
        return "VirtualEvent{} " + super.toString() +
                ", inviteLink='" + inviteLink + '\'' +
                '}';
    }

    public static class Builder extends Event.Builder<Builder> {
        String inviteLink;
        public Builder inviteLink(String inviteLink)
        {
            this.inviteLink = inviteLink;
            return this;
        }
        @Override
        public VirtualEvent build()
        {
            if (organiser != null && name != null)
                return new VirtualEvent(this);
            else throw new IllegalStateException("Invalid data.");
        }
        @Override
        protected Builder self() {
            return this;
        }
    }

    @Override
    public String toCSV() {
        return super.toCSV() + ",online," + inviteLink + '\n';
    }
}