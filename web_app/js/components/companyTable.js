import React from 'react'
import { withRouter } from 'react-router-dom'

export default withRouter((companies) => {
  if (companies._embedded) {
    const tableRows = companies._embedded.items.map(item => {
      return (
        <tr key={item._links.self.href} onClick={() => companies.history.push(`/accounts/companies/${item.accountId}`)}>
          <td>{item.name}</td>
          <td>{item.rating}</td>
          <td>{item.specialization || 'Not defined'}</td>
          <td>{item.yearFounded || 'Not defined'}</td>
        </tr>
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

  return <p>No items found for this criteria</p>
})
