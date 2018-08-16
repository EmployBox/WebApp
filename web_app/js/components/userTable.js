import React from 'react'
import { withRouter } from 'react-router-dom'
import Table from './halTable'
import URI from 'urijs'
import URITemplate from 'urijs/src/URITemplate'

const searchTemplate = new URITemplate('/search/{url}')

export default withRouter(({json, history, match, accountTempl}) => {
  console.log(json)
  return <Table json={json} currentUrl={URI.decode(match.params.url)}
    pushTo={url => history.push(searchTemplate.expand({url: url}))}
    onClickRow={({rowInfo}) => history.push(accountTempl.expand({url: rowInfo.original._links.self.href}))}
    columns={[
      {
        Header: 'Name',
        accessor: 'name'
      },
      {
        Header: 'Rating',
        accessor: 'rating'
      },
      {
        Header: 'Summary',
        accessor: 'summary'
      }
    ]}
  />
}
)

// export default withRouter(({_embedded, history, accountTempl}) => {
//   if (_embedded) {
//     const tableRows = _embedded.items.map(item => {
//       return (
//         <tr key={item._links.self.href} onClick={() => history.push(accountTempl.expand({url: item._links.self.href}))}>
//           <td>{item.name}</td>
//           <td>{item.rating}</td>
//           <td>{item.summary}</td>
//         </tr>
//       )
//     })

//     return (
//       <table class='table table-hover'>
//         <thead>
//           <tr>
//             <th>Name</th>
//             <th>☆</th>
//             <th>Summary</th>
//           </tr>
//         </thead>
//         <tbody>
//           {tableRows}
//         </tbody>
//       </table>
//     )
//   }

//   return <p>No items found for this criteria</p>
// })
