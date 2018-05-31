import React from 'react'
import {BrowserRouter, Route, Redirect, Switch} from 'react-router-dom'

import IndexPage from './pages/indexPage'

export default (props) => (
  <div>
    <BrowserRouter>
      <Switch>
        <Route exact path='/' component={IndexPage} />
        <Route path='/' render={({history}) =>
          <div>
            <h2>Page not found</h2>
            <button onClick={() => history.push('/')}>home</button>
          </div>
        } />
      </Switch>
    </BrowserRouter>
  </div>
)
