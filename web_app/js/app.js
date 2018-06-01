import React from 'react'
import {BrowserRouter, Route, Switch} from 'react-router-dom'
import Navigation from './components/navigation'
import Footer from './components/footer'

import IndexPage from './pages/indexPage'
import SearchPage from './pages/searchPage'

export default (props) => (
  <div>
    <BrowserRouter>
      <div>
        <Navigation />
        <Switch>
          <Route exact path='/' component={IndexPage} />
          <Route exact path='/:type/search' component={SearchPage} />
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
