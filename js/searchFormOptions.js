export class Option {
  constructor (name, placeholder, queryParam, uriTemplate, render) {
    this.name = name
    this.placeholder = placeholder
    this.queryParam = queryParam
    this.uriTemplate = uriTemplate
    this.render = render
  }
}

export class Render {
  constructor (renderFilters, renderTable) {
    this.renderFilters = renderFilters
    this.renderTable = renderTable
  }
}
