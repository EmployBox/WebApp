import React from 'react'
import { Link } from 'react-router-dom'

import HttpRequest from '../components/httpRequest'

function onJobResult (data) {
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
}

export default {
  renderFilters: (query) => {
    return (
      <form>
        <div class='form-group'>
          <label>Title</label>
          <input type='text' class='form-control' placeholder='Job Title' value={query.title} />
        </div>
        <div class='form-group'>
          <label>Location</label>
          <input type='text' class='form-control' placeholder='Address' value={query.location} />
        </div>
        <div class='form-check'>
          <input class='form-check-input' type='checkbox' value={query.type === 'Looking for work'} />
          <label class='form-check-label'>Looking for work</label>
        </div>
        <div class='form-check'>
          <input class='form-check-input' type='checkbox' value={query.type === 'Looking for Worker'} />
          <label class='form-check-label'>Looking for Worker</label>
        </div>
      </form>
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
            onResult={onJobResult} />
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
