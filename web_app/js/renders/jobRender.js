import React from 'react'
import { Link } from 'react-router-dom'

import HttpRequest from '../components/httpRequest'
import Searchable from './searchable'

export default class extends Searchable {
  constructor (numberOfItems) {
    super('Jobs', 'Job\'s title', `/jobs${numberOfItems}`)
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
                <Link to={'/accounts/'}>{data.account.email}</Link>
              </td>
              <td>{data.account.rating}</td>
              <td>{data.title}</td>
              <td>{data.offerBeginDate}</td>
              <td>{data.address || 'Not defined'}</td>
              <td>{data.offerType}</td>
            </tr>
          )} />
      )
    })

    return (
      <table class='table table-hover'>
        <thead>
          <tr>
            <th>Account</th>
            <th>☆</th>
            <th>Title</th>
            <th>Posted</th>
            <th>Location</th>
            <th>Type</th>
          </tr>
        </thead>
        <tbody>
          {tableRows}
        </tbody>
      </table>
    )
  }
}
