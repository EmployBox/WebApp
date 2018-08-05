import React from 'react'
import HttpRequest from '../components/httpRequest'

export default class extends React.Component {
  constructor (props) {
    super(props)
    this.state = {
      inputs: undefined
    }
    this.onSignUp = this.onSignUp.bind(this)
    console.log(props.url)
  }

  onSignUp (inputs) {
    this.setState({inputs: inputs})
  }

  render () {
    return (
      <div>
        <div class='container py-5'>
          <div class='row'>
            <div class='col' />
            <div class='col-xl-7 col-lg-5 col-md-4 col-sm-auto'>
              <h2 class='text-center'>SignUp</h2>
              {this.props.form(this.onSignUp)}
              {this.state.inputs
                ? <HttpRequest
                  method='POST'
                  url={this.props.url}
                  body={this.state.inputs}
                  afterResult={this.props.ToLogin()}
                  onError={err => (
                    <div class='alert alert-danger' role='alert'>
                      {err.message}
                    </div>)}
                />
                : <div /> }
            </div>
            <div class='col' />
          </div>
        </div>
      </div>
    )
  }
}
