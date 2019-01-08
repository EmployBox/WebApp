import React from 'react'
import fetch from 'isomorphic-fetch'
import URI from 'urijs'
import {checkAndParseResponse} from '../../utils/httpResponseHelper'

export default class extends React.Component {
  constructor (props) {
    super(props)
    this.state = {
      isLoading: true,
      isFollowing: false
    }
    this.toggleFollow = this.toggleFollow.bind(this)
    this.errorHandler = this.errorHandler.bind(this)
  }

  componentDidMount () {
    const { accountId, auth, url } = this.props

    let requestURL = new URI(url).setQuery('accountToCheck', accountId).href()
    fetch(requestURL, { method: 'GET', headers: { authorization: auth } })
      .then(checkAndParseResponse)
      .then(follows => this.setState({ isFollowing: follows.size !== 0, isLoading: false }))
      .catch(this.errorHandler)
  }

  errorHandler (error) {
    console.log(`FollowButton request error - ${error.message}`)
    this.setState({ isLoading: false })
  }

  toggleFollow () {
    const { isFollowing } = this.state
    const { auth } = this.props

    this.setState({ isLoading: true })

    fetch(this.props.url, { method: isFollowing ? 'DELETE' : 'PUT', headers: { authorization: auth } })
      .then(async resp => {
        let respText = await resp.text()
        if (resp.ok) this.setState({ isFollowing: !isFollowing, isLoading: false })
        else {
          this.setState({ isLoading: false })
          throw new Error(respText)
        }
      })
      .catch(this.errorHandler)
  }

  render () {
    const { isLoading, isFollowing } = this.state
    const { className, style } = this.props

    return (
      <div>
        <button class={className} style={style} onClick={this.toggleFollow} disabled={isLoading}>{isFollowing ? 'Following' : 'Follow'}</button>
        {isLoading &&
          <p>Loading...</p>}
      </div>
    )
  }
}
