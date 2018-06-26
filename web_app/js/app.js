import React, {Component} from 'react'
import {BrowserRouter, Route, Switch} from 'react-router-dom'

import Navigation from './components/navigation'
import Footer from './components/footer'

import SignUp from './pages/signup'
import LogIn from './pages/loginPage'
import IndexPage from './pages/indexPage'
import SearchPage from './pages/searchPage'
import URI from 'urijs'
import URITemplate from 'urijs/src/URITemplate'

const urls = {
  about: '/about',
  logIn: new URITemplate('/logIn/{url}').expand({url: 'http://localhost:8080/'}), // TODO endpoint to verify credentials
  signUp: new URITemplate('/signup/{url}').expand({url: 'http://localhost:8080/accounts/users'})
}

const apiURI = 'http://localhost:8080'

export default class extends Component {
  render () {
    return (
      <div>
        <BrowserRouter>
          <div>
            <Navigation about={urls.about} logIn={urls.logIn} signUp={urls.signUp} />
            <Switch>
              <Route exact path='/' component={IndexPage} />
              <Route exact path='/signup/:url' render={({history, match}) => <SignUp ToLogin={() => history.push(urls.logIn)} url={match.params.url} />} />
              <Route exact path='/login/:url' render={({history, match}) => <LogIn />} />
              <Route exact path='/jobs' render={(props) => (
                <SearchPage
                  apiURI={apiURI}
                  uriTemplate={new URITemplate(`/jobs{?query*}`)}
                  type='jobs' />
              )} />
              <Route exact path='/accounts/:type' render={({match}) => (
                <SearchPage
                  apiURI={apiURI}
                  uriTemplate={new URITemplate(`/accounts/${match.params.type}{?query*}`)}
                  type={match.params.type} />
              )} />
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
  }
}
