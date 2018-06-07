import React from 'react'
import { Link } from 'react-router-dom'

import HttpRequest from '../components/httpRequest'
import Searchable from './searchable'

export default class extends Searchable {
  constructor (numberOfItems) {
    super('Companies', 'Company\'s name', `/accounts/companies${numberOfItems}`)
  }

  renderTable (companies) {
    const tableRows = companies._embedded.items.map(item => {
      return (
        <HttpRequest
          key={item._links.self.href}
          url={item._links.self.href}
          onResult={data => (
            <tr>
              <td>
                <Link to={`/accounts/companies/${data.accountId}`}>{data.name}</Link>
              </td>
              <td>{data.rating}</td>
              <td>{data.specialization}</td>
              <td>{data.yearFounded}</td>
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
            <th>Specialization</th>
            <th>Year Founded</th>
          </tr>
        </thead>
        <tbody>
          {tableRows}
        </tbody>
      </table>
    )
  }
}
