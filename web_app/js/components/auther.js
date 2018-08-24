export default class {
  constructor (loginUrl) {
    this.auth = sessionStorage.getItem('session')
    this.loginUrl = loginUrl
  }

  authenticate (auth, json) {
    this.auth = auth
    this.accountId = json.accountId
    this.self = json._links.self.href
    this.accountType = json.accountType
    sessionStorage.setItem('session', auth)
  }

  unAuthenticate () {
    this.auth = undefined
  }

  setLoginUrl (loginUrl) {
    this.loginUrl = loginUrl
  }
}
