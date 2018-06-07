import React from 'react'
import { Link } from 'react-router-dom'

import HttpRequest from '../components/httpRequest'
import Searchable from './searchable'

export default class extends Searchable {
  constructor (numberOfItems) {
    super('Users', 'User\'s name', `/accounts/users${numberOfItems}`)
  }

  renderTable (jobs) {
    const tableRows = jobs._embedded.items.map(item => {
      return (
        <HttpRequest
          key={item._links.self.href}
          url={item._links.self.href}
          onResult={data => (
            <tr>
              <td>
                <Link to={`/accounts/users/${data.accountId}`}>{data.name}</Link>
              </td>
              <td>{data.account.rating}</td>
              <td>{data.summary}</td>
            </tr>
          )} />
      )
    })

    return (
      <table class='table table-hover'>
        <thead>
          <tr>
            <th>Name</th>
            <th>☆</th>
            <th>Summary</th>
          </tr>
        </thead>
        <tbody>
          {tableRows}
        </tbody>
      </table>
    )
  }
}
