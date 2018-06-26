import React from 'react'

import Carousel from '../components/carousel'
import SearchForm from '../components/searchForm'
import Options from '../searchFormOptions'

export default (props) => (
  <div>
    <Carousel />
    <SearchForm options={Options} />
  </div>
)
