import React from 'react'
import {withRouter, Route} from 'react-router-dom'
import HttpRequest from '../../components/httpRequest'
import URI from 'urijs'
import FollowersTable from '../tables/followersTable'
import URITemplate from 'urijs/src/URITemplate'
import CommentBox from '../../components/CommentBox'

const style = {
  width: 200,
  height: 200,
  border: 1,
  borderRadius: '50%'
}

const followersTempl = new URITemplate('/company/{companyUrl}/followers/{followersUrl}')
const followingTempl = new URITemplate('/company/{companyUrl}/following/{followingUrl}')

export default withRouter(({match, auth, history, accountId}) => {
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
  return <HttpRequest url={URI.decode(match.params.url)}
    authorization={auth}
    onResult={json => (
      <div class='text-center'>
        <img style={style} src={json.logoUrl} />
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
        <h3>Rating: {json.rating}</h3>
        <button class='btn btn-primary bg-dark' onClick={() => window.location.href = json.webpageUrl}>WebPage</button>
        <br />
        <CollectionButton url={json._links.followers.href} title='Followers' pushTo={followersTempl.expand({
          companyUrl: json._links.self.href,
          followersUrl: json._links.followers.href
        })} />
        <CollectionButton url={json._links.following.href} title='Following' pushTo={followingTempl.expand({
          companyUrl: json._links.self.href,
          followingUrl: json._links.following.href
        })} />
        <Route path={`${match.path}/followers/:followersUrl`} component={props =>
          <FollowersTable auth={auth} url={URI.decode(props.match.params.followersUrl)} template={followersTempl} {...props} />
        } />
        <Route path={`${match.path}/following/:followingUrl`} component={props =>
          <FollowersTable auth={auth} url={URI.decode(props.match.params.followingUrl)} template={followingTempl} {...props} />
        } />
        <CommentBox url={json._links.comments.href} auth={auth} />
      </div>
    )}
  />
}
)
