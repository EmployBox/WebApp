import React from 'react'
import {withRouter} from 'react-router-dom'
import HttpRequest from '../../components/httpRequest'
import URI from 'urijs'
import Table from '../../components/halTable'
import URITemplate from 'urijs/src/URITemplate'

const template = new URITemplate('/curricula/{curriculaUrl}/previuosJobs/{previuosJobsUrl}')

export default withRouter(({auth, history, match}) =>
  <HttpRequest url={URI.decode(match.params.previuosJobsUrl)}
    authorization={auth}
    onResult={json =>
      <Table json={json} currentUrl={URI.decode(match.params.previuosJobsUrl)}
        pushTo={url => history.push(template.expand({
          curriculaUrl: URI.decode(match.params.url),
          previuosJobsUrl: url
        }))}
        columns={[
          {
            Header: 'Company',
            accessor: 'companyName'
          },
          {
            Header: 'Work Load',
            accessor: 'workLoad'
          },
          {
            Header: 'Role',
            accessor: 'role'
          },
          {
            Header: 'Begin Date',
            accessor: 'beginDate'
          },
          {
            Header: 'End Date',
            accessor: 'endDate'
          }
        ]}
      />
    }
  />
)
