import React from 'react'

import Job from '../searchables/job'
import Company from '../searchables/company'
import User from '../searchables/user'
import HttpRequest from '../components/httpRequest'

const options = {
  jobs: Job,
  companies: Company,
  users: User
}

const numberOfItems = '?numberOfItems=10'

export default (props) => {
  const type = props.match.params.type
  const entity = options[type]
  const url = `${props.hostname}/${type}${numberOfItems}`
  return (
    <div class='container'>
      <div class='row'>
        <div class='col col-lg-3'>
          <h3 class='text-center text-white border bg-dark'>Filters</h3>
          <div class='container'>
            {entity.renderFilters()}
          </div>
        </div>
        <div class='col'>
          <h3 class='text-center text-white border bg-dark'>Results</h3>
          <HttpRequest
            url={url}
            onResult={data => entity.renderTable(data)} />
        </div>
      </div>
    </div>
  )
}
