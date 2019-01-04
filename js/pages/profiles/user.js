import React from 'react'
import {withRouter} from 'react-router-dom'
import fetch from 'isomorphic-fetch'
import HttpRequest from '../../components/httpRequest'
import URI from 'urijs'
import URITemplate from 'urijs/src/URITemplate'
import FollowersTable from '../tables/followersTable'
import JobsTable from '../tables/offeredJobsTable'
import ApplicationsTable from '../tables/applicationsTable'
import CurriculasTable from '../tables/curriculasTable'
import CommentBox from '../../components/CommentBox'
import TabRoute, {TabConfig} from '../../components/tabRoute'
import FollowButton from '../../components/buttons/followButton'

const style = {
  width: 200,
  height: 200,
  border: 1,
  borderRadius: '50%'
}

const offeredJobsTempl = new URITemplate('/account/{userUrl}/offeredJobs/{offeredJobsUrl}')
const curriculasTempl = new URITemplate('/account/{userUrl}/curriculas/{curriculaUrl}')
const applicationsTempl = new URITemplate('/account/{userUrl}/applications/{applicationUrl}')
const followersTempl = new URITemplate('/account/{userUrl}/followers/{followersURL}')
const followingTempl = new URITemplate('/account/{userUrl}/following/{followingUrl}')
const ratingFormTempl = new URITemplate('/rate/{url}')

export default withRouter(class extends React.Component {
  componentDidMount () {
    const {auth, match} = this.props

    fetch(URI.decode(match.params.url), { method: 'GET', headers: { authorization: auth } }).then(async resp => {
      let json = await resp.text()
      if (resp.ok) throw new Error(json)
      return JSON.parse(json)
    })
  }

  render () {
    const {auth, match, history, accountId, createCurriculaTempl} = this.props
    return (
      <HttpRequest
        method='GET'
        url={URI.decode(match.params.url)}
        authorization={auth}
        onResult={json => (
          <div class='container'>
            <div class='row'>
              <div class='col-4'>
                <center>
                  <img style={style} src={json.photo_url || 'https://www.cukashmir.ac.in/facultyimages/2316218245609profile-default-male.png'} />
                  <div class='row'>
                    {json.accountId !== accountId &&
                    <div class='col'>
                      <HttpRequest url={new URI(json._links.followers.href.split('?')[0]).setQuery('accountToCheck', accountId).href()}
                        authorization={auth}
                        onResult={follows => <FollowButton follows={follows}
                          url={json._links.followers.href.split('?')[0]}
                        />}
                      />
                    </div>}
                    {json.accountId !== accountId &&
                    <div class='col'>
                      <button class='btn btn-block btn-success' onClick={() => history.push(ratingFormTempl.expand({
                        url: json._links.ratings.href.split('?')[0]
                      }) + `?type=user&from=${URI.encode(match.url)}&accountIdDest=${json.accountId}`)}>Rate</button>
                    </div>}
                  </div>
                </center>
              </div>
              <div class='col-8'>
                <h2>{json.name}</h2>
                <br />
                <h3>Summary</h3>
                <p>{json.summary || 'No summary available'}</p>
                <h4 class='d-flex flex-row'>Rating: {json.rating.toFixed(1)}</h4>
              </div>
            </div>
            <br />
            <TabRoute auth={auth}
              tabConfigs={[
                new TabConfig(
                  json._links.offered_jobs.href,
                  'Offered Jobs',
                  (props) => <JobsTable auth={auth} {...props} remove={accountId === json.accountId} template={offeredJobsTempl} />,
                  offeredJobsTempl.expand({
                    userUrl: json._links.self.href,
                    offeredJobsUrl: json._links.offered_jobs.href
                  }),
                  '/offeredJobs/:offeredJobsUrl'
                ),
                new TabConfig(
                  json._links.curricula.href,
                  'Curricula',
                  (props) => <div>
                    <CurriculasTable auth={auth} {...props} remove={accountId === json.accountId} />
                    {accountId === json.accountId ? <button class='btn btn-success btn-lg' onClick={() => history.push(createCurriculaTempl.expand({url: json._links.curricula.href.split('?')[0]}))}>New</button> : <div />}
                  </div>,
                  curriculasTempl.expand({
                    userUrl: json._links.self.href,
                    curriculaUrl: json._links.curricula.href
                  }),
                  '/curriculas/:curriculaUrl'
                ),
                new TabConfig(
                  json._links.applications.href,
                  'Applications',
                  (props) => <ApplicationsTable auth={auth} remove={accountId === json.accountId} {...props} />,
                  applicationsTempl.expand({
                    userUrl: json._links.self.href,
                    applicationUrl: json._links.applications.href
                  }),
                  '/applications/:applicationsUrl'
                ),
                new TabConfig(
                  json._links.followers.href,
                  'Followers',
                  (props) => <FollowersTable auth={auth} url={URI.decode(props.match.params.followersUrl)} template={followersTempl} {...props} />,
                  followersTempl.expand({
                    userUrl: json._links.self.href,
                    followersURL: json._links.followers.href
                  }),
                  '/followers/:followersUrl'
                ),
                new TabConfig(
                  json._links.following.href,
                  'Following',
                  (props) => <FollowersTable auth={auth} url={URI.decode(props.match.params.followingUrl)} template={followingTempl} {...props} />,
                  followingTempl.expand({
                    userUrl: json._links.self.href,
                    followingUrl: json._links.following.href
                  }),
                  '/following/:followingUrl'
                )
              ]}
            />
            <CommentBox url={json._links.comments.href} auth={auth} loggedAccount={accountId} accountIdFrom={accountId} accountIdTo={json.accountId} />
          </div>
        )}
      />
    )
  }
})
