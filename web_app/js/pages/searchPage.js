import React from 'react'
import {withRouter} from 'react-router-dom'
import URI from 'urijs'

import Options from '../searchFormOptions'
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

function getFormOptions (type) {
  const formOptions = Object.assign({}, Options)
  delete formOptions[type]
  return formOptions
}

class SearchPage extends React.Component {
  // String type, URITemplate uri
  constructor (props) {
    super(props)
    console.log('search construct')
    const query = URI.parseQuery(props.location.search)
    // if (!query.pageSize) query.pageSize = '10'

    const formOptions = getFormOptions(props.type)

    this.state = {
      currentQuery: Object.assign({}, query),
      query: query,
      entity: options[props.type],
      uri: props.uriTemplate.expand({query: query}),
      formOptions: formOptions
    }

    this.handleNewQuery = this.handleNewQuery.bind(this)
  }

  static getDerivedStateFromProps (nextProps, prevState) {
    const nextQuery = URI.parseQuery(nextProps.location.search)
    const nextURI = nextProps.uriTemplate.expand({query: nextQuery})

    if (nextURI === prevState.uri) return null
    const formOptions = getFormOptions(nextProps.type)

    return {
      currentQuery: Object.assign({}, nextQuery),
      query: nextQuery,
      entity: options[nextProps.type],
      uri: nextURI,
      formOptions: formOptions
    }
  }

  handleClick () {
    const { currentQuery, query } = this.state
    if (JSON.stringify(currentQuery) !== JSON.stringify(query)) {
      const type = this.props.match.params.type

      const newURI = this.props.uriTemplate.expand({type: type, query: query})
      this.setState({currentQuery: Object.assign({}, query), uri: newURI})

      const newUrl = this.props.uriTemplate.expand({entityType: type, query: query})
      this.props.history.push(newUrl)
    }
  }

  handleNewQuery (newQuery) {
    this.setState({query: newQuery})
  }

  render () {
    const { entity, uri, currentQuery, query } = this.state

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
              url={`${this.props.apiURI}${uri}`}
              onResult={data => entity.renderTable(data)} />
          </div>
        </div>
      </div>
    )
  }
}

export default withRouter(SearchPage)
