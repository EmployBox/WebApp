import React from 'react'
import GenericForm from '../components/genericForm'

export default class extends React.Component {
  render () {
    return (
      <div>
        <GenericForm
          inputData={[
            {type: 'text', name: 'email', label: 'Email Address', id: 'emailID'},
            {type: 'password', name: 'password', label: 'Password', id: 'passID'}
          ]}
          klass='form-group'
          onSubmitHandler={this.onSubmit} />
      </div>
    )
  }
}
