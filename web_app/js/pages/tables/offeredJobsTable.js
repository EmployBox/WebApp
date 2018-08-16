import React from 'react'
import {withRouter, Link} from 'react-router-dom'
import HttpRequest from '../../components/httpRequest'
import URI from 'urijs'
import Table from '../../components/halTable'
import URITemplate from 'urijs/src/URITemplate'

const template = new URITemplate('/account/{userUrl}/offeredJobs/{offeredJobsUrl}')
const jobTemplate = new URITemplate('/job/{url}')
const userTemplate = new URITemplate('/account/{url}')
const companyTemplate = new URITemplate('/company/{url}')

export default withRouter(({match, auth, history}) => (
  <HttpRequest url={URI.decode(match.params.offeredJobsUrl)}
    authorization={auth}
    onResult={json =>
      <div class='container'>
        <Table json={json} currentUrl={URI.decode(match.params.offeredJobsUrl)}
          pushTo={url => history.push(template.expand({
            userUrl: URI.decode(match.params.url),
            offeredJobsUrl: url
          }))}
          onClickRow={({rowInfo, column}) => {
            if (column.Header !== 'Name') {
              history.push(jobTemplate.expand({
                url: rowInfo.original._links.self.href
              }))
            }
          }}
          columns={[
            {
              Header: 'Account Info',
              columns: [
                {
                  Header: 'Name',
                  id: 'name',
                  accessor: i => i.account.name,
                  Cell: ({original, value}) => {
                    return <Link to={(original.account.accountType === 'USR' ? userTemplate : companyTemplate)
                      .expand({url: original.account._links.self.href})}>{value}
                    </Link>
                  }
                },
                {
                  Header: 'Rating',
                  id: 'rating',
                  accessor: i => i.account.rating
                }
              ]
            },
            {
              Header: 'Job Info',
              columns: [
                {
                  Header: 'Title',
                  accessor: 'title'
                },
                {
                  Header: 'Type',
                  accessor: 'offerType'
                }
              ]
            }
          ]}
        />
      </div>
    }
  />
))
