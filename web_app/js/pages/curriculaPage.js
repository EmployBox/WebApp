import React from 'react'
import {withRouter} from 'react-router-dom'
import HttpRequest from '../components/httpRequest'
import URI from 'urijs'

export default withRouter(({auth, history, match}) =>
  <HttpRequest url={URI.decode(match.params.url)}
    authorization={auth}
    onResult={json =>
      <div>
        Title: {json.title}
      </div>
    }
  />
)
