import React from 'react'

import Carousel from '../components/carousel'
import SearchForm from '../components/searchForm'

export default ({...rest}) => (
  <div>
    <Carousel />
    <SearchForm {...rest} />
  </div>
)
