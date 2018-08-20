import React from 'react'
import HttpRequest from '../components/httpRequest'
import URI from 'urijs'
import URITemplate from 'urijs/src/URITemplate'
import Toggle from '../components/toggle'
import Table from '../components/halTable'
import {withRouter} from 'react-router-dom'

const userTempl = new URITemplate('/account/{url}')
const companyTempl = new URITemplate('/company/{url}')

export default withRouter(({auth, match, history}) => {
  console.log(URI.decode(match.params.url))
  return <HttpRequest url={URI.decode(match.params.url)}
    authorization={auth}
    onResult={jobRes =>
      <div class='text-center'>
        <h1>{jobRes.title}</h1>
        <h2>{jobRes.description}</h2>
        <h2>Wage:{jobRes.wage}</h2>
        <button onClick={() => history.push((jobRes._embedded.account.accountType === 'USR' ? userTempl : companyTempl)
          .expand({url: jobRes._embedded.account._links.self.href}))}>
          {jobRes._embedded.account.name}
        </button>
        <h2>{jobRes._embedded.account.rating}</h2>
        <HttpRequest url={jobRes._links.experiences.href} authorization={auth}
          onResult={json =>
            <Toggle text='Experiences'>
              <Table currentUrl={jobRes._links.experiences.href} json={json}
                columns={[
                  {
                  }
                ]}
              />
            </Toggle>
          }
        />
      </div>
    }
  />
}
)
