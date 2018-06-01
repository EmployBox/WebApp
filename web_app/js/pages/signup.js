import React from 'react'
import GenericForm from '../components/genericForm'

export default () => (
  <div>
    <div class='container py-5'>
      <div class='row'>
        <div class='col' />
        <div class='col-xl-7 col-lg-5 col-md-4 col-sm-auto'>
          <h2 class='text-center'>SignUp</h2>
          <GenericForm inputData={[
            {type: 'text', name: 'Name'},
            {type: 'password', name: 'Password'}
          ]} klass='form-row' />
        </div>
        <div class='col' />
      </div>
    </div>
  </div>
)
