import React from 'react'
import GenericForm from '../../components/genericForm'
import SignUpComponent from '../../components/signupComponent'

export default ({url, ToLogin}) => (
  <SignUpComponent url={url} ToLogin={ToLogin} form={(cb) =>
    <GenericForm
      inputData={[
        {type: 'text', name: 'name', label: 'Name', id: 'usernameID'},
        {type: 'text', name: 'email', label: 'Email address', id: 'emailID'},
        {type: 'password', name: 'password', label: 'Password', id: 'passID'},
        {type: 'text', name: 'specialization', label: 'Specialization', id: 'specializationID'},
        {type: 'number', name: 'yearFounded', label: 'Year Founded', id: 'yearFoundedID'},
        {type: 'url', name: 'logoUrl', label: 'Logo URL', id: 'logoUrlID'},
        {type: 'url', name: 'webpageUrl', label: 'WebPage URL', id: 'webpageUrlID'},
        {type: 'text', name: 'description', label: 'Description', id: 'descriptionID'}
      ]}
      klass='form-group'
      onSubmitHandler={cb}
    />
  } />
)
