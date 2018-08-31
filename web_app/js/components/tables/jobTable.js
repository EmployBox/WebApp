import React from 'react'
import { Link, withRouter } from 'react-router-dom'
import URI from 'urijs'
import URITemplate from 'urijs/src/URITemplate'

import Table from './halTable'
import RequestButton from '../requestButton'

const searchTemplate = new URITemplate('/search/{url}')

export default withRouter(({json, match, history, accountTempl, companyTempl, moderatorTempl, jobTempl}) =>
  <div class='row'>
    <div class='col'>
      <Table json={json} currentUrl={URI.decode(match.params.url)}
        pushTo={url => history.push(searchTemplate.expand({url: url}))}
        onClickRow={({rowInfo, column}) => {
          if (column.Header !== 'Name') {
            history.push(jobTempl.expand({url: rowInfo.original._links.self.href}))
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
                Cell: ({original, value}) => <Link to={(original.account.accountType === 'USR' ? accountTempl : original.account.accountType === 'CMP' ? companyTempl : moderatorTempl)
                  .expand({url: original.account._links.self.href})}>{value}
                </Link>
              },
              {
                Header: '☆',
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
                Header: 'Opens',
                accessor: 'offerBeginDate'
              },
              {
                Header: 'Location',
                id: 'address',
                accessor: i => i.address || 'Not defined'
              },
              {
                Header: 'Type',
                accessor: 'offerType'
              },
              {
                Cell: ({original}) => {
                  console.log(original)
                }
              }
            ]
          }
        ]}
      />
    </div>
  </div>
)
