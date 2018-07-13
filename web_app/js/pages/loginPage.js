import React from 'react'
import GenericForm from '../components/genericForm'
import HttpRequest from '../components/httpRequest'

export default class extends React.Component {
  constructor (props) {
    super(props)
    this.state = {
      inputs: undefined
    }
    this.onSubmit = this.onSubmit.bind(this)
    console.log(this.props.url)
  }

  onSubmit (inputs) {
    this.setState({inputs: inputs})
  }

  render () {
    return (
      <div>
        <div class='container py-5'>
          <div class='row'>
            <div class='col' />
            <div class='col-xl-7 col-lg-5 col-md-4 col-sm-auto'>
              <h2 class='text-center'>LogIn</h2>
              <GenericForm
                inputData={[
                  {type: 'text', name: 'email', label: 'Email address', id: 'emailID'},
                  {type: 'password', name: 'password', label: 'Password', id: 'passID'}
                ]}
                klass='form-group'
                onSubmitHandler={this.onSubmit}
              />
              {this.state.inputs
                ? <HttpRequest
                  method='POST'
                  url={this.props.url}
                  body={JSON.stringify(this.state.inputs)}
                  afterResult={this.props.ToLogin}
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
