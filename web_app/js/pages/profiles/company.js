import React from 'react'
import {withRouter, Route} from 'react-router-dom'
import HttpRequest from '../../components/httpRequest'
import URI from 'urijs'
import FollowersTable from '../tables/followersTable'
import URITemplate from 'urijs/src/URITemplate'
import CommentBox from '../../components/CommentBox'
import TabRoute, {TabConfig} from '../../components/tabRoute'
import JobsTable from '../tables/offeredJobsTable'

const style = {
  width: 200,
  height: 200,
  border: 1,
  borderRadius: '50%'
}

const followersTempl = new URITemplate('/company/{userUrl}/followers/{followersUrl}')
const followingTempl = new URITemplate('/company/{userUrl}/following/{followingUrl}')
const ratingFormTempl = new URITemplate('/rate/{url}')
const offeredJobsTempl = new URITemplate('/company/{userUrl}/offeredJobs/{offeredJobsUrl}')

export default withRouter(({match, auth, history, accountId}) => {
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
  return <HttpRequest url={URI.decode(match.params.url)}
    authorization={auth}
    onResult={json => (
      <div class='text-center container'>
        <img style={style} src={json.logoUrl || 'https://openjobs.me/assets/ico-company-default-96f4ffcb9967f09089dae7656368a5ec5489cd028f5236192e21095006cc86e1.png'} />
        <h2>{json.name}</h2>
        <HttpRequest url={new URI(json._links.followers.href.split('?')[0]).setQuery('accountToCheck', accountId).href()}
          authorization={auth}
          onResult={follows => <FollowButton follows={follows}
            url={json._links.followers.href.split('?')[0]}
          />
          }
        />
        <h3>{json.description}</h3>
        <h3>{json.specialization}</h3>
        <h3>
          Rating: {json.rating}
          {json.accountId !== accountId &&
            <button class='btn btn-success' onClick={() => history.push(ratingFormTempl.expand({
              url: json._links.ratings.href.split('?')[0]
            }) + `?type=company&from=${URI.encode(match.url)}&accountIdDest=${json.accountId}`)}>Rate this</button>}
        </h3>
        <button class='btn btn-primary bg-dark' onClick={() => window.location.href = json.webpageUrl}>WebPage</button>
        <br />
        <TabRoute auth={auth}
          tabConfigs={[
            new TabConfig(
              json._links.followers.href,
              'Followers',
              props => <FollowersTable auth={auth} url={URI.decode(props.match.params.followersUrl)} template={followersTempl} {...props} />,
              followersTempl.expand({
                companyUrl: json._links.self.href,
                followersUrl: json._links.followers.href
              }),
              '/followers/:followersUrl'
            ),
            new TabConfig(
              json._links.following.href,
              'Following',
              props => <FollowersTable auth={auth} url={URI.decode(props.match.params.followingUrl)} template={followingTempl} {...props} />,
              followingTempl.expand({
                companyUrl: json._links.self.href,
                followingUrl: json._links.following.href
              }),
              '/following/:followingUrl'
            )
          ]}
        />
        <CommentBox url={json._links.comments.href} auth={auth} accountIdFrom={accountId} accountIdTo={json.accountId} />
      </div>
    )}
  />
}
)
