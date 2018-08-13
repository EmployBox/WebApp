import React from 'react'

import Toggle from '../components/toggle'

export default class extends React.Component {
  constructor (props) {
    super(props)
    this.state = {
      title: '',
      wage: '',
      jobType: 'Looking for Worker',
      description: ''
    }
  }

  render () {
    return (
      <div class='container'>
        <h1 align='center'>Create a job offer or show employers that you're available for a certain job</h1>
        {/* <button onClick={() => console.log(this.state)}>HelloWorld</button> */}
        <div>
          <div class='form-row'>
            <div class='form-group col-md-8'>
              <label>Title</label>
              <input type='text' class='form-control' placeholder='Mechanic Engineer' onChange={event => this.setState({ title: event.target.value })} />
            </div>
            <div class='form-group col-md-2'>
              <label>Wage</label>
              <input type='number' class='form-control' placeholder='€' onChange={event => this.setState({ wage: event.target.value })} />
            </div>
            <div class='form-group col-md-2'>
              <label>Type</label>
              <select class='form-control' value={this.state.jobType} onChange={event => this.setState({ jobType: event.target.value })}>
                <option value='Looking for Worker'>Job Offer</option>
                <option value='Looking for work'>Candidate</option>
              </select>
            </div>
          </div>
          <div class='form-group'>
            <label>Description</label>
            <textarea class='form-control' rows='3' onChange={event => this.setState({ description: event.target.value })} placeholder='My job offer is the best because I offer...' />
          </div>
          <h4>Competences</h4>
          <div class='form-row'>
            <div class='form-group col-md-4'>
              <div class='card'>
                <ul class='list-group list-group-flush'>
                  <li class='list-group-item'>Cras justo odio</li>
                  <li class='list-group-item'>Dapibus ac facilisis in</li>
                  <li class='list-group-item'>Vestibulum at eros</li>
                </ul>
              </div>
            </div>
            <div class='form-group col-md-8'>
              <label>Competence</label>
              <input type='text' class='form-control' />
              <label>Years</label>
              <input type='number' class='form-control' />
              <center>
                <button class='btn btn-success'>Add</button>
              </center>
            </div>
          </div>
          <Toggle text='Location'>
            <div class='form-group'>
              <label>Address</label>
              <input type='text' class='form-control' placeholder='1234 Main St' />
            </div>
            <div class='form-group'>
              <label>Address 2</label>
              <input type='text' class='form-control' placeholder='Apartment, studio, or floor' />
            </div>
            <div class='form-row'>
              <div class='form-group col-md-6'>
                <label>City</label>
                <input type='text' class='form-control' placeholder='Amadora' />
              </div>
              <div class='form-group col-md-3'>
                <label>District</label>
                <input type='text' class='form-control' placeholder='Lisbon' />
              </div>
              <div class='form-group col-md-3'>
                <label>Country</label>
                <input type='text' class='form-control' placeholder='Portugal' />
              </div>
            </div>
          </Toggle>
          <Toggle text='Scheduling'>
            <div class='form-row'>
              <div class='form-group col-md-6'>
                <label>Open</label>
                <input type='datetime-local' class='form-control' onChange={event => this.setState({ description: event.target.value })} />
              </div>
              <div class='form-group col-md-6'>
                <label>Close</label>
                <input type='datetime-local' class='form-control' onChange={event => this.setState({ description: event.target.value })} />
              </div>
            </div>
          </Toggle>
          <center>
            <button type='submit' class='btn btn-success'>Create</button>
          </center>
        </div>
      </div>
    )
  }
}
