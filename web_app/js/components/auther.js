export default class {
  constructor (loginUrl) {
    const session = JSON.parse(sessionStorage.getItem('session'))
    if (session) {
      this.auth = session.auth
      this.accountId = session.accountId
      this.self = session.self
      this.accountType = session.accountType
    }
    this.loginUrl = loginUrl
  }

  authenticate (auth, json) {
    this.auth = auth
    this.accountId = json.accountId
    this.self = json._links.self.href
    this.accountType = json.accountType
    sessionStorage.setItem('session', JSON.stringify(this))
  }

  unAuthenticate () {
    this.auth = undefined
    this.accountId = undefined
    this.self = undefined
    this.accountType = undefined
    sessionStorage.removeItem('session')
  }

  setLoginUrl (loginUrl) {
    this.loginUrl = loginUrl
  }
}
