import React from 'react'
import { withRouter } from 'react-router-dom'

export default withRouter((users) => {
  if (users._embedded) {
    const tableRows = users._embedded.items.map(item => {
      return (
        <tr key={item._links.self.href} onClick={() => users.history.push(`/accounts/users/${item.id}`)}>
          <td>{item.name}</td>
          <td>{item.rating}</td>
          <td>{item.summary}</td>
        </tr>
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

  return <p>No items found for this criteria</p>
})
