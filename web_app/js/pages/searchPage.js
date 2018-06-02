import React from 'react'

import HttpRequest from '../components/httpRequest'

const urls = {
  jobs: 'http://localhost:8080/jobs',
  companies: 'http://localhost:8080/accounts/companies',
  users: 'http://localhost:8080/accounts/users'
}

export default (props) => {
  function render (data) {
    return data.content.map(cont => {
      const url = `http://localhost:8080${cont.value[0].links[0].href}`
      return (
        <HttpRequest
          url={url}
          onLoad={<p>Loading...</p>}
          onError={err => {
            console.log(err)
            return <p>ERROR!</p>
          }}
          onResult={data => <p>{data.title || data.name}</p>}
          key={url} />
      )
    })
  }

  return <div>
    <div class='container'>
      <div class='row'>
        <div class='col col-lg-3'>
          <h3>Filters</h3>
        </div>
        <div class='col'>
          <h3>Results</h3>
          <HttpRequest
            url={urls[props.match.params.type]}
            onResult={data => render(data)} />
        </div>
      </div>
    </div>
  </div>
}
