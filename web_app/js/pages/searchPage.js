import React from 'react'

import HttpRequest from '../components/httpRequest'
import Configuration from '../configuration'

const config = new Configuration()

export default (props) => {
  const entity = config.searchableEntities[props.match.params.type]
  return (
    <div class='container'>
      <div class='row'>
        <div class='col col-lg-3'>
          <h3 class='text-center text-white border bg-dark'>Filters</h3>
        </div>
        <div class='col'>
          <h3 class='text-center text-white border bg-dark'>Results</h3>
          <HttpRequest
            url={`${config.hostname}${entity.url}`}
            onResult={data => entity.renderTable(data)} />
        </div>
      </div>
    </div>
  )
}
