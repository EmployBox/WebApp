import React from 'react'
import {withRouter} from 'react-router-dom'
import HttpRequest from '../../components/httpRequest'
import Table from '../../components/tables/halTable'
import URI from 'urijs'
import URITemplate from 'urijs/src/URITemplate'

const template = new URITemplate('/account/{userUrl}/curriculas/{curriculaUrl}')
const curriculaTempl = new URITemplate('/curricula/{url}')

export default withRouter(({match, history, auth}) =>
  <HttpRequest url={URI.decode(match.params.curriculaUrl)}
    authorization={auth}
    onResult={json =>
      <div class='container'>
        <Table json={json} currentUrl={URI.decode(match.params.curriculaUrl)}
          pushTo={url => history.push(template.expand({
            userUrl: URI.decode(match.params.url),
            curriculaUrl: url
          }))}
          onClickRow={({rowInfo}) => history.push(curriculaTempl.expand({
            url: rowInfo.original._links.self.href
          }))}
          columns={[
            {
              Header: 'Title',
              accessor: 'title'
            }
          ]}
        />
      </div>
    }
  />
)
