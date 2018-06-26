class Option {
  constructor (name, placeholder, queryParam, uriTemplate) {
    this.name = name
    this.placeholder = placeholder
    this.queryParam = queryParam
    this.uriTemplate = uriTemplate
  }
}

export default {
  jobs: new Option('Jobs', 'Job\'s title', 'title', '/jobs'),
  companies: new Option('Companies', 'Company\'s name', 'name', '/accounts/companies'),
  users: new Option('Users', 'User\'s name', 'name', '/accounts/users')
}
