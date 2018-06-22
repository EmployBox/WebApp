import React from 'react'
import { Link } from 'react-router-dom'
import URI from 'urijs'
import URITemplate from 'urijs/src/URITemplate'

const getURI = (active, searchText) => new URITemplate('/{type}/search?{query}={value}')
  .expand({ type: URI.decode(active.name.toLowerCase()), query: active.queryParam, value: searchText })

export default class extends React.Component {
  // ({Option Array: options})
  constructor (props) {
    super(props)
    this.state = {
      active: props.options[Object.keys(props.options)[0]],
      searchText: ''
    }

    this.handleChange = this.handleChange.bind(this)
  }

  static getDerivedStateFromProps (nextProps, prevState) {
    if (Object.values(nextProps.options).indexOf(prevState.active) !== -1) return null
    return {
      active: nextProps.options[Object.keys(nextProps.options)[0]],
      searchText: ''
    }
  }

  handleChange (event) {
    const value = event.target.value

    this.setState({searchText: value})
  }

  render () {
    const { options } = this.props
    const { active, searchText } = this.state

    if (this.props.style === 'compact') {
      const listItems = Object.keys(options).map(property => {
        const option = options[property]

        let itemClass
        if (option.name === active.name) itemClass = 'btn btn-secondary active'
        else itemClass = 'btn btn-secondary'
        return (
          <button class={itemClass} onClick={() => this.setState({active: option})}>{option.name}</button>
        )
      })

      return (
        <div class='container py-3'>
          <div class='row justify-content-center'>
            {listItems}
            <form class='form-inline'>
              <input
                class='form-control form-control-lg col'
                type='text'
                value={searchText}
                placeholder={active.placeholder}
                onChange={this.handleChange} />
              <Link to={getURI(active, searchText)}>
                <button class='btn btn-primary'>Search</button>
              </Link>
            </form>
          </div>
        </div>
      )
    }

    const listItems = Object.keys(options).map(property => {
      const option = options[property]

      let itemClass
      if (option.name === active.name) itemClass = 'nav-link active'
      else itemClass = 'nav-link'
      return (
        <li class='nav-item' key={option.name}>
          <button class={itemClass} onClick={() => this.setState({active: option})}>{option.name}</button>
        </li>
      )
    })

    return (
      <div class='container py-5'>
        <h2 class='text-center'>Search {active.name}</h2>
        <ul class='nav nav-tabs'>
          {listItems}
        </ul>
        <form class='form-row'>
          <input
            class='form-control form-control-lg col'
            type='text'
            value={searchText}
            placeholder={active.placeholder}
            onChange={this.handleChange} />
          <Link to={getURI(active, searchText)}>
            <button class='btn btn-primary'>Search</button>
          </Link>
        </form>
      </div>
    )
  }
}
