import React from 'react'
import {withRouter} from 'react-router-dom'
import HttpRequest from '../../components/httpRequest'
import URI from 'urijs'
import Table from '../../components/tables/halTable'
import URITemplate from 'urijs/src/URITemplate'

const accountTempl = new URITemplate('/account/{url}')
const companyTempl = new URITemplate('/company/{url}')

export default withRouter(({match, auth, history, template, url}) => (
  <HttpRequest url={url}
    authorization={auth}
    onResult={json =>
      <div class='container'>
        <Table json={json} currentUrl={url}
          pushTo={url => history.push(template.expand({
            userUrl: URI.decode(match.params.url),
            followersUrl: url
          }))}
          onClickRow={({rowInfo}) =>
            history.push((rowInfo.original.accountType === 'USR' ? accountTempl : companyTempl)
              .expand({url: rowInfo.original._links.self.href}))
          }
          columns={[
            {
              Header: 'Name',
              accessor: 'name'
            },
            {
              Header: 'Rating',
              id: 'rating',
              accessor: item => item.rating.toFixed(1)
            }
          ]}
        />
      </div>}
  />
))
