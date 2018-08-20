import React from 'react'
import {Route, withRouter} from 'react-router-dom'
import HttpRequest from '../../components/httpRequest'
import URI from 'urijs'
import URITemplate from 'urijs/src/URITemplate'
import FollowersTable from '../tables/followersTable'
import JobsTable from '../tables/offeredJobsTable'
import ApplicationsTable from '../tables/applicationsTable'
import CurriculasTable from '../tables/curriculasTable'
import CommentBox from '../../components/CommentBox';

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

export default withRouter(({auth, match, history, accountId}) => {
  const CollectionButton = ({url, title, pushTo}) => (
    <HttpRequest url={url} authorization={auth}
      onResult={json => (
        <button type='button' onClick={() => history.push(pushTo)} class='btn btn-primary bg-dark'>
          <div>
            {json.size} {title}
          </div>
        </button>
      )}
    />
  )
  const FollowButton = class extends React.Component {
    constructor (props) {
      super(props)
      this.state = {
        text: props.follows.size === 0 ? 'Follow' : 'Following',
        method: props.follows.size === 0 ? 'PUT' : 'DELETE',
        flag: false
      }
      this.changeState = this.changeState.bind(this)
      this.onClick = this.onClick.bind(this)
    }

    changeState (json) {
      this.setState(oldstate => {
        oldstate.text = oldstate.text === 'Follow' ? 'Folowing' : 'Follow'
        oldstate.method = oldstate.method === 'PUT' ? 'DELETE' : 'PUT'
        oldstate.flag = false
        return oldstate
      })
    }

    onClick () {
      this.setState(oldstate => {
        oldstate.flag = !oldstate.flag
        return oldstate
      })
    }

    render () {
      return (<div>
        <button class='btn btn-primary bg-dark' onClick={this.onClick} >{this.state.text}</button>
        {this.state.flag
          ? <HttpRequest method={this.state.method} url={this.props.url} authorization={auth}
            afterResult={this.changeState}
          />
          : <div />}
      </div>
      )
    }
  }
  return (
    <HttpRequest
      method='GET'
      url={URI.decode(match.params.url)}
      authorization={auth}
      onResult={json => (
        <div class='text-center'>
          <img style={style} src={json.photo_url} />
          <h2>{json.name}</h2>
          <HttpRequest url={new URI(json._links.followers.href.split('?')[0]).setQuery('accountToCheck', accountId).href()}
            authorization={auth}
            onResult={follows => <FollowButton follows={follows}
              url={json._links.followers.href.split('?')[0]}
            />
            }
          />
          <h3>{json.summary}</h3>
          <h4>Rating: {json.rating}</h4>
          <CollectionButton url={json._links.offered_jobs.href} title='Offered Jobs' pushTo={offeredJobsTempl.expand({
            userUrl: json._links.self.href,
            offeredJobsUrl: json._links.offered_jobs.href
          })} />
          <span> </span>
          <CollectionButton url={json._links.curricula.href} title='Curriculas' pushTo={curriculasTempl.expand({
            userUrl: json._links.self.href,
            curriculaUrl: json._links.curricula.href
          })} />
          <span> </span>
          <CollectionButton url={json._links.applications.href} title='Applications' pushTo={applicationsTempl.expand({
            userUrl: json._links.self.href,
            applicationUrl: json._links.applications.href
          })} />
          <span> </span>
          <CollectionButton url={json._links.followers.href} title='Followers' pushTo={followersTempl.expand({
            userUrl: json._links.self.href,
            followersURL: json._links.followers.href
          })} />
          <span> </span>
          <CollectionButton url={json._links.following.href} title='Following' pushTo={followingTempl.expand({
            userUrl: json._links.self.href,
            followingUrl: json._links.following.href
          })} />
          <Route path={`${match.path}/followers/:followersUrl`} component={(props) =>
            <FollowersTable auth={auth} url={URI.decode(props.match.params.followersUrl)} template={followersTempl} {...props} />} />
          <Route path={`${match.path}/applications/:applicationsUrl`} component={(props) => <ApplicationsTable auth={auth} {...props} />} />
          <Route path={`${match.path}/offeredJobs/:offeredJobsUrl`} component={(props) => <JobsTable auth={auth} {...props} />} />
          <Route path={`${match.path}/curriculas/:curriculaUrl`} component={(props) => <CurriculasTable auth={auth} {...props} />} />
          <Route path={`${match.path}/following/:followingUrl`} component={(props) =>
            <FollowersTable auth={auth} url={URI.decode(props.match.params.followingUrl)} template={followingTempl} {...props} />} />
          <CommentBox url={json._links.comments.href} auth={auth} />
        </div>
      )}
    />
  )
})
