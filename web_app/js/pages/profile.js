import React from 'react'
import HttpRequest from '../components/httpRequest'
import URI from 'urijs'

export default ({auth, match}) => (
  <HttpRequest
    method='GET'
    url={URI.decode(match.params.url)}
    authorization={auth}
  />
)
