import React from 'react'
import listStyle from '../../styles/listStyle'

export default class extends React.Component {
  constructor (props) {
    super(props)
    this.state = {
      repeats: 'NoRepetition',
      date: '',
      startHour: '',
      endHour: '',
      createdSchedules: []
    }
  }

  render () {
    return (
      <div>
        <div class='form-row'>
          <div class='form-group col-md-2'>
            <label>Repeats</label>
            <select class='form-control' value={this.state.repeats} onChange={event => this.setState({ repeats: event.target.value })}>
              <option value='NoRepetition'>...</option>
              <option value='Daily'>Daily</option>
              <option value='Weekly'>Weekly</option>
              <option value='Montly'>Montly</option>
            </select>
          </div>
          <div class='form-group col-md-4'>
            <label>Date</label>
            <input type='Date' class='form-control' value={this.state.date} onChange={event => this.setState({ date: event.target.value })} />
          </div>
          <div class='form-group col-md-2'>
            <label>Start</label>
            <input type='time' class='form-control' value={this.state.startHour} onChange={event => this.setState({ startHour: event.target.value })} />
          </div>
          <div class='form-group col-md-2'>
            <label>End</label>
            <input type='time' class='form-control' value={this.state.endHour} onChange={event => this.setState({ endHour: event.target.value })} />
          </div>
          <div class='form-group col-md-2'>
            <label className='text-white'>I'm hidden</label>
            <button class='btn btn-success form-control' onClick={() => {
              if (this.state.date !== '' && this.state.startHour !== '' && this.state.endHour !== '') {
                const newSchedules = this.props.createdSchedules
                const {date, startHour, endHour, repeats} = this.state

                // TODO verify if startHour is less than EndHour

                newSchedules.push({date: date, startHour: startHour, endHour: endHour, repeats: repeats})

                this.props.onNewSchedule(newSchedules)

                this.setState({ isError: false })
              } else {
                this.setState({ isError: true })
              }
            }}>Add</button>
          </div>
        </div>
        <div class='form-row'>
          <div class='form-group'>
            <div class='card'>
              <ul class='list-group list-group-flush' style={listStyle} >
                {this.props.createdSchedules.map((value, index) => {
                  return <li class='list-group-item'>
                    <div class='row'>
                      <div class='col-10'>
                        {value.date}, {value.startHour} - {value.endHour}{value.repeats !== 'NoRepetition' ? `, ${value.repeats}` : ''}
                      </div>
                      <div class='col-2'>
                        <button class='fas fa-trash'
                          type='button'
                          aria-label='Close'
                          onClick={() => {
                            const newSchedules = this.props.createdSchedules
                            newSchedules.splice(index)
                            this.props.onNewSchedule(newSchedules)
                          }} />
                      </div>
                    </div>
                  </li>
                })}
              </ul>
            </div>
          </div>
          {
            this.state.isError
              ? <div class='form-group col-md-4'>
                <div class='alert alert-danger' role='alert'>
                    You should fill all input boxes
                </div>
              </div>
              : <div />
          }
        </div>
      </div>
    )
  }
}
