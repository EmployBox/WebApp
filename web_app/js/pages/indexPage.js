import React from 'react'

import Carousel from '../components/carousel'
import SearchForm from '../components/forms/searchForm'

export default ({...rest}) => (
  <div>
    <Carousel />
    <SearchForm {...rest} />
  </div>
)
