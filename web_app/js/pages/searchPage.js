import React from 'react'
import {withRouter} from 'react-router-dom'
import URI from 'urijs'
import URITemplate from 'urijs/src/URITemplate'

import SearchForm from '../components/searchForm'
import JobFilters from '../components/jobFilters'
import JobTable from '../components/jobTable'
import CompanyFilters from '../components/companyFilters'
import CompanyTable from '../components/companyTable'
import UserFilters from '../components/userFilters'
import UserTable from '../components/userTable'
import HttpRequest from '../components/httpRequest'

const options = {
  jobs: {
    renderFilters: JobFilters,
    renderTable: JobTable
  },
  companies: {
    renderFilters: CompanyFilters,
    renderTable: CompanyTable
  },
  users: {
    renderFilters: UserFilters,
    renderTable: UserTable
  }
}

const searchPageTemplate = new URITemplate('/{entityType}/search{?query*}')

class SearchPage extends React.Component {
  constructor (props) {
    super(props)
    const type = props.match.params.type

    const query = URI.parseQuery(props.location.search)
    if (!query.pageSize) query.pageSize = '10'

    const apiURITemplate = type !== 'jobs'
      ? new URITemplate(`${props.url}/accounts/{type}{?query*}`)
      : new URITemplate(`${props.url}/{type}{?query*}`)

    const formOptions = getFormOptions(type)

    this.state = {
      currentQuery: Object.assign({}, query),
      query: query,
      entity: options[type],
      apiURITemplate: apiURITemplate,
      apiURI: apiURITemplate.expand({type: type, query: query}),
      formOptions: formOptions
    }

    this.handleNewQuery = this.handleNewQuery.bind(this)
  }

  static getDerivedStateFromProps (nextProps, prevState) {
    const nextQuery = URI.parseQuery(nextProps.location.search)
    const type = nextProps.match.params.type
    const nextApiURITemplate = type !== 'jobs'
      ? new URITemplate(`${nextProps.url}/accounts/{type}{?query*}`)
      : new URITemplate(`${nextProps.url}/{type}{?query*}`)
    const nextapiURI = nextApiURITemplate.expand({type: type, query: nextQuery})

    if (nextapiURI === prevState.apiURI) return null
    const formOptions = getFormOptions(type)

    return {
      currentQuery: Object.assign({}, nextQuery),
      query: nextQuery,
      entity: options[type],
      apiURITemplate: nextApiURITemplate,
      apiURI: nextapiURI,
      formOptions: formOptions
    }
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
        <SearchForm style='compact' options={this.state.formOptions} />
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

class Option {
  constructor (name, placeholder, queryParam) {
    this.name = name
    this.placeholder = placeholder
    this.queryParam = queryParam
  }
}

function getFormOptions (type) {
  const formOptions = {
    jobs: new Option('Jobs', 'Job\'s title', 'title'),
    companies: new Option('Companies', 'Company\'s name', 'name'),
    users: new Option('Users', 'User\'s name', 'name')
  }
  delete formOptions[type]
  return formOptions
}
