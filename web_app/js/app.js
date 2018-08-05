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

const auther = new Auther()
const PrivateRoute = PrivateRouter(auther)

const signUpUserTempl = new URITemplate('/signup/user/{url}')
const signUpCompanyTempl = new URITemplate('/signup/company/{url}')
const logInTempl = new URITemplate('/logIn/{url}')
const signUpTempl = new URITemplate('/signup/{urlUser}/{urlCompany}')
const profileTempl = new URITemplate('/profile/{url}')

function getLink (link, hal) {
  return hal[link]['_links'].self.href
}

export default class extends Component {
  constructor (props) {
    super(props)
    this.state = {
      authenticated: false,
      home: undefined
    }
    this.loginExpand = this.loginExpand.bind(this)
  }

  loginExpand () {
    const url = logInTempl.expand({url: getLink('login', this.state.home)})
    console.log(url)
    return url
  }

  render () {
    return (
      this.state.home
        ? <div>
          <BrowserRouter>
            <div>
              <Navigation navItems={
                this.state.authenticated
                  ? [
                    {name: 'Profile', link: profileTempl.expand({url: getLink('login', this.state.home)})},
                    {name: 'About', link: '/about'},
                    {name: 'Log out',
                      class: 'btn btn-outline-primary',
                      click: () => this.setState(oldstate => {
                        oldstate.authenticated = false
                        auther.unAuthenticate()
                        return oldstate
                      }) }
                  ]
                  : [
                    {name: 'About', link: '/about'},
                    {name: 'Log in', link: this.loginExpand()},
                    {name: 'Sign up',
                      link: signUpTempl.expand({
                        urlUser: getLink('users', this.state.home),
                        urlCompany: getLink('companies', this.state.home)}
                      ),
                      class: 'btn btn-outline-primary'}
                  ]} />
              <Switch>
                <Route exact path='/' component={IndexPage} />
                <Route exact path='/signup/user/:url' render={({history, match}) => <SignUpUser url={URI.decode(match.params.url)} ToLogin={() => history.push(this.loginExpand())} />} />
                <Route exact path='/signup/company/:url' render={({history, match}) => <SignUpCompany url={URI.decode(match.params.url)} ToLogin={() => history.push(this.loginExpand())} />} />
                <Route exact path='/signup/:urlUser/:urlCompany' render={({history, match}) =>
                  <SignUp signUpUser={() => history.push(signUpUserTempl.expand({ url: match.params.urlUser }))}
                    signUpCompany={() => history.push(signUpCompanyTempl.expand({ url: match.params.urlCompany }))} />}
                />
                <Route exact path='/login/:url' render={({history, match}) =>
                  <LogIn url={URI.decode(match.params.url)}
                    ToLogin={(json, auth) => {
                      this.setState(oldstate => {
                        oldstate.authenticated = true
                        auther.authenticate(auth)
                        return oldstate
                      })
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
        : <HttpRequest url={apiURI}
          afterResult={json => this.setState(oldstate => {
            oldstate.home = json
            auther.setLoginUrl(logInTempl.expand({url: json.login['_links'].self.href}))
            return oldstate
          })}
        />
    )
  }
}
