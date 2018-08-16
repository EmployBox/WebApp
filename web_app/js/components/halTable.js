import React from 'react'
import ReactTable from 'react-table'
import URI from 'urijs'

export default ({json, columns, currentUrl, pushTo, onClickRow}) => {
  const uri = new URI(currentUrl)
  const query = URI.parseQuery(uri.query())
  const {page, pageSize} = query

  return (<div>
    {json.size === 0
      ? <p>No Items</p>
      : <ReactTable columns={columns}
        manual
        data={json._embedded.items}
        pages={json.last_page + 1}
        defaultPageSize={Number(pageSize)}
        page={Number(page)}
        className='-striped -highlight'
        onPageChange={pageIndex => pushTo(uri.setQuery('page', pageIndex).href())}
        onPageSizeChange={pageSize => pushTo(uri.setQuery('pageSize', pageSize).href())}
        onSortedChange={(newSorted, column, shiftKey) => console.log(JSON.stringify(newSorted) + '---' + JSON.stringify(column) + '---' + shiftKey)}
        getTdProps={(state, rowInfo, column, instance) => {
          return {
            onClick: e => onClickRow({
              state,
              rowInfo,
              column,
              instance,
              event: e
            })
          }
        }}
      />}
  </div>
  )
}
