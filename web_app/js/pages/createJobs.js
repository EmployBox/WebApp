import React from 'react'
import {withRouter} from 'react-router-dom'
import URI from 'urijs'

import Toggle from '../components/toggle'
import HttpRequest from '../components/httpRequest'

const competencesListStyle = {
  maxHeight: '144px',
  overflowY: 'scroll'
}

class Competence {
  constructor (competence, year) {
    this.competences = competence
    this.years = year
  }
}

export default withRouter(class extends React.Component {
  constructor (props) {
    super(props)
    this.state = {
      accountId: props.accountId,
      title: '',
      wage: '',
      offerType: 'Looking for Worker',
      description: '',
      address: '',
      address2: '',
      city: '',
      district: '',
      country: '',
      offerBeginDate: '',
      offerEndDate: '',
      competence: '',
      year: '',
      experiences: [] // { competences: String, years: Number }
    }
  }

  render () {
    console.log(URI.decode(this.props.match.params.jobUrl))
    return (
      <div class='container'>
        <br />
        {/* <button onClick={() => console.log(this.state)}>HelloWorld</button> */}
        <div>
          <h4 className='btn-primary bg-dark btn-lg btn-block'>Job Details</h4>
          <div class='form-row'>
            <div class='form-group col-md-8'>
              <label>Title</label>
              <input type='text' class='form-control' placeholder='Mechanic Engineer' value={this.state.title} onChange={event => this.setState({ title: event.target.value })} />
            </div>
            <div class='form-group col-md-2'>
              <label>Wage</label>
              <input type='number' class='form-control' placeholder='€' value={this.state.wage} onChange={event => this.setState({ wage: event.target.value })} />
            </div>
            <div class='form-group col-md-2'>
              <label>Type</label>
              <select class='form-control' value={this.state.offerType} onChange={event => this.setState({ offerType: event.target.value })}>
                <option value='Looking for Worker'>Job Offer</option>
                <option value='Looking for work'>Candidate</option>
              </select>
            </div>
          </div>
          <div class='form-group'>
            <label>Description</label>
            <textarea class='form-control' rows='3' value={this.state.description} onChange={event => this.setState({ description: event.target.value })} placeholder='My job offer is the best because I offer...' />
          </div>
          <h4 className='btn-primary bg-dark btn-lg btn-block'>Competences</h4>
          <div class='form-row'>
            <div class='form-group col-md-4'>
              <div class='card'>
                <ul class='list-group list-group-flush' style={competencesListStyle} >
                  {this.state.experiences.map(value => {
                    return <li class='list-group-item'>{value.competences} - {value.years} years of experience</li>
                  })}
                </ul>
              </div>
            </div>
            <div class='form-group col-md-8'>
              <div class='form-row'>
                <div class='form-group col-md-4'>
                  <label>Competence</label>
                  <input type='text' class='form-control' placeholder='MS Office' value={this.state.competence} onChange={event => this.setState({ competence: event.target.value })} />
                </div>
                <div class='form-group col-md-4'>
                  <label>Years</label>
                  <input type='number' class='form-control' placeholder='2' value={this.state.year} onChange={event => this.setState({ year: event.target.value })} />
                </div>
                <div class='form-group col-md-4'>
                  <label className='text-white'>I'm hidden</label>
                  <button class='btn btn-success form-control' onClick={() => {
                    if (this.state.competence !== '' && this.state.year !== '') {
                      this.setState(prevState => {
                        const newCompetences = prevState.experiences
                        newCompetences.push(new Competence(this.state.competence, this.state.year))
                        return { experiences: newCompetences, addCompetenceError: undefined, competence: '', year: '' }
                      })
                    } else {
                      this.setState({ addCompetenceError: <div class='alert alert-danger' role='alert'>
                        You should fill both input boxes
                      </div> })
                    }
                  }}>Add</button>
                </div>
              </div>
              {this.state.addCompetenceError ? this.state.addCompetenceError : <div />}
            </div>
          </div>
          <Toggle text='Location'>
            <div class='form-group'>
              <label>Address</label>
              <input type='text' class='form-control' placeholder='1234 Main St' value={this.state.address} onChange={event => this.setState({ address: event.target.value })} />
            </div>
            <div class='form-group'>
              <label>Address 2</label>
              <input type='text' class='form-control' placeholder='Apartment, studio, or floor' value={this.state.address2} onChange={event => this.setState({ address2: event.target.value })} />
            </div>
            <div class='form-row'>
              <div class='form-group col-md-6'>
                <label>City</label>
                <input type='text' class='form-control' placeholder='Amadora' value={this.state.city} onChange={event => this.setState({ city: event.target.value })} />
              </div>
              <div class='form-group col-md-3'>
                <label>District</label>
                <input type='text' class='form-control' placeholder='Lisbon' value={this.state.district} onChange={event => this.setState({ district: event.target.value })} />
              </div>
              <div class='form-group col-md-3'>
                <label>Country</label>
                <input type='text' class='form-control' placeholder='Portugal' value={this.state.country} onChange={event => this.setState({ country: event.target.value })} />
              </div>
            </div>
          </Toggle>
          <Toggle text='Scheduling'>
            <div class='form-row'>
              <div class='form-group col-md-6'>
                <label>Open</label>
                <input type='datetime-local' class='form-control' value={this.state.offerBeginDate} onChange={event => this.setState({ offerBeginDate: event.target.value })} />
              </div>
              <div class='form-group col-md-6'>
                <label>Close</label>
                <input type='datetime-local' class='form-control' value={this.state.offerEndDate} onChange={event => this.setState({ offerEndDate: event.target.value })} />
              </div>
            </div>
          </Toggle>
          <center>
            <button type='submit' class='btn btn-success' onClick={() => this.setState(prev => {
              const body = prev
              delete body.competence
              delete body.year
              body.address = body.address + body.address2
              delete body.address2
              return { body: body }
            })}>Create</button>
            {this.state.body
              ? <HttpRequest
                method='POST'
                url={URI.decode(this.props.match.params.jobUrl)}
                authorization={this.props.auth}
                body={this.state.body}
                afterResult={json => {
                  this.props.history.push(`/job/${URI.encode(json._links.self.href)}`)
                }}
                onError={err => (
                  <div class='alert alert-danger' role='alert'>
                    {err.message}
                  </div>
                )}
              />
              : <div />}
          </center>
        </div>
      </div>
    )
  }
})
