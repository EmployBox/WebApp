import React from 'react'
import { withRouter } from 'react-router-dom'
import URI from 'urijs'
import URITemplate from 'urijs/src/URITemplate'

import Table from './halTable'
import ApplyButton from '../applyButton'

const searchTemplate = new URITemplate('/search/{url}')

export default withRouter(({json, match, history, accountTempl, companyTempl, moderatorTempl, jobTempl, auther}) =>
  <div class='row'>
    <div class='col'>
      <Table json={json} currentUrl={URI.decode(match.params.url)}
        pushTo={url => history.push(searchTemplate.expand({url: url}))}
        onClickRow={({rowInfo, column}) => {
          if (column.Header !== 'Name' && column.Header !== undefined) {
            history.push(jobTempl.expand({url: rowInfo.original._links.self.href}))
          }
        }}
        columns={[
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
                  return original.offerType === 'Looking for Worker' && <ApplyButton auther={auther} job={original} />
                }
              }
            ]
          }
        ]}
      />
    </div>
  </div>
)
