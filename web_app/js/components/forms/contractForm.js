import React from 'react'

import CustomSchedulingForm from './customSchedulingForm'

export default ({type, schedules, onTypeChange, onDatesChange}) => {
  return (
    <div class='form-row'>
      <div class='form-group col-md-2'>
        <label>Contract</label>
        <select class='form-control' value={type} onChange={event => onTypeChange(event.target.value)}>
          <option value='Full-time'>Full-time</option>
          <option value='Part-time'>Part-time</option>
          <option value='Freelance'>Freelance</option>
          <option value='Custom'>Custom</option>
        </select>
      </div>
      <div class='form-group col-md-10'>
        {type === 'Custom'
          ? <CustomSchedulingForm
            onNewSchedule={onDatesChange}
            createdSchedules={schedules} />
          : <div>
            <div class='form-row'>
              <div class='form-group col-md-4'>
                <label>Start (Optional)</label>
                <input type='time' class='form-control' value={schedules[0] ? schedules[0].startHour : ''} onChange={event => {
                  if (schedules[0]) {
                    schedules[0].startHour = event.target.value
                  } else {
                    schedules[0] = {startHour: event.target.value}
                  }
                  onDatesChange(schedules)
                }} />
              </div>
              <div class='form-group col-md-4'>
                <label>End (Optional)</label>
                <input type='time' class='form-control' value={schedules[0] ? schedules[0].endHour : ''} onChange={event => {
                  if (schedules[0]) {
                    schedules[0].endHour = event.target.value
                  } else {
                    schedules[0] = {endHour: event.target.value}
                  }
                  onDatesChange(schedules)
                }} />
              </div>
              <div class='form-group col-md-4'>
                <label className='text-white'>I'm hidden</label>
                <button type='time' class='btn btn-danger form-control' onClick={() => onDatesChange([])}>Clear</button>
              </div>
            </div>
          </div>}
      </div>
    </div>
  )
}
