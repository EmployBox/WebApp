import React from 'react'
import HttpRequest from '../../components/httpRequest'
import URI from 'urijs'

const style = {
  width: 200,
  height: 200,
  border: 1,
  borderRadius: '50%'
}

export default ({match, auth}) => (
  <HttpRequest url={URI.decode(match.params.url)}
    authorization={auth}
    onResult={json => (
      <div>
        <img style={style} src={json.logoUrl} />
        <h2>{json.name}</h2>
        <h3>{json.description}</h3>
        <h3>{json.specialization}</h3>
        <h3>{json.rating}</h3>
        <button onClick={() => window.location.href = json.webpageUrl}>WebPage</button>
      </div>
    )}
  />
)
