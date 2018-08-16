import React from 'react'

export default ({signUpUser, signUpCompany}) => (
  <div>
    <h2 class='text-center'>SignUp</h2>
    <div class='container'>
      <div class='row align-items-center'>
        <div class='col text-center'>
          <h3 class='text-center text-white border bg-dark'>User</h3>
        </div>
        <div class='col text-center'>
          <h3 class='text-center text-white border bg-dark'>Company</h3>
        </div>
      </div>
      <div class='row align-items-center'>
        <div class='col text-center'>
          <button class='btn btn-light' onClick={signUpUser}>
            <img src='/user.png' width='240' height='240' />
          </button>
        </div>
        <div class='col text-center'>
          <button class='btn btn-light' onClick={signUpCompany}>
            <img src='/company.png' width='240' height='240' />
          </button>
        </div>
      </div>
    </div>
  </div>
)
