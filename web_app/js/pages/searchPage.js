import React from 'react'
import {withRouter} from 'react-router-dom'
import URI from 'urijs'
import URITemplate from 'urijs/src/URITemplate'

import Job from '../searchables/job'
import Company from '../searchables/company'
import User from '../searchables/user'
import HttpRequest from '../components/httpRequest'

const options = {
  jobs: Job,
  companies: Company,
  users: User
}

const searchPageTemplate = new URITemplate('/{entityType}/search{?query*}')

class SearchPage extends React.Component {
  constructor (props) {
    super(props)
    const type = props.match.params.type

    const query = URI.parseQuery(props.location.search)
    if (!query.pageSize) query.pageSize = '10'

    const apiURITemplate = new URITemplate(`${props.url}/{type}{?query*}`)

    this.state = {
      currentQuery: Object.assign({}, query),
      query: query,
      entity: options[type],
      apiURITemplate: apiURITemplate,
      apiURI: apiURITemplate.expand({type: type, query: query})
    }

    this.handleNewQuery = this.handleNewQuery.bind(this)
  }

  handleClick () {
    const { currentQuery, query, apiURITemplate } = this.state
    if (JSON.stringify(currentQuery) !== JSON.stringify(query)) {
      const type = this.props.match.params.type

      const newAPIURI = apiURITemplate.expand({type: type, query: query})
      this.setState({currentQuery: Object.assign({}, query), apiURI: newAPIURI})

      const newUrl = searchPageTemplate.expand({entityType: type, query: query})
      this.props.history.push(newUrl)
    }
  }

  handleNewQuery (newQuery) {
    this.setState({query: newQuery})
  }

  render () {
    const { entity, apiURI, currentQuery, query } = this.state

    let buttonClass = 'btn btn-success'
    if (JSON.stringify(currentQuery) === JSON.stringify(query)) buttonClass = buttonClass + ' disabled'

    return (
      <div class='container'>
        <div class='row'>
          <div class='col col-lg-3'>
            <h3 class='text-center text-white border bg-dark'>Filters</h3>
            <div class='container'>
              {entity.renderFilters(query, this.handleNewQuery)}
              <br />
              <div class='form-group'>
                <label>Number of Results</label>
                <select value={query.pageSize} class='form-control' onChange={(event) => { query.pageSize = event.target.value; this.setState({ query: query }) }}>
                  <option value='10'>10</option>
                  <option value='15'>15</option>
                  <option value='20'>20</option>
                  <option value='30'>30</option>
                </select>
              </div>
              <center>
                <button type='submit' class={buttonClass} onClick={() => this.handleClick()}>Search</button>
              </center>
            </div>
          </div>
          <div class='col'>
            <h3 class='text-center text-white border bg-dark'>Results</h3>
            <HttpRequest
              url={apiURI}
              onResult={data => entity.renderTable(data)} />
          </div>
        </div>
      </div>
    )
  }
}

export default withRouter(SearchPage)
