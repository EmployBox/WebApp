import React from 'react'
import { Link } from 'react-router-dom'

export default class extends React.Component {
  // ({Searchable Array: options})
  constructor (props) {
    super(props)
    this.state = {
      active: props.options[Object.keys(props.options)[0]],
      searchText: ''
    }

    this.handleChange = this.handleChange.bind(this)
  }

  handleChange (event) {
    const value = event.target.value

    this.setState({searchText: value})
  }

  render () {
    const { options } = this.props

    const listItems = Object.keys(options).map(property => {
      const option = options[property]

      let itemClass
      if (option.name === this.state.active.name) itemClass = 'nav-link active'
      else itemClass = 'nav-link'
      return (
        <li class='nav-item' key={option.name}>
          <button class={itemClass} onClick={() => this.setState({active: option})}>{option.name}</button>
        </li>
      )
    })

    const { active, searchText } = this.state

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
          <Link class='btn btn-primary' to={`/${active.name.toLowerCase()}/search?${options.queryParam}=${searchText}`}>Search</Link>
        </form>
      </div>
    )
  }
}
