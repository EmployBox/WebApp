import React, {Component} from 'react'
import {BrowserRouter, Route, Switch} from 'react-router-dom'

import Navigation from './components/navigation'
import Footer from './components/footer'
import PrivateRouter from './components/privateRoute'
import Auther from './components/auther'
import JobFilters from './components/jobFilters'
import JobTable from './components/jobTable'
import CompanyFilters from './components/companyFilters'
import CompanyTable from './components/companyTable'
import UserFilters from './components/userFilters'
import UserTable from './components/userTable'

import SignUp from './pages/signup'
import LogIn from './pages/loginPage'
import IndexPage from './pages/indexPage'
import SearchPage from './pages/searchPage'
import SignUpUser from './pages/signupUser'
import SignUpCompany from './pages/signupCompany'
import Profile from './pages/profile'

import URI from 'urijs'
import URITemplate from 'urijs/src/URITemplate'
import HttpRequest from './components/httpRequest'
import { Option, Render } from './searchFormOptions'

const apiURI = 'http://localhost:8080'

const auther = new Auther()
const PrivateRoute = PrivateRouter(auther)

const signUpUserTempl = new URITemplate('/signup/user/{url}')
const signUpCompanyTempl = new URITemplate('/signup/company/{url}')
const logInTempl = new URITemplate('/logIn/{url}')
const signUpTempl = new URITemplate('/signup/{urlUser}/{urlCompany}')
const accountTempl = new URITemplate('/account/{url}')
const searchTempl = new URITemplate('/search/{url}')
const companyTempl = new URITemplate('/company/{url}')
const jobTempl = new URITemplate('/job/{url}')

function getLink (link, hal) {
  return hal[link]['_links'].self.href
}

function loginExpand (json) {
  return logInTempl.expand({url: getLink('login', json)})
}

const UserTable1 = ({...rest}) => <UserTable {...rest} accountTempl={accountTempl} />
const CompanyTable1 = ({...rest}) => <CompanyTable {...rest} companyTempl={companyTempl} />
const JobTable1 = ({...rest}) => <JobTable {...rest} jobTempl={jobTempl} accountTempl={accountTempl} companyTempl={companyTempl} />

function getOptions (json) {
  return {
    jobs: new Option('Jobs', 'Job\'s title', 'title', getLink('jobs', json), new Render(JobFilters, JobTable1)),
    companies: new Option('Companies', 'Company\'s name', 'name', getLink('companies', json), new Render(CompanyFilters, CompanyTable1)),
    users: new Option('Users', 'User\'s name', 'name', getLink('users', json), new Render(UserFilters, UserTable1))
  }
}

export default class extends Component {
  constructor (props) {
    super(props)
    this.state = {
      authenticated: false
    }
  }

  render () {
    return (
      <HttpRequest url={apiURI}
        afterResult={json => auther.setLoginUrl(loginExpand(json))}
        onResult={json => (
          <div>
            <BrowserRouter>
              <div>
                <Navigation navItems={
                  this.state.authenticated
                    ? [
                      {name: 'Profile', link: accountTempl.expand({url: this.state.self})},
                      {name: 'About', link: '/about'},
                      {name: 'Log out',
                        class: 'btn btn-outline-primary',
                        click: () => this.setState(oldstate => {
                          oldstate.authenticated = false
                          oldstate.self = undefined
                          auther.unAuthenticate()
                          return oldstate
                        }) }
                    ]
                    : [
                      {name: 'About', link: '/about'},
                      {name: 'Log in', link: loginExpand(json)},
                      {name: 'Sign up',
                        link: signUpTempl.expand({
                          urlUser: getLink('users', json),
                          urlCompany: getLink('companies', json)}
                        ),
                        class: 'btn btn-outline-primary'}
                    ]} />
                <Switch>
                  <Route exact path='/' render={(props) =>
                    <IndexPage options={getOptions(json)} searchTempl={searchTempl} />}
                  />
                  <Route exact path='/signup/user/:url' render={({history, match}) =>
                    <SignUpUser url={URI.decode(match.params.url)} ToLogin={() => history.push(loginExpand(json))} />} />
                  <Route exact path='/signup/company/:url' render={({history, match}) =>
                    <SignUpCompany url={URI.decode(match.params.url)} ToLogin={() => history.push(loginExpand(json))} />} />
                  <Route exact path='/signup/:urlUser/:urlCompany' render={({history, match}) =>
                    <SignUp signUpUser={() => history.push(signUpUserTempl.expand({ url: match.params.urlUser }))}
                      signUpCompany={() => history.push(signUpCompanyTempl.expand({ url: match.params.urlCompany }))} />}
                  />
                  <Route exact path='/login/:url' render={({history, match}) =>
                    <LogIn url={URI.decode(match.params.url)}
                      ToLogin={(json, auth) => {
                        this.setState(oldstate => {
                          oldstate.authenticated = true
                          oldstate.self = json['_links'].self.href
                          auther.authenticate(auth)
                          return oldstate
                        })
                        history.push(URI.parseQuery(history.location.search).redirect || '/')
                      }} />
                  } />
                  <Route exact path='/search/:url' render={({match}) => (
                    <SearchPage
                      uriTemplate={new URITemplate(URI.decode(match.params.url).split('?')[0] + '{?query*}')}
                      options={getOptions(json)}
                      match={match}
                      searchTempl={searchTempl}
                    />
                  )} />
                  <PrivateRoute exact path='/account/:url' component={Profile} />
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
          </div>)}
      />)
  }
}
