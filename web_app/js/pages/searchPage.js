import React from 'react'
import {withRouter} from 'react-router-dom'
import URI from 'urijs'

import SearchForm from '../components/searchForm'
import HttpRequest from '../components/httpRequest'

function getFormOptions (url, options) {
  const formOptions = Object.assign({}, options)
  let deleted
  for (var name in formOptions) {
    if (formOptions[name].uriTemplate === url) {
      deleted = formOptions[name]
      delete formOptions[name]
      break
    }
  }
  return [formOptions, deleted]
}

class SearchPage extends React.Component {
  // String type, URITemplate uri
  constructor (props) {
    super(props)
    console.log('search construct')

    const uri = URI.decode(props.match.params.url)
    const queryStr = URI.parse(uri).query

    const query = URI.parseQuery(queryStr)
    // if (!query.pageSize) query.pageSize = '10'
    console.log(query)

    const [formOptions, deleted] = getFormOptions(uri.split('?')[0], props.options)

    console.log(formOptions)

    this.state = {
      currentQuery: Object.assign({}, query),
      query: query,
      entity: deleted,
      uri: props.uriTemplate.expand({query: query}),
      formOptions: formOptions
    }

    this.handleNewQuery = this.handleNewQuery.bind(this)
  }

  static getDerivedStateFromProps (nextProps, prevState) {
    const aux = URI.decode(nextProps.match.params.url)
    const nextQuery = URI.parseQuery(URI.parse(aux).query)
    const nextURI = nextProps.uriTemplate.expand({query: nextQuery})

    if (nextURI === prevState.uri) return null
    const [formOptions, deleted] = getFormOptions(nextURI.split('?')[0], nextProps.options)

    return {
      currentQuery: Object.assign({}, nextQuery),
      query: nextQuery,
      entity: deleted,
      uri: nextURI,
      formOptions: formOptions
    }
  }

  handleClick () {
    const { currentQuery, query } = this.state
    if (JSON.stringify(currentQuery) !== JSON.stringify(query)) {
      const type = this.props.match.params.type

      const newURI = this.props.uriTemplate.expand({type: type, query: query})
      //console.log('newURI ' + JSON.stringify(newURI))
      this.setState({currentQuery: Object.assign({}, query), uri: newURI})

      //const newUrl = this.props.uriTemplate.expand({entityType: type, query: query})
      this.props.history.push(this.props.searchTempl.expand({url: newURI}))
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
        <SearchForm style='compact' options={this.state.formOptions} searchTempl={this.props.searchTempl} />
        <div class='row'>
          <div class='col col-lg-3'>
            <h3 class='text-center text-white border bg-dark'>Filters</h3>
            <div class='container'>
              {entity.render.renderFilters(query, this.handleNewQuery)}
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
              url={uri}
              onResult={data => entity.render.renderTable(data)} />
          </div>
        </div>
      </div>
    )
  }
}

export default withRouter(SearchPage)
