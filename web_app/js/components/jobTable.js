import React from 'react'
import { Link, withRouter } from 'react-router-dom'

export default withRouter((props) => {
  if (props._embedded) {
    const tableRows = props._embedded.items.map(item => {
      const { account } = item

      let link
      if (account.accountType === 'USR') link = '/accounts/users/' + account.accountId
      else if (account.accountType === 'CMP') link = '/accounts/companies/' + account.accountId
      else link = '/accounts/moderators/' + account.accountId

      let wasLinkClicked = false

      return (
        <tr key={item._links.self.href} onClick={() => { if (!wasLinkClicked) props.history.push('/jobs/' + item.jobId) }}>
          <td>
            <Link to={link} onClick={() => { wasLinkClicked = true }}>{account.name}</Link>
          </td>
          <td>{account.rating}</td>
          <td>{item.title}</td>
          <td>{item.offerBeginDate}</td>
          <td>{item.address || 'Not defined'}</td>
          <td>{item.offerType}</td>
        </tr>
      )
    })

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

  return <p>No items found for this criteria</p>
})
