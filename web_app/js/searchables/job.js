import React from 'react'
import { Link } from 'react-router-dom'

import HttpRequest from '../components/httpRequest'

export default {
  renderFilters: () => {
    return (
      <div class='row'>
        <div class='form-check'>
          <input class='form-check-input' type='checkbox' value='' id='defaultCheck1' />
          <label class='form-check-label' for='defaultCheck1'>
          Default checkbox
          </label>
        </div>
      </div>
    )
  },

  renderTable: (jobs) => {
    let tableRows = <tr><td>Empty</td></tr>
    if (jobs._embedded) {
      tableRows = jobs._embedded.items.map(item => {
        return (
          <HttpRequest
            key={item._links.self.href}
            url={item._links.self.href}
            onLoad={<tr><td>Loading...</td></tr>}
            onResult={data => {
              const { account } = data._embedded

              let link
              if (account.accountType === 'USR') {
                link = '/accounts/users/' + account.accountId
              } else if (account.accountType === 'CMP') {
                link = '/accounts/companies/' + account.accountId
              } else {
                link = '/accounts/moderators/' + account.accountId
              }

              return (
                <tr>
                  <td>
                    <Link to={link}>{account.name}</Link>
                  </td>
                  <td>{account.rating}</td>
                  <td>{data.title}</td>
                  <td>{data.offerBeginDate}</td>
                  <td>{data.address || 'Not defined'}</td>
                  <td>{data.offerType}</td>
                </tr>
              )
            }} />
        )
      })
    }

    return (
      <table class='table table-hover'>
        <thead>
          <tr>
            <th>Account</th>
            <th>☆</th>
            <th>Title</th>
            <th>Opens</th>
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
