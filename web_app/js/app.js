import React from 'react'
import {BrowserRouter, Route, Redirect, Switch} from 'react-router-dom'
import Navigation from './components/navigation'
import Footer from './components/footer'
import SignUp from './pages/signup'

import IndexPage from './pages/indexPage'

export default (props) => (
  <div>
    <BrowserRouter>
      <div>
        <Navigation />
        <Switch>
          <Route exact path='/' component={IndexPage} />
          <Route exact path='/signup' component={SignUp} />
          <Route path='/' render={({history}) =>
            <div>
              <h2>Page not found</h2>
              <button onClick={() => history.push('/')}>home</button>
            </div>
          } />
        </Switch>
        <Footer />
      </div>
    </BrowserRouter>
  </div>
)
