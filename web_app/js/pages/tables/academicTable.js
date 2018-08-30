import React from 'react'
import HttpRequest from '../../components/httpRequest'
import URI from 'urijs'
import Table from '../../components/halTable'
import {withRouter} from 'react-router-dom'
import URITemplate from 'urijs/src/URITemplate'

const template = new URITemplate('/curricula/{curriculaUrl}/academic/{academicUrl}')

export default withRouter(({auth, history, match}) =>
  <HttpRequest url={URI.decode(match.params.academicUrl)}
    authorization={auth}
    onResult={json =>
      <div class='container'>
        <Table json={json} currentUrl={URI.decode(match.params.academicUrl)}
          pushTo={url => history.push(template.expand({
            curriculaUrl: URI.decode(match.params.url),
            academicUrl: url
          }))}
          columns={[
            {
              Header: 'Study Area',
              accessor: 'studyArea'
            },
            {
              Header: 'Institution',
              accessor: 'institution'
            },
            {
              Header: 'Degree',
              accessor: 'degreeObtained'
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
      </div>
    }
  />
)
