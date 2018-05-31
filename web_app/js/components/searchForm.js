import React from 'react'
import { Link } from 'react-router-dom'

const options = ['Jobs', 'Companies', 'Users']

export default class extends React.Component {
  // ({String Array: options})
  constructor (props) {
    super(props)
    this.state = {
      active: options[0],
      searchText: ''
    }

    this.handleChange = this.handleChange.bind(this)
  }

  handleChange (event) {
    const value = event.target.value

    this.setState({searchText: value})
  }

  render () {
    const listItems = []
    for (let i = 0; i < options.length; i++) {
      let itemClass
      if (options[i] === this.state.active) itemClass = 'nav-link active'
      else itemClass = 'nav-link'
      listItems.push(
        <li class='nav-item' key={options[i]}>
          <button class={itemClass} onClick={() => this.setState({active: options[i]})}>{options[i]}</button>
        </li>
      )
    }

    const { active, searchText } = this.state

    return (
      <div class='container py-5'>
        <h2 class='text-center'>Search {this.state.active}</h2>
        <ul class='nav nav-tabs'>
          {listItems}
        </ul>
        <form class='form-row'>
          <input class='form-control form-control-lg col' type='text' value={searchText} onChange={this.handleChange} />
          <Link class='btn btn-primary' to={`/${active.charAt(0).toLowerCase() + active.slice(1)}/search?q=${searchText}`}>Search</Link>
        </form>
      </div>
    )
  }
}
