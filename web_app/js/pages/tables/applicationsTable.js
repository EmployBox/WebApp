import React from 'react'
import {withRouter} from 'react-router-dom'
import HttpRequest from '../../components/httpRequest'
import Table from '../../components/halTable'
import URI from 'urijs'
import URITemplate from 'urijs/src/URITemplate'

const template = new URITemplate('/account/{userUrl}/applications/{applicationUrl}')

export default withRouter(({match, history, auth}) =>
  <HttpRequest url={URI.decode(match.params.applicationsUrl)}
    authorization={auth}
    onResult={json =>
      <div class='container'>
        <Table json={json} currentUrl={URI.decode(match.params.applicationsUrl)}
          pushTo={url => history.push(template.expand({
            userUrl: URI.decode(match.params.url),
            applicationUrl: url
          }))}
          columns={[
            //TODO columns
          ]}
        />
      </div>
    }
  />
)
