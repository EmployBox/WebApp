import React from 'react'
import {withRouter} from 'react-router-dom'
import HttpRequest from '../../components/httpRequest'
import URI from 'urijs'
import Table from '../../components/tables/halTable'
import URITemplate from 'urijs/src/URITemplate'

const template = new URITemplate('/curricula/{curriculaUrl}/projects/{projectsUrl}')

export default withRouter(({auth, match, history}) =>
  <HttpRequest url={URI.decode(match.params.projectsUrl)}
    authorization={auth}
    onResult={json =>
      <Table json={json} currentUrl={URI.decode(match.params.projectsUrl)}
        pushTo={url => history.push(template.expand({
          curriculaUrl: URI.decode(match.params.url),
          projectsUrl: url
        }))}
        columns={[
          {
            Header: 'Name',
            accessor: 'name'
          },
          {
            Header: 'Description',
            accessor: 'description'
          }
        ]}
      />
    }
  />
)
