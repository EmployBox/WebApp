import React from 'react'
import {withRouter, Route} from 'react-router-dom'
import HttpRequest from '../../components/httpRequest'
import URI from 'urijs'
import FollowersTable from '../tables/followersTable'
import URITemplate from 'urijs/src/URITemplate'

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
  return <HttpRequest url={URI.decode(match.params.url)}
    authorization={auth}
    onResult={json => (
      <div class='text-center'>
        <img style={style} src={json.logoUrl} />
        <h2>{json.name}</h2>
        <HttpRequest url={}
          <button class='btn btn-primary bg-dark' onClick={}>Follow</button>
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
      </div>
    )}
  />
}
)
