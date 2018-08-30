import React from 'react'
import GenericForm from '../../components/forms/genericForm'
import SignUpComponent from '../../components/signupComponent'

export default ({url, ToLogin}) => (
  <SignUpComponent url={url} ToLogin={ToLogin} form={(cb) =>
    <GenericForm
      inputData={[
        {type: 'text', name: 'name', label: 'Name', id: 'usernameID'},
        {type: 'text', name: 'email', label: 'Email address', id: 'emailID'},
        {type: 'password', name: 'password', label: 'Password', id: 'passID'},
        {type: 'url', name: 'photo_url', label: 'Photo URL', id: 'photoUrlID'},
        {type: 'text', name: 'summary', label: 'Summary', id: 'summaryID'}
      ]}
      klass='form-group'
      onSubmitHandler={cb}
    />} />
)
