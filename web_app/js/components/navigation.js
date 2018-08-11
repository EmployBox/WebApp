import React from 'react'
import { Link, withRouter } from 'react-router-dom'

export default withRouter(({navItems}) => (
  <nav class='navbar navbar-expand-lg navbar-dark bg-dark navbar-fixed-top'>
    <div class='container-fluid'>
      <Link class='navbar-brand' to='/'>EmployBox</Link>
      <button class='navbar-toggler' type='button' data-toggle='collapse' data-target='#navbarResponsive' aria-controls='navbarResponsive' aria-expanded='false'
        aria-label='Toggle navigation'>
        <span class='navbar-toggler-icon' />
      </button>
      <div class='collapse navbar-collapse' id='navbarResponsive'>
        <ul class='navbar-nav ml-auto'>
          {navItems.map(item => (
            <li key={item.name}>
              {item.link
                ? <Link class={item.class || 'nav-link'} to={item.link}>{item.name}</Link>
                : <button class={item.class || 'nav-link'} onClick={item.click}>{item.name}</button>
              }
            </li>
          ))}
        </ul>
      </div>
    </div>
  </nav>
))
