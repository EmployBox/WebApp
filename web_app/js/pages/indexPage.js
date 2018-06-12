import React from 'react'

import Carousel from '../components/carousel'
import SearchForm from '../components/searchForm'

class Option {
  constructor (name, placeholder, queryParam) {
    this.name = name
    this.placeholder = placeholder
    this.queryParam = queryParam
  }
}

const options = {
  jobs: new Option('Jobs', 'Job\'s title', 'title'),
  companies: new Option('Companies', 'Company\'s name', 'name'),
  users: new Option('Users', 'User\'s name', 'name')
}

export default (props) => (
  <div>
    <Carousel />
    <SearchForm options={options} />
  </div>
)
