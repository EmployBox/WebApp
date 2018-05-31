import React from 'react'
import {BrowserRouter, Route, Redirect, Switch} from 'react-router-dom'

import IndexPage from './pages/indexPage'

export default (props) => (
  <div>
    <BrowserRouter>
      <Switch>
        <Route exact path='/' component={IndexPage} />
      </Switch>
    </BrowserRouter>
  </div>
)
