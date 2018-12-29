import React from 'react'
import {withRouter} from 'react-router-dom'
import HttpRequest from '../components/httpRequest'
import GenericForm from '../components/forms/genericForm'
import base64 from 'base-64'
import URI from 'urijs'

export default withRouter(class extends React.Component {
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
    return <div>
      <div class='container py-5'>
        <div class='row'>
          <div class='col' />
          <div class='col-xl-7 col-lg-5 col-md-4 col-sm-auto'>
            <h2 class='text-center'>Verify Account</h2>
            <GenericForm
              inputData={[
                {type: 'text', name: 'email', label: 'Email address', id: 'emailID'},
                {type: 'password', name: 'password', label: 'Password', id: 'passID'}
              ]}
              klass='form-group'
              btnKlass='btn btn-danger btn-lg btn-block'
              onSubmitHandler={this.onSubmit}
            />
            {this.state.auth
              ? <HttpRequest key={new Date().valueOf()}
                method='DELETE'
                url={URI.decode(this.props.match.params.url)}
                authorization={this.state.auth}
                afterResult={json => {
                  this.props.logout()
                  this.props.history.push('/')
                }}
                onError={err => (
                  <div class='alert alert-danger' role='alert'>
                    {err.message}
                  </div>
                )}
              />
              : <div />
            }
          </div>
          <div class='col' />
        </div>
      </div>
    </div>
  }
})
