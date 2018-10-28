import React from 'react'
import GenericForm from '../components/forms/genericForm'
import HttpRequest from '../components/httpRequest'
import base64 from 'base-64'

export default class extends React.Component {
  constructor (props) {
    super(props)
    this.state = {
      auth: undefined
    }
    this.onSubmit = this.onSubmit.bind(this)
  }

  onSubmit (inputs) {
    this.setState({auth: 'Basic ' + base64.encode(`${inputs.email}:${inputs.password}`)})
  }

  render () {
    return (
      <div class='container py-5'>
        <div class='row justify-content-center'>
          <div class='col-auto'>
            <h2 class='text-center'>LogIn</h2>
            <GenericForm
              inputData={[
                {type: 'text', name: 'email', label: 'Email address', id: 'emailID'},
                {type: 'password', name: 'password', label: 'Password', id: 'passID'}
              ]}
              klass='form-group'
              onSubmitHandler={this.onSubmit}
            />
            {this.state.auth &&
            <HttpRequest key={new Date().valueOf()}
              method='GET'
              url={this.props.url}
              authorization={this.state.auth}
              afterResult={json => {
                this.props.ToLogin(json, this.state.auth)
              }}
              onError={err => {
                console.log(err)
                return (
                  <div class='alert alert-danger' role='alert'>
                    <p>Wrong email and password combination.</p>
                  </div>
                )
              }}
            />}
          </div>
        </div>
      </div>
    )
  }
}
