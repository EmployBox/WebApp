package isel.ps.employbox.model.output;

import com.fasterxml.jackson.annotation.JsonProperty;
import isel.ps.employbox.controllers.jobs.ScheduleController;

import java.time.Instant;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

public class OutSchedule implements OutputDto<OutSchedule.ScheduleItemOutput> {

    private final OutJob _outJob;

    @JsonProperty
    private final long scheduleId;

    @JsonProperty
    private final Instant date;

    @JsonProperty
    private final Instant startHour;

    @JsonProperty
    private final Instant endHour;

    @JsonProperty
    private final String repeats;

    @JsonProperty
    private final long version;

    @JsonProperty
    private final _Embedded _embedded;

    @JsonProperty
    private final _Links _links;


    public OutSchedule(
            long scheduleId,
            OutJob outJob,
            Instant date,
            Instant startHour,
            Instant endHour,
            String repeats,
            long version)
    {
        this.scheduleId = scheduleId;
        this._outJob = outJob;
        this.date = date;
        this.startHour = startHour;
        this.endHour = endHour;
        this.version = version;
        this.repeats = repeats;
        this._embedded = new _Embedded();
        this._links = new _Links();
    }

    @Override
    public ScheduleItemOutput getCollectionItemOutput() {
        return new ScheduleItemOutput(scheduleId, date, startHour, endHour, repeats, version) ;
    }

    class ScheduleItemOutput{
        @JsonProperty
        private final long scheduleId;

        @JsonProperty
        private final Instant date;

        @JsonProperty
        private final Instant startHour;

        @JsonProperty
        private final Instant endHour;

        @JsonProperty
        private final String repeats;

        @JsonProperty
        private final long version;

        @JsonProperty
        private  final _Links _links = new _Links();

        ScheduleItemOutput(long scheduleId,
                           Instant date,
                           Instant startHour,
                           Instant endHour,
                           String repeats,
                           long version)
        {
            this.scheduleId = scheduleId;
            this.date = date;
            this.startHour = startHour;
            this.endHour = endHour;
            this.repeats = repeats;
            this.version = version;
        }
    }


    private class _Links {
        @JsonProperty
        private _Links.Self self = new _Links.Self();

        private class Self {
            @JsonProperty
            final String href = HOSTNAME + linkTo(methodOn(ScheduleController.class, _outJob.getJobId()).getSchedule(scheduleId)).withSelfRel().getHref();
        }
    }

    private class _Embedded {

        @JsonProperty
        private final OutJob job = _outJob;
    }
}