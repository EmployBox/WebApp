import React, {Component} from 'react'
import {BrowserRouter, Route, Switch, Link} from 'react-router-dom'

import Navigation from './components/navigation'
import Footer from './components/footer'
import PrivateRouter from './components/privateRoute'
import Auther from './components/auther'
import JobFilters from './components/filters/jobFilters'
import JobTable from './components/tables/jobTable'
import CompanyFilters from './components/filters/companyFilters'
import CompanyTable from './components/tables/companyTable'
import UserFilters from './components/filters/userFilters'
import UserTable from './components/tables/userTable'

import CreateJobs from './pages/createJobs'
import SignUp from './pages/signup/signup'
import LogIn from './pages/loginPage'
import IndexPage from './pages/indexPage'
import SearchPage from './pages/searchPage'
import SignUpUser from './pages/signup/signupUser'
import SignUpCompany from './pages/signup/signupCompany'
import User from './pages/profiles/user'
import Company from './pages/profiles/company'
import Job from './pages/jobPage'
import Curricula from './pages/curriculaPage'
import RatingForm from './pages/ratingForm'
import DeleteAccount from './pages/deleteAccountPage'

import URI from 'urijs'
import URITemplate from 'urijs/src/URITemplate'
import HttpRequest from './components/httpRequest'
import { Option, Render } from './searchFormOptions'
import CreateCurricula from './pages/createCurricula'

const apiURI = 'http://localhost:8080/'
// const apiURI = 'http://35.230.153.165/api/'

const auther = new Auther()
const PrivateRoute = PrivateRouter(auther)

const createJobTempl = new URITemplate('/create/jobs/{url}')
const createCurriculaTempl = new URITemplate('/create/curricula/{url}')
const signUpUserTempl = new URITemplate('/signup/user/{url}')
const signUpCompanyTempl = new URITemplate('/signup/company/{url}')
const logInTempl = new URITemplate('/logIn/{url}')
const signUpTempl = new URITemplate('/signup/{urlUser}/{urlCompany}')
const accountTempl = new URITemplate('/account/{url}')
const searchTempl = new URITemplate('/search/{url}')
const companyTempl = new URITemplate('/company/{url}')
const jobTempl = new URITemplate('/job/{url}')
const curriculaTempl = new URITemplate('/curricula/{url}')
const deleteAccountTempl = new URITemplate('/delete/{url}')

function getLink (link, hal) {
  return hal[link]['_links'].self.href
}

function loginExpand (json) {
  return logInTempl.expand({url: getLink('login', json)})
}

export default class extends Component {
  constructor (props) {
    super(props)
    this.state = {
      authenticated: auther.auth || false
    }
    this.logout = this.logout.bind(this)
  }

  getOptions (json) {
    const UserTable1 = ({...rest}) => <UserTable {...rest} accountTempl={accountTempl} />
    const CompanyTable1 = ({...rest}) => <CompanyTable {...rest} companyTempl={companyTempl} />
    const JobTable1 = ({...rest}) => <JobTable {...rest} jobTempl={jobTempl} accountTempl={accountTempl} companyTempl={companyTempl} />

    return {
      jobs: new Option('Jobs', 'Job\'s title', 'title', getLink('jobs', json), new Render(JobFilters, JobTable1)),
      companies: new Option('Companies', 'Company\'s name', 'name', getLink('companies', json), new Render(CompanyFilters, CompanyTable1)),
      users: new Option('Users', 'User\'s name', 'name', getLink('users', json), new Render(UserFilters, UserTable1))
    }
  }

  logout () {
    this.setState(oldstate => {
      oldstate.authenticated = false
      auther.unAuthenticate()
      return oldstate
    })
  }

