import React from 'react'
import {BrowserRouter, Route, Switch} from 'react-router-dom'

import Navigation from './components/navigation'
import Footer from './components/footer'

import SignUp from './pages/signup'
import IndexPage from './pages/indexPage'
import SearchPage from './pages/searchPage'

const apiURI = 'http://localhost:8080'

export default (props) => (
  <div>
    <BrowserRouter>
      <div>
        <Navigation />
        <Switch>
          <Route exact path='/' component={IndexPage} />
          <Route exact path='/signup' component={SignUp} />
          <Route exact path='/:type/search' render={(props) => <SearchPage url={apiURI} />} />
          <Route path='/' render={({history}) =>
            <center class='py-5'>
              <h2>Page not found</h2>
              <button onClick={() => history.push('/')}>home</button>
            </center>
          } />
        </Switch>
        <Footer />
      </div>
    </BrowserRouter>
  </div>
)
