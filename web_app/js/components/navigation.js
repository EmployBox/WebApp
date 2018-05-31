import React from 'react'
import { Link } from 'react-router-dom'

export default (props) => (
  <nav class='navbar navbar-expand-lg navbar-dark bg-dark fixed-top'>
    <div class='container'>
      <Link class='navbar-brand' to='/'>EmployBox</Link>
      <button class='navbar-toggler' type='button' data-toggle='collapse' data-target='#navbarResponsive' aria-controls='navbarResponsive' aria-expanded='false'
        aria-label='Toggle navigation'>
        <span class='navbar-toggler-icon' />
      </button>
      <div class='collapse navbar-collapse' id='navbarResponsive'>
        <ul class='navbar-nav ml-auto'>
          <li class='nav-item'>
            <Link class='nav-link' to='/about'>About</Link>
          </li>
          <li class='nav-item'>
            <Link class='nav-link' to='/logIn'>Log in</Link>
          </li>
          <li class='nav-item'>
            <Link class='btn btn-outline-primary' to='/signUp'>Sign up</Link>
          </li>
        </ul>
      </div>
    </div>
  </nav>
)
