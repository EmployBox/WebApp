import React from 'react'

import HttpRequest from '../components/httpRequest'

export default class Searchable {
  // String name, String placeholder, String url, function(json):<table> renderTable
  constructor (name, placeholder, url) {
    this.name = name
    this.placeholder = placeholder
    this.url = url
  }

  renderTable (json) {
    if (!json._embedded) return <p>Empty</p>
    return json._embedded.items.map(item => {
      const url = `${item._links.self.href}`
      return (
        <HttpRequest
          url={url}
          onResult={data => <p>Default</p>}
          key={url} />
      )
    })
  }
}
