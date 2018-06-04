import JobRender from './renders/jobRender'
import CompanyRender from './renders/companyRender'
import UserRender from './renders/userRender'

export default class {
  constructor () {
    this.hostname = 'http://localhost:8080'
    this.numberOfItems = '?numberOfItems=10'

    this.jobs = new JobRender(this.numberOfItems)
    this.companies = new CompanyRender(this.numberOfItems)
    this.users = new UserRender(this.numberOfItems)

    this.searchableEntities = [this.jobs, this.companies, this.users]
  }
}