  render () {
    return (
      <HttpRequest url={apiURI}
        onResult={json => {
          auther.setLoginUrl(loginExpand(json))
          return (
            <div>
              <BrowserRouter>
                <div>
                  <Navigation>
                    <ul class='navbar-nav ml-auto'>
                      <li class='nav-item'>
                        <Link class='nav-link' to='/about'>About</Link>
                      </li>
                      <li class='nav-item'>
                        <Link class='nav-link' to={createJobTempl.expand({url: getLink('jobs', json)})}>Post new Job</Link>
                      </li>
                      {this.state.authenticated &&
                      <li class='nav-item dropdown'>
                        <a class='nav-link dropdown-toggle' role='button' data-toggle='dropdown' aria-haspopup='true' aria-expanded='false'>
                        Account
                        </a>
                        <div class='dropdown-menu dropdown-menu-right' aria-labelledby='navbarDropdown'>
                          <Link class='dropdown-item' to={(auther.accountType === 'USR' ? accountTempl : companyTempl).expand({ url: auther.self })}>Profile</Link>
                          <Link class='dropdown-item text-danger' to={deleteAccountTempl.expand({url: auther.self})}>Delete</Link>
                          <div class='dropdown-divider' />
                          <button class='dropdown-item'
                            onClick={this.logout}>Log out</button>
                        </div>
                      </li>
                      }
                      {!this.state.authenticated &&
                      <li class='nav-item'>
                        <Link class='nav-link' to={loginExpand(json)}>Log in</Link>
                      </li>}
                      {!this.state.authenticated &&
                      <li class='nav-item'>
                        <Link class='btn btn-outline-primary'
                          to={signUpTempl.expand({
                            urlUser: getLink('users', json),
                            urlCompany: getLink('companies', json)
                          })}>Sign up</Link>
                      </li>}
                    </ul>
                  </Navigation>
                  <Switch>
                    <Route exact path='/' render={(props) =>
                      <IndexPage options={this.getOptions(json)} searchTempl={searchTempl} />}
                    />
                    <Route exact path='/signup/user/:url' render={({ history, match }) =>
                      <SignUpUser url={URI.decode(match.params.url)} ToLogin={() => history.push(loginExpand(json))} />}
                    />
                    <Route exact path='/signup/company/:url' render={({ history, match }) =>
                      <SignUpCompany url={URI.decode(match.params.url)} ToLogin={() => history.push(loginExpand(json))} />}
                    />
                    <Route exact path='/signup/:urlUser/:urlCompany' render={({ history, match }) =>
                      <SignUp signUpUser={() => history.push(signUpUserTempl.expand({ url: match.params.urlUser }))}
                        signUpCompany={() => history.push(signUpCompanyTempl.expand({ url: match.params.urlCompany }))} />}
                    />
                    <Route exact path='/login/:url' render={({ history, match }) =>
                      <LogIn url={URI.decode(match.params.url)}
                        ToLogin={(json, auth) => {
                          this.setState(oldstate => {
                            oldstate.authenticated = true
                            oldstate.self = json['_links'].self.href
                            auther.authenticate(auth, json)
                            return oldstate
                          })
                          history.push(URI.parseQuery(history.location.search).redirect || '/')
                        }} />
                    } />
                    <Route exact path='/search/:url' render={({ match }) => (
                      <SearchPage
                        uriTemplate={new URITemplate(URI.decode(match.params.url).split('?')[0] + '{?query*}')}
                        options={this.getOptions(json)}
                        match={match}
                        searchTempl={searchTempl}
                        auther={auther}
                      />
                    )} />
                    <PrivateRoute exact path='/create/curricula/:url' component={(props) =>
                      <CreateCurricula curriculaTempl={curriculaTempl} {...props} />}
                    />
                    <PrivateRoute path='/rate/:url' component={RatingForm} />
                    <PrivateRoute path='/account/:url' component={(props) =>
                      <User createCurriculaTempl={createCurriculaTempl} {...props} />}
                    />
                    <PrivateRoute path='/company/:url' component={Company} />
                    <PrivateRoute exact path='/create/jobs/:jobUrl' component={CreateJobs} />
                    <PrivateRoute exact path='/job/:url' component={Job} />
                    <PrivateRoute path='/curricula/:url' component={Curricula} />
                    <PrivateRoute exact path='/delete/:url' component={props => <DeleteAccount logout={this.logout} {...props} />} />
                    <Route path='/' render={({ history }) =>
                      <center class='py-5 alert alert-danger' role='alert'>
                        <h2>Error 404.</h2>
                        <p>The requested URL {history.location.pathname} was not found on web application.</p>
                      </center>
                    } />
                  </Switch>
                  <Footer />
                </div>
              </BrowserRouter>
            </div>
          )
        }}
      />
    )
  }
}
