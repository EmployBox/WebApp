import React from 'react'
import {withRouter} from 'react-router-dom'
import fetch from 'isomorphic-fetch'
import URI from 'urijs'
import URITemplate from 'urijs/src/URITemplate'
import FollowersTable from '../tables/followersTable'
import JobsTable from '../tables/offeredJobsTable'
import ApplicationsTable from '../tables/applicationsTable'
import CurriculasTable from '../tables/curriculasTable'
import CommentBox from '../../components/CommentBox'
import TabRoute, {TabConfig} from '../../components/tabRoute'
import FollowButton from '../../components/buttons/followButton'
import {parseResponseToJson} from '../../utils/httpResponseHelper'

const offeredJobsTempl = new URITemplate('/account/{userUrl}/offeredJobs/{offeredJobsUrl}')
const curriculasTempl = new URITemplate('/account/{userUrl}/curriculas/{curriculaUrl}')
const applicationsTempl = new URITemplate('/account/{userUrl}/applications/{applicationUrl}')
const followersTempl = new URITemplate('/account/{userUrl}/followers/{followersURL}')
const followingTempl = new URITemplate('/account/{userUrl}/following/{followingUrl}')
const ratingFormTempl = new URITemplate('/rate/{url}')

export default withRouter(class extends React.Component {
  constructor (props) {
    super(props)

    this.state = {
      user: {},
      isLoading: true,
      error: null
    }
  }

  componentDidMount () {
    const {auth, match} = this.props

    fetch(URI.decode(match.params.url), { method: 'GET', headers: { authorization: auth } })
      .then(parseResponseToJson)
      .then(async user => this.setState({ user, isLoading: false }))
      .catch(error => this.setState({ error, isLoading: false }))
  }

  rateOnClick () {
    const { match, history } = this.props
    const { user } = this.state

    const url = user._links.ratings.href.split('?')[0]
    const newURI = ratingFormTempl.expand({ url: url }) + `?type=user&from=${URI.encode(match.url)}&accountIdDest=${user.accountId}`

    history.push(newURI)
  }

  render () {
    const { auth, history, accountId, createCurriculaTempl } = this.props
    const { user, isLoading, error } = this.state

    if (error) return <p>{error.message}</p>

    if (isLoading) return <p>Loading ...</p>

    const imgStyle = {
      width: 200,
      height: 200,
      border: 1,
      borderRadius: '50%'
    }

    const buttonsStyle = {
      marginRight: 5,
      marginLeft: 5
    }

    return (
      <div class='container pt-20'>
        <div class='row'>
          <div class='col-4'>
            <center>
              <img style={imgStyle} src={user.photo_url || 'https://www.cukashmir.ac.in/facultyimages/2316218245609profile-default-male.png'} />
            </center>
          </div>
          <div class='col-8'>
            <h1>
              {user.name}
              <span class='float-right'>
                {user.rating.toFixed(1)}/10
              </span>
            </h1>
            <h2>Summary</h2>
            <p>{user.summary || 'No summary available'}</p>
            <div class='row'>
              {user.accountId !== accountId &&
                <FollowButton className='btn btn-primary' style={buttonsStyle} url={user._links.followers.href.split('?')[0]} accountId={accountId} auth={auth} />}
              {user.accountId !== accountId &&
                <button class='btn btn-success' onClick={() => this.rateOnClick()}>Rate</button>}
            </div>
          </div>
        </div>
        <br />
        <TabRoute auth={auth}
          tabConfigs={[
            new TabConfig(
              user._links.offered_jobs.href,
              'Offered Jobs',
              (props) => <JobsTable auth={auth} {...props} remove={accountId === user.accountId} template={offeredJobsTempl} />,
              offeredJobsTempl.expand({
                userUrl: user._links.self.href,
                offeredJobsUrl: user._links.offered_jobs.href
              }),
              '/offeredJobs/:offeredJobsUrl'
            ),
            new TabConfig(
              user._links.curricula.href,
              'Curricula',
              (props) => <div>
                <CurriculasTable auth={auth} {...props} remove={accountId === user.accountId} />
                {accountId === user.accountId ? <button class='btn btn-success btn-lg' onClick={() => history.push(createCurriculaTempl.expand({url: user._links.curricula.href.split('?')[0]}))}>New</button> : <div />}
              </div>,
              curriculasTempl.expand({
                userUrl: user._links.self.href,
                curriculaUrl: user._links.curricula.href
              }),
              '/curriculas/:curriculaUrl'
            ),
            new TabConfig(
              user._links.applications.href,
              'Applications',
              (props) => <ApplicationsTable auth={auth} remove={accountId === user.accountId} {...props} />,
              applicationsTempl.expand({
                userUrl: user._links.self.href,
                applicationUrl: user._links.applications.href
              }),
              '/applications/:applicationsUrl'
            ),
            new TabConfig(
              user._links.followers.href,
              'Followers',
              (props) => <FollowersTable auth={auth} url={URI.decode(props.match.params.followersUrl)} template={followersTempl} {...props} />,
              followersTempl.expand({
                userUrl: user._links.self.href,
                followersURL: user._links.followers.href
              }),
              '/followers/:followersUrl'
            ),
            new TabConfig(
              user._links.following.href,
              'Following',
              (props) => <FollowersTable auth={auth} url={URI.decode(props.match.params.followingUrl)} template={followingTempl} {...props} />,
              followingTempl.expand({
                userUrl: user._links.self.href,
                followingUrl: user._links.following.href
              }),
              '/following/:followingUrl'
            )
          ]}
        />
        <CommentBox url={user._links.comments.href} auth={auth} loggedAccount={accountId} accountIdFrom={accountId} accountIdTo={user.accountId} />
      </div>
    )
  }
})
