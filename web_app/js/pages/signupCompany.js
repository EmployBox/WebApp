import React from 'react'
import GenericForm from '../components/genericForm'
import SignUpComponent from '../components/signupComponent'

export default ({url}) => (
  <SignUpComponent url={url} form={(cb) =>
    <GenericForm
      inputData={[
        {type: 'text', name: 'name', label: 'Name', id: 'usernameID'},
        {type: 'text', name: 'email', label: 'Email address', id: 'emailID'},
        {type: 'password', name: 'password', label: 'Password', id: 'passID'}
      ]}
      klass='form-group'
      onSubmitHandler={cb}
    />
  } />
)
