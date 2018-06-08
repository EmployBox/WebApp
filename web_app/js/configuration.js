import SearchableJob from './searchables/job'
import SearchableCompany from './searchables/company'
import SearchableUser from './searchables/user'

export default class {
  constructor () {
    this.hostname = 'http://localhost:8080'
    this.numberOfItems = '?numberOfItems=10'

    this.searchableEntities = {
      jobs: new SearchableJob(this.numberOfItems),
      companies: new SearchableCompany(this.numberOfItems),
      users: new SearchableUser(this.numberOfItems)
    }
  }
}
