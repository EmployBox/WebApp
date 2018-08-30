package isel.ps.employbox.model.output;

import com.fasterxml.jackson.annotation.JsonProperty;
import isel.ps.employbox.controllers.jobs.ScheduleController;

import java.time.Instant;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

public class OutSchedule implements OutputDto {

    private final OutJob _outJob;

    private final OutAccount _outAccount;

    @JsonProperty
    private final long scheduleId;

    @JsonProperty
    private final Instant startDate;

    @JsonProperty
    private final Instant endDate;

    @JsonProperty
    private final Instant startHour;

    @JsonProperty
    private final Instant endHour;

    @JsonProperty
    private final String scheduleType;

    @JsonProperty
    private final long version;

    @JsonProperty
    private final _Embedded _embedded;

    @JsonProperty
    private final _Links _links;


    public OutSchedule(
            long scheduleId,
            OutJob outJob,
            OutAccount outAccount,
            Instant startDate,
            Instant endDate,
            Instant startHour,
            Instant endHour,
            String scheduleType,
            long version)
    {
        this.scheduleId = scheduleId;
        this._outAccount = outAccount;
        this._outJob = outJob;
        this.startDate = startDate;
        this.endDate = endDate;
        this.startHour = startHour;
        this.endHour = endHour;
        this.scheduleType = scheduleType;
        this.version = version;
        this._embedded = new _Embedded();
        this._links = new _Links();
    }

    @Override
    public Object getCollectionItemOutput() {
        return new ScheduleItemOutput(scheduleId,  startDate, endDate, startHour, endHour, scheduleType, version) ;
    }

    class ScheduleItemOutput{
        @JsonProperty
        private final long scheduleId;

        @JsonProperty
        private final Instant startDate;

        @JsonProperty
        private final Instant endDate;

        @JsonProperty
        private final Instant startHour;

        @JsonProperty
        private final Instant endHour;

        @JsonProperty
        private final String scheduleType;

        @JsonProperty
        private final long version;

        @JsonProperty
        private  final _Links _links = new _Links();

        ScheduleItemOutput(long scheduleId,
                           Instant startDate,
                           Instant endDate,
                           Instant startHour,
                           Instant endHour,
                           String scheduleType,
                           long version)
        {
            this.scheduleId = scheduleId;
            this.startDate = startDate;
            this.endDate = endDate;
            this.startHour = startHour;
            this.endHour = endHour;
            this.scheduleType = scheduleType;
            this.version = version;
        }
    }


    private class _Links {
        @JsonProperty
        private _Links.Self self = new _Links.Self();

        private class Self {
            @JsonProperty
            final String href = HOSTNAME + linkTo(methodOn(ScheduleController.class, _outJob.jobId).getSchedule(scheduleId)).withSelfRel().getHref();
        }
    }

    private class _Embedded {

        @JsonProperty
        private final OutJob job = _outJob;

        @JsonProperty
        private final OutAccount outAccount = _outAccount;
    }
}