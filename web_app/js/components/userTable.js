import React from 'react'
import { withRouter } from 'react-router-dom'

export default withRouter(({_embedded, history, accountTempl}) => {
  if (_embedded) {
    const tableRows = _embedded.items.map(item => {
      return (
        <tr key={item._links.self.href} onClick={() => history.push(accountTempl.expand({url: item._links.self.href}))}>
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
