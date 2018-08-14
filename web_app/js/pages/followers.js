import React from 'react'
import {withRouter} from 'react-router-dom'
import HttpRequest from '../components/httpRequest'
import URI from 'urijs'

export default withRouter(({match, auth}) => (
  <HttpRequest url={URI.decode(match.params.followersUrl)} authorization={auth}
    onResult={({size, _links, _embedded}) => (
      <div class='container'>
        <table class='table table-striped table-dark'>
          <thead>
            <tr>
              <th>#</th>
              <th>Name</th>
              <th>Rating</th>
            </tr>
          </thead>
          <tbody>
            {_embedded.items.map(({name, rating}, index) => (
              <tr>
                <th>{index + 1}</th>
                <th>{name}</th>
                <th>{rating}</th>
              </tr>
            ))}
          </tbody>
        </table>
      </div>
    )}
    onError={error => (
      <div class='alert alert-danger' role='alert'>
        {JSON.parse(error.message).detail || error.message}
      </div>)}
  />
))
