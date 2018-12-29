import React from 'react'
import { withRouter } from 'react-router-dom'
import Table from './halTable'
import URI from 'urijs'
import URITemplate from 'urijs/src/URITemplate'

const searchTemplate = new URITemplate('/search/{url}')

export default withRouter(({json, history, match, companyTempl}) =>
  <Table json={json} currentUrl={URI.decode(match.params.url)}
    pushTo={url => history.push(searchTemplate.expand({url: url}))}
    onClickRow={({rowInfo}) => history.push(companyTempl.expand({url: rowInfo.original._links.self.href}))}
    columns={[
      {
        Header: 'Name',
        accessor: 'name'
      },
      {
        Header: '☆',
        id: 'rating',
        accessor: item => item.rating.toFixed(1)
      },
      {
        Header: 'Specialization',
        accessor: 'specialization'
      },
      {
        Header: 'Year Founded',
        accessor: 'yearFounded'
      }
    ]}
  />
)
