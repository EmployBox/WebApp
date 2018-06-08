import React from 'react'

import Carousel from '../components/carousel'
import SearchForm from '../components/searchForm'
import Configuration from '../configuration'

const config = new Configuration()

export default (props) => (
  <div>
    <Carousel />
    <SearchForm options={config.searchableEntities} />
  </div>
)
