import React from 'react'
import { Link, withRouter } from 'react-router-dom'

export default withRouter(({_embedded, history, accountTempl, companyTempl, moderatorTempl, jobTempl, authenticated, createJobsURL}) => {
  if (_embedded) {
    const tableRows = _embedded.items.map(item => {
      const { account } = item

      let templ
      if (account.accountType === 'USR') templ = accountTempl
      else if (account.accountType === 'CMP') templ = companyTempl
      else templ = moderatorTempl

      let wasLinkClicked = false

      return (
        <tr key={item._links.self.href} onClick={() => { if (!wasLinkClicked) history.push(jobTempl.expand({url: item._links.self.href})) }}>
          <td>
            <Link to={templ.expand({url: account._links.self.href})} onClick={() => { wasLinkClicked = true }}>{account.name}</Link>
          </td>
          <td>{account.rating}</td>
          <td>{item.title}</td>
          <td>{item.offerBeginDate}</td>
          <td>{item.address || 'Not defined'}</td>
          <td>{item.offerType}</td>
        </tr>
      )
    })

    const table = <table class='table table-hover'>
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

    return (
      <div>
        {authenticated
          ? <div class='row'>
            <div class='col'>
              {table}
            </div>
            <div class='col-auto'>
              <Link class='btn btn-success' to={createJobsURL}>New</Link>
            </div>
          </div>
          : <div>{table}</div>
        }
      </div>
    )
  }

  return <p>No items found for this criteria</p>
})
