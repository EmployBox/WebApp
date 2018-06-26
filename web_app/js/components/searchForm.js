import React from 'react'
import { withRouter } from 'react-router-dom'

function getURI (active, searchText) {
  if (searchText !== '') {
    return `${active.uriTemplate}?${active.queryParam}=${searchText}`
  }
  return active.uriTemplate
}

class SearchForm extends React.Component {
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
    const { value } = event.target

    this.setState({searchText: value})
  }

  render () {
    const { options } = this.props
    const { active, searchText } = this.state

    return this.props.style === 'compact' ? this.compactForm(options, active, searchText) : this.form(options, active, searchText)
  }

  compactForm (options, active, searchText) {
    const listItems = Object.keys(options).map(property => {
      const option = options[property]
      let itemClass
      if (option.name === active.name) itemClass = 'btn btn-secondary active'
      else itemClass = 'btn btn-secondary'
      return <button class={itemClass} key={options[property].name} onClick={() => this.setState({ active: option })}>{option.name}</button>
    })
    return (
      <div class='container py-3'>
        <div class='row justify-content-center'>
          {listItems}
          <div class='form-inline' onKeyPress={event => {
            const code = event.keyCode || event.which
            if (code === 13) this.buttonClick.click()
          }}>
            <input class='form-control form-control-lg col' type='text' value={searchText} placeholder={active.placeholder} onChange={this.handleChange} />
            <button class='btn btn-primary' ref={input => { this.buttonClick = input }} onClick={() => this.props.history.push(getURI(active, searchText))}>Search</button>
          </div>
        </div>
      </div>
    )
  }

  form (options, active, searchText) {
    const listItems = Object.keys(options).map(property => {
      const option = options[property]
      let itemClass
      if (option.name === active.name) itemClass = 'nav-link active'
      else itemClass = 'nav-link'

      return (
        <li class='nav-item' key={option.name}>
          <button class={itemClass} onClick={() => this.setState({ active: option })}>{option.name}</button>
        </li>
      )
    })
    return (
      <div class='container py-5'>
        <h2 class='text-center'>Search {active.name}</h2>
        <ul class='nav nav-tabs'>
          {listItems}
        </ul>
        <div class='form-row' onKeyPress={event => {
          const code = event.keyCode || event.which
          if (code === 13) this.buttonClick.click()
        }}>
          <input class='form-control form-control-lg col' type='text' value={searchText} placeholder={active.placeholder} onChange={this.handleChange} />
          <button class='btn btn-primary' ref={input => { this.buttonClick = input }} onClick={() => this.props.history.push(getURI(active, searchText))}>Search</button>
        </div>
      </div>
    )
  }
}

export default withRouter(SearchForm)
