import React, {Component} from 'react'
import {BrowserRouter, Route, Switch, Redirect} from 'react-router-dom'

import Navigation from './components/navigation'
import Footer from './components/footer'

import SignUp from './pages/signup'
import LogIn from './pages/loginPage'
import IndexPage from './pages/indexPage'
import SearchPage from './pages/searchPage'
import SignUpUser from './pages/signupUser'
import SignUpCompany from './pages/signupCompany'
import Profile from './pages/profile'
import PrivateRouter from './components/privateRoute'
import Auther from './components/auther'

import URI from 'urijs'
import URITemplate from 'urijs/src/URITemplate'
import HttpRequest from './components/httpRequest'

const apiURI = 'http://localhost:8080'

const urls = {
  about: '/about',
  logIn: new URITemplate('/logIn/{url}').expand({url: apiURI + '/accounts/self'}), // TODO endpoint to verify credentials
  signUp: new URITemplate('/signup/{urlUser}/{urlCompany}').expand({urlUser: apiURI + '/accounts/users', urlCompany: apiURI + '/accounts/companies'}),
  profile: new URITemplate('/profile/{url}').expand({url: apiURI + '/accounts/self'})
}

const auther = new Auther(urls.logIn)
const PrivateRoute = PrivateRouter(auther)

const signUpUserTempl = new URITemplate('/signup/user/{url}')
const signUpCompanyTempl = new URITemplate('/signup/company/{url}')

export default class extends Component {
  constructor (props) {
    super(props)
    this.state = {
      authenticated: false,
      home: undefined
    }
  }

  render () {
    return (
      <div>
        <BrowserRouter>
          <div>
            {this.state.home
              ? <Navigation navItems={
                this.state.authenticated
                  ? [
                    {name: 'Profile', link: urls.profile},
                    {name: 'About', link: urls.about},
                    {name: 'Log out', click: () => { auther.unAuthenticate(); this.setState({authenticated: false}) }, class: 'btn btn-outline-primary'}
                  ]
                  : [
                    {name: 'About', link: urls.about},
                    {name: 'Log in', link: urls.logIn},
                    {name: 'Sign up', link: urls.signUp, class: 'btn btn-outline-primary'}
                  ]} />
              : <HttpRequest url={apiURI}
                afterResult={json => this.setState()}
              />}
            <Switch>
              <Route exact path='/' component={IndexPage} />
              <Route exact path='/signup/user/:url' render={({history, match}) => <SignUpUser url={URI.decode(match.params.url)} />} />
              <Route exact path='/signup/company/:url' render={({history, match}) => <SignUpCompany url={URI.decode(match.params.url)} />} />
              <Route exact path='/signup/:urlUser/:urlCompany' render={({history, match}) =>
                <SignUp signUpUser={() => history.push(signUpUserTempl.expand({ url: match.params.urlUser }))}
                  signUpCompany={() => history.push(signUpCompanyTempl.expand({ url: match.params.urlCompany }))} />}
              />
              <Route exact path='/login/:url' render={({history, match}) =>
                <LogIn url={URI.decode(match.params.url)}
                  ToLogin={(json, auth) => {
                    auther.authenticate(auth)
                    this.setState({authenticated: true})
                    history.push(URI.parseQuery(history.location.search).redirect || '/')
                  }} />
              } />
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
              <PrivateRoute exact path='/profile/:url' component={Profile} />
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
