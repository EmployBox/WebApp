import React from 'react'
import {withRouter} from 'react-router-dom'
import HttpRequest from '../../components/httpRequest'
import URI from 'urijs'
import Table from '../../components/tables/halTable'
import URITemplate from 'urijs/src/URITemplate'

const template = new URITemplate('/curricula/{curriculaUrl}/experiences/{experiencesUrl}')

export default withRouter(({auth, match, history}) =>
  <HttpRequest url={URI.decode(match.params.experiencesUrl)}
    authorization={auth}
    onResult={json =>
      <Table json={json} currentUrl={URI.decode(match.params.experiencesUrl)}
        pushTo={url => history.push(template.expand({
          curriculaUrl: URI.decode(match.params.url),
          experiencesUrl: url
        }))}
        columns={[
          {
            Header: 'Competence',
            accessor: 'competence'
          },
          {
            Header: 'Years',
            accessor: 'year'
          }
        ]}
      />
    }
  />
)
