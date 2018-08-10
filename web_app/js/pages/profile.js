import React from 'react'
import HttpRequest from '../components/httpRequest'
import URI from 'urijs'

const style = {
  width: 200,
  height: 200,
  border: 1,
  borderRadius: '50%'
}

export default ({auth, match}) => (
  <HttpRequest
    method='GET'
    url={URI.decode(match.params.url)}
    authorization={auth}
    onResult={json => (
      <div>
        <img style={style} src={json.photo_url} />
        <h2>{json.name}</h2>
        <h3>{json.summary}</h3>
        <h3>{json.rating}</h3>
        <button >Offered Jobs</button>
        <button >Curricula</button>
        <button >Aplications</button>
        <button >Followers</button>
        <button >Following</button>
      </div>
    )}
  />
)
